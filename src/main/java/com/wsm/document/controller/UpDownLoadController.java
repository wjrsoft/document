package com.wsm.document.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @name: TestController
 * @Author: wangshimin
 * @Date: 2019/4/11  10:44
 * @Description:
 */
@Controller
public class UpDownLoadController {
    @GetMapping("/upDownload")
    public String test(){
        return "upDownload";
    }
    
    @ResponseBody
    @RequestMapping("/fileUploadLocal")
    public String fileUploadLocal(MultipartFile file){
    	System.out.println("进入fileUploadLocal");
        //判断文件是否为空
        if(file.isEmpty()){
            return "文件为空，上传失败!";
        }
        try{
            //获得文件的字节流
        	return fileSave(file);
        }catch (Exception e){
            e.printStackTrace();
            return  "上传失败";
        }
    }
    /**避免内存溢出
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public String fileSave(MultipartFile file) throws IOException {
    	InputStream fis = file.getInputStream();
        FileOutputStream fos = new FileOutputStream("/temp/"+file.getOriginalFilename());
        int len = 0;
        byte[] b = new byte[1024*10];
        while ((len=fis.read(b))!=-1){
            fos.write(b,0,len);
        }
        fos.close();
        fis.close();
        return "文件保存成功";
    }
    
    /**
     * 文件上传
     * @param file
     * @param filePath
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public  String fileSave(MultipartFile file,String filePath) throws IllegalStateException, IOException  { 
    	System.out.println("开始上传文件");
    	String dbFilePath="";//保存的文件路劲
    	String content="";
    	//文件具体路劲和文件名
    	dbFilePath="/temp/"+file.getOriginalFilename();
    	//保存文件
    	File uploadFile = new File(dbFilePath);
    	if(!uploadFile.getParentFile().exists()) {
    		uploadFile.getParentFile().mkdir();
    	}
    	file.transferTo(uploadFile);
    	System.out.println("上传文件成功");
    	file=null;
    	//存库的路劲
    	content=dbFilePath;
    	return content;
    }
    
    
    
    @ResponseBody
    @RequestMapping("/fileUploadLocal3")
    public String fileUploadLocal3(MultipartFile file){
        //判断文件是否为空
        if(file.isEmpty()){
            return "文件为空，上传失败!";
        }
        try{
            //获得文件的字节流
            byte[] bytes=file.getBytes();
            //获得path对象，也即是文件保存的路径对象
            String contextPath="/temp/";
            Path path= Paths.get(contextPath+file.getOriginalFilename());
            //调用静态方法完成将文件写入到目标路径
            Files.write(path,bytes);
            return "恭喜上传成功!";
        }catch (Exception e){
            e.printStackTrace();
            return  "上传失败";
        }
    }

    /////////////////////下载文件//////////////////////////////
    @RequestMapping("/download")
    public String downloadFile(HttpServletResponse response) {
        String fileName = "王锦荣毕业学位教师证.zip";// 设置文件名，根据业务需要替换成要下载的文件名
        if (fileName != null) {
            //设置文件路径
            String realPath = "/temp/";
            File file = new File(realPath , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
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
                    System.out.println("success");
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
        }
        return null;
    }
}
