package com.xia.suda.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xia.suda.entity.ShoppingCart;
import com.xia.suda.mapper.ShoppingCartMapper;
import com.xia.suda.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
