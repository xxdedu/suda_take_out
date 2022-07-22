package com.xia.suda.common;

/**
 * @Description: 自定义业务异常类
 * @Author: codedong
 * @Date:2022年07月22日11:21
 */
public class CustomException extends RuntimeException{

    public CustomException(String message) {
        super(message);
    }
}
