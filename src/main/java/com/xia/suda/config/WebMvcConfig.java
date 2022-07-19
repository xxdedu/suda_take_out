package com.xia.suda.config;

import com.xia.suda.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @Description:
 * @Author: codedong
 * @Date:2022年07月12日23:03
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 设置静态资源映射
     * 让mvc可以识别到backend和front中的前端资源 指定位置
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射。。。");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /**
     * 扩展MVC框架的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建消息转换器对象，将Controller中返回的R对象转换成对应的JSON，再通过输出流的方式响应到页面
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器，底层使用Jackson将Java对象转为JSON
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将上面的消息转换器对象追加到MVC框架的转换容器中
        // 注意，要将自定义的转换器放到列表第一位，表示优先使用
        converters.add(0, messageConverter);
        super.extendMessageConverters(converters);
    }
}
