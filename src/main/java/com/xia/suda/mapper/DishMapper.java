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
import com.xia.suda.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Mapper 是 Mybatis 的注解，和Spring没有关系。@Mapper注解的的作用
 *
 * 1:为了把mapper这个DAO交給Spring管理，参考 http://412887952-qq-com.iteye.com/blog/2392672
 *
 * 2:为了不再写mapper映射文件，参考https://blog.csdn.net/weixin_39666581/article/details/103899495
 *
 * 3:为一个添加@Mapper注解的接口自动生成一个实现类
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}

