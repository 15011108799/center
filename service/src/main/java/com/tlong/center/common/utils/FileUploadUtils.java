package com.tlong.center.common.utils;

import com.tlong.center.web.WebUserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUploadUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebUserController.class);
    public static String upload(MultipartFile file){
        if (file.isEmpty()){
            logger.error("上传文件为空");
        }

        //获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件的文件名为:"+fileName);

        //获取文件的后缀名
        String suffixName  = fileName.substring(fileName.lastIndexOf("."));
        logger.info("上传文件的后缀名为:"+suffixName );

        //文件上传后的路径
        //TODO 自定义控制文件目录
        String filePath = "D:\\apache-tomcat-8.5.30\\webapps\\tlongPic\\";

        //解决中文问题,，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File newFile = new File(filePath + fileName);

        //检测是否存在目录就是看filepath这个文件夹是否存在
        if (!newFile.getParentFile().exists()){
            newFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(newFile);
            return "http://localhost:8080/tlongPic/"+fileName;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
