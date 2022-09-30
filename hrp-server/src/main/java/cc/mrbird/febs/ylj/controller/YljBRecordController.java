package cc.mrbird.febs.ylj.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.domain.QueryRequest;

import cc.mrbird.febs.ylj.entity.YljRecord_extend;
import cc.mrbird.febs.ylj.service.IYljBRecordService;
import cc.mrbird.febs.ylj.entity.YljBRecord;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.*;

/**
 * @author viki
 * @since 2021-11-25
 */
@Slf4j
@Validated
@RestController
@RequestMapping("yljBRecord")

public class YljBRecordController extends BaseController {

    private String message;
    @Autowired
    public IYljBRecordService iYljBRecordService;

/**
 INSERT into t_menu(parent_id,menu_name,path,component,perms,icon,type,order_num,CREATE_time)
 VALUES (0,'','/dca/YljBRecord/YljBRecord','dca/YljBRecord/YljBRecord','yljBRecord:view','fork',0,1,NOW())
 SELECT MAX(MENU_ID) from t_menu;
 INSERT into t_menu(parent_id,MENU_NAME,perms,type,order_num,CREATE_time) VALUES(0,'新增','yljBRecord:add',1,1,NOW())
 INSERT into t_menu(parent_id,MENU_NAME,perms,type,order_num,CREATE_time) VALUES(0,'编辑','yljBRecord:update',1,1,NOW())
 INSERT into t_menu(parent_id,MENU_NAME,perms,type,order_num,CREATE_time) VALUES(0,'删除','yljBRecord:delete',1,1,NOW())
 */


    /**
     * 分页查询数据
     *
     * @param request    分页信息
     * @param yljBRecord 查询条件
     * @return
     */
    @GetMapping
    public Map<String, Object> List(QueryRequest request, YljBRecord yljBRecord) {
        return getDataTable(this.iYljBRecordService.findYljBRecords(request, yljBRecord));
    }

