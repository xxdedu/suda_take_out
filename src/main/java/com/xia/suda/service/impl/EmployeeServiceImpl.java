package com.xia.suda.service.impl;
//
//import com.xia.suda.entity.Employee;
//import com.xia.suda.service.EmployeeService;
//import org.springframework.stereotype.Service;
//
//
///**
// * @Description: 继承父类，实现接口
// * @Author: codedong
// * @Date:2022年07月12日23:34
// */
//@Service
//public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
//
//
//}

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xia.suda.entity.Employee;
import com.xia.suda.mapper.EmployeeMapper;
import com.xia.suda.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
