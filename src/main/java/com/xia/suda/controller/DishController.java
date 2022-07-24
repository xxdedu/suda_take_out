package com.xia.suda.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xia.suda.common.R;
import com.xia.suda.dto.DishDto;
import com.xia.suda.entity.Category;
import com.xia.suda.entity.Dish;
import com.xia.suda.service.CategoryService;
import com.xia.suda.service.DishFlavorService;
import com.xia.suda.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private CategoryService categoryService;

    /**
     * Dish类中没有口味等字段，所以需要引入另一个类DTO，封装页面提交的数据
     * 这里是JSON数据，必须要加上@RequestBody
     *
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 1.构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        // 为了解决Dish中不包含categoryName的问题
        Page<DishDto> dishDtoPage = new Page<>();

        // 2.条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        // 3.添加构造条件
        queryWrapper.like(name != null, Dish::getName, name);

        // 4.添加排序条件 根据更新时间降序排序
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        // 执行分页查询
        dishService.page(pageInfo, queryWrapper);

        // 对象拷贝 不需要拷贝protected List<T> records; 因为这个records对应的就是页面中展示的列表数据,需要单独处理
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        // 单独处理records 将其类型List<Dish>转为List<DishDto>
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoRecords = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            // 将item中的普通属性都拷贝到dishDto中
            BeanUtils.copyProperties(item, dishDto);
            // 拿到菜品的分类id
            Long categoryId = item.getCategoryId();
            // 根据分类id去查到分类对象
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoRecords);
        return R.success(dishDtoPage);
    }
}
