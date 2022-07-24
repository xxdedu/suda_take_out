package com.xia.suda.dto;


import com.xia.suda.entity.OrderDetail;
import com.xia.suda.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
