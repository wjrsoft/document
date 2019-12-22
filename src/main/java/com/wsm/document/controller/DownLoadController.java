package com.wsm.document.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DownLoadController {
    @GetMapping("/downloadhtml")
    public String download() {
    	return "download";
    }
    
    @ResponseBody
    @RequestMapping("/queryFileList")
    public List<Map<String,String>> queryFileList(@RequestParam(value = "path") String path){
    	System.out.println(getFiles(path));
    	return getFiles(path);
    }
    
    
    public static void main(String[] args) {
//		System.out.println(JSON.toJSONString(getFiles("/code")));
	}
    /**
     * @Author：
     * @Description：获取某个目录下所有直接下级文件，不包括目录下的子目录的下的文件，所以不用递归获取
     * @Date：
     */
     public  List<Map<String,String>> getFiles(String path) {
         List<Map<String,String>> files = new ArrayList<Map<String,String>>();
         File file = new File(path);
         File[] tempList = file.listFiles();

         for (int i = 0; i < tempList.length; i++) {
             if (tempList[i].isFile()) {
            	 Map<String,String> map= new HashMap<String,String>();
            	 map.put("type", "file");
            	 map.put("path",tempList[i].getPath());
            	 map.put("name",tempList[i].getName());
                 files.add(map);
             }
             if (tempList[i].isDirectory()) {
            	 Map<String,String> map= new HashMap<String,String>();
            	 map.put("type", "direcTory");
            	 map.put("path",tempList[i].getPath());
            	 map.put("name","");
                 files.add(map);
             }
         }
         return files;
     }
    
    /**
     * 递归查询，不建议使用
     * @param strPath
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getFileList(String strPath) {
    	List fileList=new ArrayList();
    	File fileDir = new File(strPath);
        if (null != fileDir && fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();

            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    // 如果是文件夹 继续读取
                    if (files[i].isDirectory()) { 
                        getFileList(files[i].getAbsolutePath()); 
                    } else { 
                        String strFileName = files[i].getAbsolutePath();
                        if (null != strFileName && !strFileName.endsWith(".jar") && !strFileName.endsWith(".cmd")
                                && !strFileName.endsWith(".xlsx")) {
                            System.out.println("---" + strFileName);
                            fileList.add(files[i]);
                        }
                    }
                }

            } else {
                if (null != fileDir) {
                    String strFileName = fileDir.getAbsolutePath();
                    // 排除jar cmd 和 xlsx （根据需求需要）
                    if (null != strFileName && !strFileName.endsWith(".jar") && !strFileName.endsWith(".cmd")
                            && !strFileName.endsWith(".xlsx")) {
                        System.out.println("---" + strFileName);
                        fileList.add(fileDir);
                    }
                }
            }
        }
    // 定义的全去文件列表的变量
        return fileList;
    }
    
    
    /////////////////////下载文件//////////////////////////////
    @RequestMapping("/downloadFile")
    public String downloadFile(HttpServletResponse response,HttpServletRequest req) {
    	String params=req.getParameter("fileName");
    	System.out.println(params);
        String fileName = "IMG_20191215_150144.jpg";// 设置文件名，根据业务需要替换成要下载的文件名
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
