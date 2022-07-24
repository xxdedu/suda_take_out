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

}
