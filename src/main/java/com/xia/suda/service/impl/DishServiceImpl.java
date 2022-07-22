package com.xia.suda.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xia.suda.entity.Dish;
import com.xia.suda.mapper.DishMapper;
import com.xia.suda.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: codedong
 * @Date:2022年07月21日9:05
 */

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
