package com.xia.suda.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Description: 底层是基于代理，通过AOP把Controller中的方法拦截到，抛异常统一在这个类中进行
 * 任何Controller类中只要加了RestController、Controller都会被这个处理器进行处理
 *
 * // 此类中存在方法返回json数据，所以需要加上@ResponseBody将结果封装成json数据进行返回
 * @Author: codedong
 * @Date:2022年07月18日10:44
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 进行异常处理方法，只要Controller抛出SQLIntegrityConstraintViolationException异常，就会在这被拦截到并处理
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.error(exception.getMessage());

        // 违反了employee表中的唯一约束
        if (exception.getMessage().contains("Duplicate entry")) {
            // 取出报错信息中的账号姓名
            String[] split = exception.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * 进行异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception) {
        log.error(exception.getMessage());
        return R.error(exception.getMessage());
    }
}
