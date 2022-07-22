package com.xia.suda.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xia.suda.entity.DishFlavor;
import com.xia.suda.mapper.DishFlavorMapper;
import com.xia.suda.service.DishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: codedong
 * @Date:2022年07月21日9:05
 */

@Service
@Slf4j
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
