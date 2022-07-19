package com.xia.suda.mapper;//package com.xia.suda.mapper;
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.xia.suda.entity.Employee;
//import org.apache.ibatis.annotations.Mapper;
//
///**
// * @Description:
// * @Author: codedong
// * @Date:2022年07月12日23:32
// */
//@Mapper
//public interface EmployeeMapper extends BaseMapper<Employee> {
//
//}

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xia.suda.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}

