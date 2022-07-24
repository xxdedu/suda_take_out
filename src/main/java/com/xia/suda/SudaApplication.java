package com.xia.suda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description:
 * @Author: codedong
 * @Date:2022年07月12日22:47
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class SudaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SudaApplication.class, args);
        log.info("项目启动成功");
    }
}
