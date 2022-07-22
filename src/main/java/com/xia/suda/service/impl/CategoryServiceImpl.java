package com.xia.suda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xia.suda.common.CustomException;
import com.xia.suda.entity.Category;
import com.xia.suda.entity.Dish;
import com.xia.suda.entity.Setmeal;
import com.xia.suda.mapper.CategoryMapper;
import com.xia.suda.service.CategoryService;
import com.xia.suda.service.DishService;
import com.xia.suda.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: codedong
 * @Date:2022年07月21日9:05
 */

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前进行判断，判断当前分类是否关联了相关菜品/套餐
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        // 查询出这个id关联了多少个菜品
        // 查询当前分类是否关联了菜品，如果已经关联，抛出业务异常
        if (dishService.count(dishLambdaQueryWrapper) > 0) {
            throw new CustomException("当前分类下关联了菜品，无法删除");
        }

        // 查询当前分类是否关联了套餐，如果已经关联，抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        if (setmealService.count(setmealLambdaQueryWrapper) > 0) {
            throw new CustomException("当前分类下关联了套餐，无法删除");
        }

        // 正常删除
        super.removeById(id);
    }
}
