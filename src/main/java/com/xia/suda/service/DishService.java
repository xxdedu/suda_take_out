package com.xia.suda.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xia.suda.dto.DishDto;
import com.xia.suda.entity.Dish;

/**
 * @Description:
 * @Author: codedong
 * @Date:2022年07月21日9:03
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时插入菜品对应的口味数据，需要操作两张表，dish、dish_flavor
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id查询菜品信息和对应的口味信息
     */
    DishDto getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息，同时更新对应的口味信息
     * 需要更新菜品表dish和口味表dish_flavor
     * @param dishDto
     */
    void updateWithFlavor(DishDto dishDto);
}
