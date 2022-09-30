package cc.mrbird.febs.system.service;


import cc.mrbird.febs.common.exception.FebsException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public interface FileService {
    void validFile(MultipartFile file);
    Map<String,String> upLoadFile(MultipartFile file) throws FebsException;
    void downLoadFile(HttpServletResponse response, String fileName) throws FebsException, IOException;
    void downLoadOAFile(HttpServletResponse response, String fileName,String path) throws FebsException, IOException;
    void  deleteFile(String fileName,String dataType,String data,String column);
}
