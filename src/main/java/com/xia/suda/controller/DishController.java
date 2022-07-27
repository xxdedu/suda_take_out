package com.xia.suda.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xia.suda.common.R;
import com.xia.suda.dto.DishDto;
import com.xia.suda.entity.Category;
import com.xia.suda.entity.Dish;
import com.xia.suda.entity.DishFlavor;
import com.xia.suda.service.CategoryService;
import com.xia.suda.service.DishFlavorService;
import com.xia.suda.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Dish类中没有口味等字段，所以需要引入另一个类DTO，封装页面提交的数据
     * 这里是JSON数据，必须要加上@RequestBody
     *
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        // 清理缓存
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
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


    /**
     * 根据id查询菜品信息和对应的口味信息
     * 需要查两张表，可以在DishService中扩展方法
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * 需要更新菜品表dish和口味表dish_flavor
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        // 更新菜品信息，同时更新对应的口味信息
        dishService.updateWithFlavor(dishDto);

        // 清理所有菜品的缓存数据，保持数据库缓存数据一致
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);

        // 精准清理缓存，清理某个分类下面的菜品缓存数据
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);

        return R.success("修改菜品成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        List<DishDto> dishDtoList = null;
        // 动态构造key
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus(); // dish_32323424_1

        // 先从Redis中获取缓存数据 此处是根据分类进行缓存，所以此处key是分类id
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        // 如果存在，直接返回，无需查询数据库
        if (dishDtoList != null) {
            return R.success(dishDtoList);
        }

        // 如果不存在，需要查询数据库，将查询到的菜品数据缓存到Redis

        // 构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        // 只查询出状态为起售的商品
        queryWrapper.eq(Dish::getStatus, 1);
        // 添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            // 当前菜品id 获取dishFlavorList
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());

        // Redis中不存在，需要将查询结果写入Redis
        redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }

}
