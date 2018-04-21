package com.tlong.center.common.fileUpAndDown;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);


    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    /**
     * 文件上传方法
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String uploadFile(@RequestParam MultipartFile file){
        if (file.isEmpty()){
            logger.error("上传文件为空");
            return "上传文件为空";
        }

        //获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件的文件名为:"+fileName);

        //获取文件的后缀名
        String suffixName  = fileName.substring(fileName.lastIndexOf("."));
        logger.info("上传文件的后缀名为:"+suffixName );

        //文件上传后的路径
        String filePath = "E://pic//";

        //解决中文问题,，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File newFile = new File(filePath + fileName);

        //检测是否存在目录就是看filepath这个文件夹是否存在
        if (!newFile.getParentFile().exists()){
            newFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(newFile);
            return "图片上传成功!";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "图片上传失败!";
    }

    /**
     * 文件下载相关
     */
    @RequestMapping("/download")
    public String downloadFile(HttpServletResponse response, @RequestParam String fileName) {
        //处理fileName确保能找到存储的文件的名字
        if (fileName == null) {
            logger.warn("出现异常操作!要下载的文件名为空");
//            fileName = "NoNameFile";
        } else {
//            ResourceUtils.getFile("file:temp")
            String realPath = "E://pic//";
            File file = new File(realPath, fileName);
            if (file.exists()) {
                //设置强制下载打不开
                response.setContentType("application/force-download");
                //设置文件名
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + fileName);
            }
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                logger.info("文件下载完成");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return null;
    }

    //多文件上传
    @RequestMapping(value = "/batch/upload", method = RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
//        MultipartFile file = null;
//        BufferedOutputStream stream = null;
//        for (int i = 0; i < files.size(); ++i) {
//            file = files.get(i);
//            if (!file.isEmpty()) {
//                try {
//                    byte[] bytes = file.getBytes();
//                    stream = new BufferedOutputStream(new FileOutputStream(
//                            new File(file.getOriginalFilename())));
//                    stream.write(bytes);
//                    stream.close();
//
//                } catch (Exception e) {
//                    stream = null;
//                    return "You failed to upload " + i + " => "
//                            + e.getMessage();
//                }
//            } else {
//                return "You failed to upload " + i
//                        + " because the file was empty.";
//            }
//        }
//        return "upload successful";

        if (!CollectionUtils.isEmpty(files)){
            for (MultipartFile file :files){
                try {
                    this.uploadFile(file);
                }catch (Exception e){
                    return "上传失败";
                }

            }
        }
        return "上传成功!";
    }




}
