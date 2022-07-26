package com.xia.suda.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xia.suda.entity.OrderDetail;
import com.xia.suda.mapper.OrderDetailMapper;
import com.xia.suda.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}