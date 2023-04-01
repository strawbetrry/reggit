package com.whxy.reggie.controller;

import com.whxy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String base_path;

    /**
     * 实现文件上传功能
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(@RequestPart("file") MultipartFile file){
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件名的后缀名,例:.jpg
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UID的随机数防止文件名重复
        String fileName = UUID.randomUUID().toString() + substring;
        //判断使用的路径是否存在，不存在就创建一个文件路径
        File file1 = new File(base_path);
        if(!file1.exists()){
            file1.mkdirs();
        }
        try {
            file.transferTo(new File(base_path+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //创建文件输入流
            FileInputStream fileInputStream = new FileInputStream(base_path+name);
            //创建输出流用于显示图片在浏览器上
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");
            //创建byte数组用于接收输入流的数据，然后通过该数组写到输出流
            byte[] bytes = new byte[1024];
            int length = 0;
            while( (length = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,length);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
