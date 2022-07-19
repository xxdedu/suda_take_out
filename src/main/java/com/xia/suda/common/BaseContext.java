package com.xia.suda.common;

/**
 * @Description: 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * @Author: codedong
 * @Date:2022年07月19日15:29
 */
public class BaseContext {

    /**
     * 存id，所以是Long
     */
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