    /**
     * 添加
     *
     * @param yljBRecord
     * @return
     */
    @Log("新增/按钮")
    @PostMapping
    @RequiresPermissions("yljBRecord:add")
    public void addYljBRecord(@Valid YljBRecord yljBRecord) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            yljBRecord.setCreateUserId(currentUser.getUserId());
            this.iYljBRecordService.createYljBRecord(yljBRecord);
        } catch (Exception e) {
            message = "新增/按钮失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }

    /**
     * 修改
     *
     * @param yljBRecord
     * @return
     */
    @Log("修改")
    @PutMapping
    @RequiresPermissions("yljBRecord:update")
    public void updateYljBRecord(@Valid YljBRecord yljBRecord) throws FebsException {
        try {
            User currentUser = FebsUtil.getCurrentUser();
            yljBRecord.setModifyUserId(currentUser.getUserId());
            this.iYljBRecordService.updateYljBRecord(yljBRecord);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }


    @Log("删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("yljBRecord:delete")
    public void deleteYljBRecords(@NotBlank(message = "{required}") @PathVariable String ids) throws FebsException {
        try {
            String[] arr_ids = ids.split(StringPool.COMMA);
            this.iYljBRecordService.deleteYljBRecords(arr_ids);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @PostMapping("excel")
    public void export(QueryRequest request, YljBRecord yljBRecord, HttpServletResponse response) throws FebsException {
        try {
            List<YljBRecord> yljBRecords = this.iYljBRecordService.findYljBRecords(request, yljBRecord).getRecords();
            ExcelKit.$Export(YljBRecord.class, response).downXlsx(yljBRecords, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @GetMapping("/{id}")
    public YljBRecord detail(@NotBlank(message = "{required}") @PathVariable String id) {
        YljBRecord yljBRecord = this.iYljBRecordService.getById(id);
        return yljBRecord;
    }

    @PostMapping("importYlj")
    public FebsResponse importUser(@RequestParam MultipartFile file)
            throws IOException {
        long beginMillis = System.currentTimeMillis();

        List<YljRecord_extend> successList = Lists.newArrayList();
        List<Map<String, Object>> errorList = Lists.newArrayList();

        ExcelKit.$Import(YljRecord_extend.class)
                .readXlsx(file.getInputStream(), new ExcelReadHandler<YljRecord_extend>() {

                    @Override
                    public void onSuccess(int sheetIndex, int rowIndex, YljRecord_extend entity) {
                        successList.add(entity); // 单行读取成功，加入入库队列。
                    }

                    @Override
                    public void onError(int sheetIndex, int rowIndex,
                                        List<ExcelErrorField> errorFields) {
                        // 读取数据失败，记录了当前行所有失败的数据
                        errorList.add(MapUtil.of(//
                                "sheetIndex", sheetIndex
                        ));
                        errorList.add(MapUtil.of(//
                                "rowIndex", rowIndex));
                        errorList.add(MapUtil.of(//
                                "errorFields", errorFields));
                    }
                });

        // TODO: 执行successList的入库操作。
        Map<String, Object> result = new HashMap<>();
        result.put("data", successList);
        result.put("haveError", !CollectionUtil.isEmpty(errorList));
        result.put("error", errorList);
        result.put("timeConsuming", (System.currentTimeMillis() - beginMillis) / 1000L);


//        List<String> strRoleList = new ArrayList<>();
//        List<String> strDeptList = new ArrayList<>();
//        String strError = "";

        if (CollectionUtil.isEmpty(errorList)) {
            List<YljBRecord> insertList = new ArrayList<>();
            for (YljRecord_extend d : successList
            ) {
                YljBRecord d2 = new YljBRecord();
                BeanUtil.copyProperties(d, d2, CopyOptions.create().setIgnoreNullValue(true));
                insertList.add(d2);
            }
            if (insertList.size() > 0) {
                this.iYljBRecordService.insertYljBRecord(insertList);
            }
        }
        return new FebsResponse().data(errorList);
    }

    @GetMapping("/getXz")
    public FebsResponse getXz(String dtr) {
        ModelMap map = new ModelMap();
        int success = 0;
        List<YljBRecord> list = new ArrayList<>();
        try {
            Date bshdate = DateUtil.parse(dtr + "-01");
            Date ushdate = DateUtil.offset(bshdate, DateField.MONTH, -1);
            list = this.iYljBRecordService.getXzByIdCardAndFkssqLists(bshdate, ushdate,true);
            success = 1;
        } catch (Exception e) {
            message = "查询失败";
        }
        map.put("message", message);
        map.put("success", success);
        map.put("data", list);
        return new FebsResponse().data(map);
    }

    @GetMapping("/getJs")
    public FebsResponse getJs(String dtr) {
        ModelMap map = new ModelMap();
        int success = 0;
        Date bshdate = DateUtil.parse(dtr + "-01");
        Date ushdate = DateUtil.offset(bshdate, DateField.MONTH, -1);
        List<YljBRecord> list = new ArrayList<>();
        try {
            list = this.iYljBRecordService.getJsByIdCardAndFkssqLists(bshdate, ushdate,true);
            success = 1;
        } catch (Exception e) {
            message = "查询失败";
        }
        map.put("message", message);
        map.put("success", success);
        map.put("data", list);
        return new FebsResponse().data(map);
    }

    @GetMapping("/getBj")
    public FebsResponse getBj(String dtr) {
        ModelMap map = new ModelMap();
        int success = 0;
        List<YljBRecord> list = new ArrayList<>();
        try {
            Date bshdate = DateUtil.parse(dtr + "-01");
            list = this.iYljBRecordService.getBjByFkssqLists(bshdate);
            success = 1;
        } catch (Exception e) {
            message = "查询失败";
        }
        map.put("message", message);
        map.put("success", success);
        map.put("data", list);
        return new FebsResponse().data(map);
    }

}