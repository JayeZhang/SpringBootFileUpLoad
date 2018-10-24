package com.fileupload.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName: com.fileupload.fileupload.controller
 * @Description:(这里用一句话描述这个类的作用)
 * @author: JIE.ZHANG
 * @service
 * @version:V1.0.0
 * @date: 2018/10/18 14:11
 */
@Controller
public class fileUploadController {

    @Value("${file.path}")
    private String filePath;

    /**
     * 上传页面跳转
     *
     * @Title:  up
     * @param
     * @return: java.lang.String
     * @throws  Exception 	运行时异常
     * @author: JIE.ZHANG
     * @date:   2018/10/18 15:21
     *
     */
    @RequestMapping("/up")
    public String up(){
        return "up";
    }

    /**
     * 下载页面跳转
     *
     * @Title:  download
     * @param
     * @return: java.lang.String
     * @throws  Exception 	运行时异常
     * @author: JIE.ZHANG
     * @date:   2018/10/18 15:22
     *
     */
    @RequestMapping("/download")
    public String download (){

        return "download";
    }


    /**
     * 上传方法
     *
     * @Title:  upFile
     * @param file
     * @param request
     * @return: java.lang.String
     * @throws  Exception 	运行时异常
     * @author: JIE.ZHANG
     * @date:   2018/10/18 15:22
     *
     */
    @ResponseBody
    @RequestMapping("/upFile")
    public String upFile(@RequestParam("file") MultipartFile file, HttpServletRequest request){


        if (!file.isEmpty()) {

            //存放上传的文件路径
            String storePath= filePath;
            // 文件名称
            String fileName = file.getOriginalFilename();

            File filepath = new File(storePath, fileName);

            if (!filepath.getParentFile().exists()) {
                //如果目录不存在，创建目录
                filepath.getParentFile().mkdirs();

            }

            try {
                //把文件写入目标文件地址
                file.transferTo(new File(storePath+File.separator+fileName));

            } catch (Exception e) {

                e.printStackTrace();

                return "error";

            }
            //返回到成功页面
            return "success";

        }else {
            //返回到失败的页面
            return "error";
        }
    }

    /**
     * 下载方法
     *
     * @Title:  downloadFile
     * @param request
     * @param filename
     * @param model
     * @return: org.springframework.http.ResponseEntity<byte[]>
     * @throws  Exception 	运行时异常
     * @author: JIE.ZHANG
     * @date:   2018/10/18 15:23
     *
     */
    @ResponseBody
    @RequestMapping(value="/downloadFile")
    public ResponseEntity<byte[]> downloadFile(HttpServletRequest request, @RequestParam("filename") String filename, Model model) throws IOException {

        //从我们的上传文件夹中去取
        String downloadFilePath=filePath;
        //新建一个文件
        File file = new File(downloadFilePath+File.separator+filename);
        //http头信息
        HttpHeaders headers = new HttpHeaders();
        //设置编码x
        String downloadFileName = new String(filename.getBytes("UTF-8"),"iso-8859-1");

        headers.setContentDispositionFormData("attachment", downloadFileName);

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        //MediaType:互联网媒介类型  contentType：具体请求中的媒体类型信息
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);

    }
}
