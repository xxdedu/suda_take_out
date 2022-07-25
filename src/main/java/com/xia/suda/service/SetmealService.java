package com.xia.suda.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xia.suda.dto.SetmealDto;
import com.xia.suda.entity.Setmeal;

/**
 * @Description:
 * @Author: codedong
 * @Date:2022年07月21日9:03
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 将套餐基本信息以及关联的菜品信息一起保存
     */
    void saveWithDish(SetmealDto setmealDto);
}
