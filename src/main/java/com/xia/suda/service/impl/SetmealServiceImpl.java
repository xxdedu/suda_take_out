package com.xia.suda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xia.suda.common.CustomException;
import com.xia.suda.dto.SetmealDto;
import com.xia.suda.entity.Setmeal;
import com.xia.suda.entity.SetmealDish;
import com.xia.suda.mapper.SetmealMapper;
import com.xia.suda.service.SetmealDishService;
import com.xia.suda.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: codedong
 * @Date:2022年07月21日9:05
 */

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 将套餐基本信息以及关联的菜品信息一起保存
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息 setmeal
        this.save(setmealDto);
        // 保存套餐和菜品的关联信息  setmeal_dish
        // 获取菜品信息 但是setmealId并没有
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除setmeal和setmeal_dish表对应的数据
     * 注意：只能删除停售状态的套餐
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {

        // 查询套餐状态，确认是否可以删除
        // select count(*) from setmeal where id in (1,2,3) and status = 1; 如果有在售卖中的套餐，抛出业务异常
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        // 不能删除，抛出业务异常
        int onSaleCount = this.count(queryWrapper);
        if (onSaleCount > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        // 删除套餐表中数据
        this.removeByIds(ids);

        // 删除关系表中的数据
        // delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }


    /**
     * 停售/起售套餐
     * @param ids
     */
    public void updateStatus(Long ids) {
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        Setmeal setmeal = this.getById(ids);
        // 此时为起售，转为停售
        if (setmeal.getStatus() == 1) {
            updateWrapper.eq(ids != null, Setmeal::getId, ids).set(Setmeal::getStatus, 0);
        }
        if (setmeal.getStatus() == 0) {
            updateWrapper.eq(ids != null, Setmeal::getId, ids).set(Setmeal::getStatus, 1);
        }
        this.update(null, updateWrapper);
    }
}
