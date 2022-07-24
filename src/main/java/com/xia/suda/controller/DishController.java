package com.xia.suda.controller;

import com.xia.suda.common.R;
import com.xia.suda.dto.DishDto;
import com.xia.suda.service.DishFlavorService;
import com.xia.suda.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 菜品管理
 * @Author: codedong
 * @Date:2022年07月22日15:01
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * Dish类中没有口味等字段，所以需要引入另一个类DTO，封装页面提交的数据
     * 这里是JSON数据，必须要加上@RequestBody
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }
}
