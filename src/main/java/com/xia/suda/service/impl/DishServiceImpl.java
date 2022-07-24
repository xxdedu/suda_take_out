package com.xia.suda.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xia.suda.dto.DishDto;
import com.xia.suda.entity.Dish;
import com.xia.suda.entity.DishFlavor;
import com.xia.suda.mapper.DishMapper;
import com.xia.suda.service.DishFlavorService;
import com.xia.suda.service.DishService;
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
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时插入菜品和对应的口味数据，需要操作两张表，dish、dish_flavor
     * 这里涉及到对多张表的控制，需要加入事务
     * 注意：要在启动类上加入注解才能生效：@EnableTransactionManagement
     *
     * @param dishDto
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 1.保存菜品的基本信息到菜品表dish，因为dishDto继承自dish
        this.save(dishDto);
        // 获取菜品id
        Long dishId = dishDto.getId();

        // 2.保存口味数据到dish_flavor表 flavors是一个集合，所以用saveBatch

        // 简单这么写是有问题的，此处的flavors只是封装了name和value两个属性，并没有封装dishId上，可以从上面获取到id
//        dishFlavorService.saveBatch(dishDto.getFlavors());

        // 菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 将dishId塞进flavors中
        flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
