package com.xia.suda.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xia.suda.common.R;
import com.xia.suda.entity.Employee;
import com.xia.suda.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * @Description:
 * @Author: codedong
 * @Date:2022年07月12日23:39
 */

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 因为前端使用的是post请求
     * @RequestBody 接收前端传入的json数据
     * HttpServletRequest 登陆成功之后，需要将employee对象的id存入session中，表示登录成功，方便之后获取登录用户
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 1.对前端传入的密码md5加密
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        // 2.根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 等值查询
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        // 数据库中对userName添加了唯一索引 所以使用one
        Employee emp = employeeService.getOne(queryWrapper);
        if (emp == null) {
            return R.error("登陆失败，用户不存在");
        }

        // 3.数据库密码 与 前端传入代码 比对是否一致
        if (!emp.getPassword().equals(password)) {
            return R.error("登陆失败，密码错误");
        }

        // 4.查看员工状态是否可用 status 0禁用 1正常
        if (emp.getStatus() == 0) {
            return R.error("登录失败，用户已被禁用");
        }

        // 5.将员工id放入Session，返回成功结果
        request.getSession().setAttribute("employee", employee.getId());
        return R.success(emp);
    }

    /**
     * 退出登录
     * 1.清理Session中的用户id
     * 2.返回结果，返回登录界面 前端完成
     *
     * 要操作Session，所以入参需要HttpServletRequest
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！");
    }

    /**
     * 新增员工
     * @RequestBody 接收前端的JSON传参
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        if (employee == null) {
            log.info("员工信息为空！！！");
        }
        log.info("新增员工，员工信息:{}", employee.toString());
        // 给用户设置初始密码，进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employee.setId((new Date()).getTime());

        // 存入数据库
        employeeService.save(employee);
        return R.success("新增员工成功");

    }

    /**
     * 员工信息分页查询
     * 使用MybatisPlus提供的分页插件
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        // 执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }


    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询Employee
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return R.error("没有查询到对应员工信息");
        }
        return R.success(employee);
    }


}
