package com.xia.suda.controller;

import com.xia.suda.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description: 文件上传和下载
 * @Author: codedong
 * @Date:2022年07月22日13:43
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    /**
     * 引入配置文件中的路径地址
     */
    @Value("${suda.path}")
    private String basePath;

    /**
     * 此处的file必须和前端表单的名字保持一致(file)
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        // 设置文件名
        // 获得原始文件名后缀，带.
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 随机生成文件名 使用uuid 防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        // 创建一个目录对象
        File dir = new File(basePath);
        // 判断当前是否存在此目录
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * 需要response，输出流需要通过response获得
     * @param response
     * @param name
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) {
        // 输入流，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            // 输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            // 设置响应回去的是什么类型的文件
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ( (len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                // 刷新
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
