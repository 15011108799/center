package com.tlong.center.common.utils;

import com.tlong.center.web.WebUserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class FileUploadUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebUserController.class);

    public static String upload(MultipartFile file) {
        if (file.isEmpty()) {
            logger.error("上传文件为空");
        }

        //获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件的文件名为:" + fileName);

        //获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        logger.info("上传文件的后缀名为:" + suffixName);

        //文件上传后的路径
        //TODO 自定义控制文件目录
        String filePath = "D:\\apache-tomcat-8.5.30\\webapps\\tlongPic\\";

        //解决中文问题,，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File newFile = new File(filePath + fileName);

        //检测是否存在目录就是看filepath这个文件夹是否存在
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(newFile);
            return fileName + ",";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readFile(String base64Data) {
        String tempFileName = "";
        try {
            String dataPrix = "";
            String data = "";
            if (base64Data == null || "".equals(base64Data)) {
                throw new Exception("上传失败，上传图片数据为空");
            } else {
                String[] d = base64Data.split("base64,");
                if (d != null && d.length == 2) {
                    dataPrix = d[0];
                    data = d[1];
                } else {
                    throw new Exception("上传失败，数据不合法");
                }
            }
            String suffix = "";
            if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {//data:image/jpeg;base64,base64编码的jpeg图片数据
                suffix = ".jpg";
            } else if ("data:image/x-icon;".equalsIgnoreCase(dataPrix)) {//data:image/x-icon;base64,base64编码的icon图片数据
                suffix = ".ico";
            } else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {//data:image/gif;base64,base64编码的gif图片数据
                suffix = ".gif";
            } else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {//data:image/png;base64,base64编码的png图片数据
                suffix = ".png";
            } else if ("data:video/mp4;".equalsIgnoreCase(dataPrix)) {//data:image/png;base64,base64编码的png图片数据
                suffix = ".mp4";
            } else {
                throw new Exception("上传图片格式不合法");
            }
            tempFileName = UUID.randomUUID().toString() + suffix;
            byte[] bs = Base64Utils.decodeFromString(data);
            try {
                //使用apache提供的工具类操作流
                OutputStream out = new FileOutputStream("D:\\apache-tomcat-8.5.30\\webapps\\tlongPic\\" + tempFileName);
                InputStream is = new ByteArrayInputStream(bs);
                byte[] buff = new byte[1024];
                int len = 0;
                while ((len = is.read(buff)) != -1) {
                    out.write(buff, 0, len);
                }
                is.close();
                out.close();
            } catch (Exception ee) {
                throw new Exception("上传失败，写入文件失败，" + ee.getMessage());
            }
        } catch (Exception e) {

        }
        return tempFileName;
    }

    public static String handleFileUpload(List<MultipartFile> files) {
        StringBuilder picUrl = new StringBuilder();
        if (!CollectionUtils.isEmpty(files)) {
            for (MultipartFile file : files) {
                try {
                    picUrl.append(upload(file));
                } catch (Exception e) {
                    return "上传失败";
                }
            }
        }
        return picUrl.toString();
    }
}
