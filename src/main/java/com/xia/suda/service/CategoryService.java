package com.xia.suda.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xia.suda.entity.Category;

/**
 * @Description:
 * @Author: codedong
 * @Date:2022年07月21日9:03
 */
public interface CategoryService extends IService<Category> {

    void remove(Long id);
}
