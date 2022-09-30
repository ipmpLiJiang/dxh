package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.utils.EntityUtil;
import cc.mrbird.febs.system.domain.Catalog;
import cc.mrbird.febs.system.domain.CatalogExcel;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.service.ICatalogService;
import cc.mrbird.febs.system.service.ICodeService;
import cc.mrbird.febs.system.service.IEmployeeService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
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
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/catalog")
public class CatalogController extends BaseController {

    private String message;

    @Autowired
    EntityUtil entityUtil;

    @Autowired
    ICatalogService iCatalogService;

    @Autowired
    ICodeService iCodeService;

//    @Autowired
//    FebsProperties febsProperties;

    @GetMapping
    public List<Catalog> findCatalog(Integer employeeid) {
        return this.iCatalogService.findCatalog(employeeid);
    }

    @Log("新增档案目录")
    @PutMapping("/{employeeid}")
    public Map<String, Object> addCatalog(@RequestBody List<Catalog> catalogList, @PathVariable String employeeid) {
        this.iCatalogService.createCatalog(catalogList, employeeid);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS, catalogList);
    }

    @Log("修改档案目录")
    @PutMapping
    public Map<String, Object> updateCatalog(@Valid Catalog catalog) {
        this.iCatalogService.updateCatalog(catalog);
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS, null);
    }

    @Log("删除档案目录")
    @DeleteMapping("/delete/{catalogId}")
    public Map<String, Object> deleteCatalog(@NotBlank(message = "{required}") @PathVariable String catalogId) throws FebsException {
        this.iCatalogService.deleteCatalog(catalogId);
        return getResult(HttpStatus.OK, FebsConstant.DELETE_SUCCESS, null);
    }

    @PostMapping("uploadFile")
    public FebsResponse UploadFile(@RequestParam("file") MultipartFile file, String baseId, String gparname) throws FebsException {
        if (file.isEmpty()) {
            throw new FebsException(HttpStatus.BAD_REQUEST, "上传文件为空");
        }
        String filename = file.getOriginalFilename();
        if (!StringUtils.endsWith(filename, ".pdf")) {
            throw new FebsException(HttpStatus.BAD_REQUEST, "只支持.pdf类型文件导入");
        }
        ModelMap map = new ModelMap();
        String suffixName = filename.substring(filename.lastIndexOf("."));  // 后缀名
//        suffixName = suffixName.toLowerCase();

        String filePath = "c:/qc/upload/"; // 上传后的路径
        String serName = UUID.randomUUID().toString() + suffixName;
//        File dest = new File(filePath + "/" + gparname + "/" + code + "/" + parent + "/" + serName);
        File dest = new File(filePath + "/" + gparname + "/" + serName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);

        } catch (IOException e) {
            message = "导入pdf失败," + e.getMessage();
            log.error(message);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
//        String fileUrl = "upload/" + gparname + "/" + code + "/" + parent + "/" + serName;
        String fileUrl = "upload/" + gparname + "/" + serName;

        map.put("uid", baseId);
        map.put("success", 1);
        map.put("name", filename);
        map.put("status", "done");
        map.put("url", fileUrl);
        map.put("serName", serName);
        return new FebsResponse().put("data", map);
    }

    /**
     * 导入Excel数据，并批量插入 Catalog表
     */
    @PostMapping("import")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file, String gparname) throws FebsException {
        try {
            if (file.isEmpty()) {
                throw new FebsException(HttpStatus.BAD_REQUEST, "导入数据为空");
            }
            String filename = file.getOriginalFilename();
            if (!StringUtils.endsWith(filename, ".xlsx")) {
                throw new FebsException(HttpStatus.BAD_REQUEST, "只支持.xlsx类型文件导入");
            }
            // 开始导入操作
            long beginTimeMillis = System.currentTimeMillis();
            final List<CatalogExcel> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            ExcelKit.$Import(CatalogExcel.class).readXlsx(file.getInputStream(), new ExcelReadHandler<CatalogExcel>() {
                @Override
                public void onSuccess(int sheet, int row, CatalogExcel excel) {
                    // 数据校验成功时，加入集合
                    data.add(excel);
                }

                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });

            if (!data.isEmpty()) {
                // 数据分组，减少下面循环
                Map<String, List<CatalogExcel>> groupExcel = data.stream().collect(Collectors.groupingBy(CatalogExcel::getCode));
                List<CatalogExcel> excelQuery = Lists.newArrayList();
                List<CatalogExcel> excelLbQuery = Lists.newArrayList();
                List<Catalog> insert = Lists.newArrayList();
                // 获取人事编码
                List<Code> codeList = iCodeService.list();
                List<Code> codeQuery = Lists.newArrayList();
                // 获取所有数据
                List<Catalog> catalogList = iCatalogService.list();
                // 获取动态数据
                List<Catalog> dataList = catalogList.stream().filter(s -> !s.getParentid().equals("0") && s.getEmployeeid() != null).collect(Collectors.toList());
                // 获取根数据
                List<Catalog> parentList = catalogList.stream().filter(s -> s.getParentid().equals("0")).collect(Collectors.toList());
                List<Catalog> catalogQuery = Lists.newArrayList();
                List<Catalog> catalogEmployeeQuery = Lists.newArrayList();
                List<Catalog> catalogSortQuery = Lists.newArrayList();
                Integer maxSecondno = 0;
                for (String code : groupExcel.keySet()) {
                    excelQuery = groupExcel.get(code);// 根据 人事编码 查 employeeid
                    codeQuery = codeList.stream().filter(s -> s.getEmployeecode().equals(code)).collect(Collectors.toList());
                    if (codeQuery.size() > 0) {
                        Code codeEntity = codeQuery.get(0);
                        catalogEmployeeQuery = dataList.stream().filter(s ->
                                s.getEmployeeid().equals(Long.valueOf(codeEntity.getEmployeeid()))
                        ).collect(Collectors.toList());

                        Map<String, List<CatalogExcel>> groupLbExcel = excelQuery.stream().collect(Collectors.groupingBy(CatalogExcel::getLbname));
                        for (String lbName : groupLbExcel.keySet()) {
                            excelLbQuery = groupLbExcel.get(lbName);
                            // 大类别
                            catalogQuery = parentList.stream().filter(s -> s.getTextname().equals(lbName)).collect(Collectors.toList());
                            if (catalogQuery.size() > 0) {
                                // 类别ID
                                String pid = catalogQuery.get(0).getId();
                                catalogSortQuery = catalogEmployeeQuery.stream().filter(s ->
                                        s.getParentid().equals(pid)
                                ).collect(Collectors.toList());

                                if(catalogSortQuery.size() > 0) {
                                    catalogSortQuery.sort(Comparator.comparing(Catalog::getSecondno).reversed());
                                    maxSecondno = catalogSortQuery.get(0).getSecondno();
                                } else {
                                    maxSecondno = 0;
                                }

                                excelLbQuery.sort(Comparator.comparing(CatalogExcel::getSecondno));
                                for (CatalogExcel excel : excelLbQuery) {
                                    maxSecondno += 1;
                                    Catalog entity = new Catalog();
                                    entityUtil.getEntity(excel, entity);

                                    if (StringUtils.isNotBlank(excel.getFilename())) {
                                        entity.setSername("upload/" + gparname + "/" + excel.getFilename() + ".pdf");
                                        entity.setFilename(excel.getFilename() + ".pdf");
                                    } else {
                                        entity.setSername("");
                                        entity.setFilename("");
                                    }
                                    entity.setSecondno(maxSecondno);
                                    entity.setId(UUID.randomUUID().toString());
                                    entity.setEmployeeid(Long.valueOf(codeEntity.getEmployeeid()));
                                    entity.setParentid(pid);
                                    insert.add(entity);
                                }
                            }
                        }
                    }
                }
                // 将合法的记录批量入库
                if (insert.size() > 0)
                    this.iCatalogService.saveOrUpdateBatch(insert);
            }
            long time = ((System.currentTimeMillis() - beginTimeMillis));
            ImmutableMap<String, Object> result = ImmutableMap.of(
                    "time", time,
                    "data", data,
                    "error", error
            );
            return new FebsResponse().data(result);
        } catch (Exception e) {
            message = "导入Excel数据失败," + e.getMessage();
            log.error(message);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }

}
