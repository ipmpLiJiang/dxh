package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.ComFile;
import cc.mrbird.febs.system.domain.InUploadFile;
import cc.mrbird.febs.system.domain.OutComFile;
import cc.mrbird.febs.system.service.IComFileService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("comFile")
public class ComFileController extends BaseController {
    private String message;
    @Autowired
    IComFileService iComFileService;

    @GetMapping
    public List<ComFile> ComFileList(String id) {
        return this.iComFileService.findComFile(id);
    }

    @Log("新增附件信息")
    @PostMapping
    public Map<String, Object> addComFile(@Valid ComFile code) {
        this.iComFileService.createComFile(code);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS, null);
    }

    @Log("修改附件信息")
    @PutMapping
    public Map<String, Object> updateComFile(@Valid ComFile code) {
        this.iComFileService.updateComFile(code);
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS, null);
    }

    @Log("删除附件信息")
    @DeleteMapping("/delete/{codeIds}")
    public Map<String, Object> deleteComFiles(@NotBlank(message = "{required}") @PathVariable String codeIds) throws FebsException {
        String[] ids = codeIds.split(StringPool.COMMA);
        this.iComFileService.deleteComFiles(ids);
        return getResult(HttpStatus.OK, FebsConstant.DELETE_SUCCESS, null);
    }

    @PostMapping("fileList")
    public FebsResponse findImgListComFiles(InUploadFile inUploadFile) {
        List<OutComFile> outList = new ArrayList<>();
        try {
            LambdaQueryWrapper<ComFile> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ComFile::getRefTabId, inUploadFile.getId());
            if (StringUtils.isNotBlank(inUploadFile.getRefTab())) {
                wrapper.eq(ComFile::getRefTabTable, inUploadFile.getRefTab());
            }
            wrapper.eq(ComFile::getIsDeletemark, 1);
            List<ComFile> list = this.iComFileService.list(wrapper);
            if (list.size() > 0) {
                for (ComFile item : list) {
                    String fileUrl = "upload/" + item.getServerName();
                    OutComFile outComFile = new OutComFile();
                    outComFile.setUid(item.getId());
                    outComFile.setName(item.getClientName());
                    outComFile.setStatus("done");
                    outComFile.setUrl(fileUrl);
                    outComFile.setSerName(item.getServerName());
                    outComFile.setThumbUrl(fileUrl);
                    outList.add(outComFile);
                }
            }
        } catch (Exception e) {
            log.error(message, e);
        }
        return new FebsResponse().data(outList);
    }

    @PostMapping("uploadFile")
    public FebsResponse UploadFile(@RequestParam("file") MultipartFile file, InUploadFile inUploadFile) throws FebsException {
        int success = 0;
        ModelMap map = new ModelMap();
        message = "";
        if (file.isEmpty()) {
            message = "空文件";
        }
        if (StringUtils.isBlank(message)) {
            Date thisDate = new Date();
            String strId = inUploadFile.getId();
            String fileName2 = file.getOriginalFilename();  // 文件名
            String suffixName = fileName2.substring(fileName2.lastIndexOf("."));  // 后缀名
            suffixName = suffixName.toLowerCase();
            String[] sufArr = inUploadFile.getSuffix().split(",");
            boolean isSuf = false;
            for (String suffix : sufArr) {
                if (suffixName.equals(suffix)) {
                    isSuf = true;
                    break;
                }
            }
            if (isSuf) {
                String serPath = "c:/qc/upload/"; // 上传后的路径
                String fileName = UUID.randomUUID().toString() + suffixName;
                File dest = new File(serPath + fileName);
                String Id = UUID.randomUUID().toString();
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    file.transferTo(dest);
                    ComFile cf = new ComFile();
                    cf.setId(Id);
                    cf.setCreateTime(thisDate);
                    cf.setClientName(fileName2);//客户端的名称
                    cf.setServerName(fileName);
                    cf.setRefTabId(strId);
                    cf.setRefTabTable(inUploadFile.getRefTab());
                    iComFileService.createComFile(cf);
                    success = 1;
                } catch (IOException e) {
                    success = 0;
                    message = "上传文件失败、异常";
                    log.error(message, e);
                }
                if (success == 1) {
                    String fileUrl = "upload/" + fileName;

                    map.put("success", success);
                    map.put("uid", Id);
                    map.put("name", fileName2);
                    map.put("status", "done");
                    map.put("url", fileUrl);
                    map.put("thumbUrl", fileUrl);
                    map.put("serName", fileName);
                } else {
                    map.put("success", success);
                    map.put("message", message);
                }
            } else {
                map.put("success", success);
                map.put("message", "上传文件的格式不正确，应上传" +inUploadFile.getSuffix()+ "格式.");
            }
        } else {
            map.put("success", success);
            map.put("message", message);
        }
        return new FebsResponse().put("data", map);
    }

    @Log("删除")
    @PostMapping("deleteFile")
    public FebsResponse deleteFile(InUploadFile inUploadFile) {
        ModelMap map = new ModelMap();
        int success = 0;
        try {
            String strId = inUploadFile.getId();
            ComFile comFile = this.iComFileService.getById(strId);
            if (comFile != null) {
                String[] ids = {inUploadFile.getId()};
                this.iComFileService.deleteComFiles(ids);
                success = 1;
//                String filePath = febsProperties.getUploadPath(); // 上传后的路径
//                String fileUrl = filePath + "/" + comFile.getServerName();
//                delete(fileUrl);
            }
        } catch (Exception e) {
            message = "删除失败.";
            log.error(message, e);
        }
        map.put("message", message);
        map.put("success", success);
        return new FebsResponse().data(map);
    }

    public boolean delete(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                flag = true;
            }
        } else {
            flag = true;
        }
        return flag;
    }
}
