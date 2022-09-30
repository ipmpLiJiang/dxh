package cc.mrbird.febs.system.service.impl;


import cc.mrbird.febs.common.exception.FebsException;

import cc.mrbird.febs.common.utils.HTTPTEST;
import cc.mrbird.febs.system.service.*;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Slf4j
@Service("fileService")
public class FileServiceImpl implements FileService {
    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    IEducationService iEducationService;

    @Autowired
    IElectronicRecordsService iElectronicRecordsService;

    @Autowired
    IJobTypeService iJobTypeService;
    @Autowired
    ITitleService iTitleService;

    @Autowired
    IAdministrativeLevelService iAdministrativeLevelService;

    @Autowired
    IAdministrativePostService iAdministrativePostService;
    private static String filePath ="c:/qc/upload/";

    public void validFile(MultipartFile file){
        if (file.isEmpty()) {
            throw new FebsException(HttpStatus.BAD_REQUEST,"导入数据为空");
        }
        String filename = file.getOriginalFilename();
        if (!StringUtils.endsWith(filename, ".xlsx")) {
            throw new FebsException(HttpStatus.BAD_REQUEST,"只支持.xlsx类型文件导入");
        }
    }
    /**
     *
     * @param file
     * @return 处理之后的文件名  存入数据库 作为再次下载的文件名
     */
    public Map<String,String> upLoadFile( MultipartFile file) throws FebsException {
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
        int random = (int) (Math.random() * 900 + 100);
        String name = System.currentTimeMillis() + "-" + random;
        String fileName = name + suffix;
        File serverFile = new File(filePath + fileName);
        try {
            file.transferTo(serverFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FebsException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        Map<String,String> map=new HashMap<>();
        map.put("path","http://129.211.4.36:1099/img/"+fileName);
        map.put("fileName","6910ab7bgw1egloghsfi3j20b40b40t6.jpg");
        return map;
    }

    /**
     * 文件下载 通过数据库存入的文件名来下载
     *
     * @param response
     * @param fileName
     */
    public void downLoadFile(HttpServletResponse response, String fileName) throws FebsException{
        //HTTPTEST.getFile(response);
        File file = new File(filePath + fileName);
        if (file.exists()) {
            response.setCharacterEncoding("utf-8");
            //自动判断下载文件类型
            response.setContentType("multipart/form-data");
            //设置文件头  Content-Disposition 让浏览器弹出下载对话框
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new FebsException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                throw new FebsException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new FebsException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } else {
            throw new FebsException(HttpStatus.BAD_REQUEST, "文件不存在");
        }
    }

    /**
     * 根据文件名删除文件
     *
     * @param fileName
     */
    public void  deleteFile(String fileName,String dataType,String data,String column) {
        File file = new File(filePath + fileName);
        if (file.exists()) {
            file.delete();
        }
        //this.updatedata(null,dataType,data,column);
    }

    public void downLoadOAFile(HttpServletResponse response, String fileName,String path) throws FebsException, UnsupportedEncodingException {
        HTTPTEST.getFile(response,fileName,path);
    }

}
