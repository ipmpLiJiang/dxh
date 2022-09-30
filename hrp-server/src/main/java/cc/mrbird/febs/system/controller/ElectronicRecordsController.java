package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.ElectronicRecords;
import cc.mrbird.febs.system.service.IElectronicRecordsService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author MrBird
 */
@RestController
@RequestMapping("/system/electronic/records")
public class ElectronicRecordsController extends BaseController {
    @Autowired
    IElectronicRecordsService iElectronicRecordsService;
    @Log("新增电子档案")
    @PostMapping
    public Map<String,Object> addElectronicRecord(@Valid ElectronicRecords electronicRecords)  {
        this.iElectronicRecordsService.createElectronicRecord(electronicRecords);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改电子档案")
    @PutMapping
    public Map<String,Object> updateElectronicRecord(@Valid ElectronicRecords electronicRecords)  {
        this.iElectronicRecordsService.updateElectronicRecord(electronicRecords);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除电子档案")
    @DeleteMapping("/delete/{electronicRecordIds}")
    public Map<String,Object> deleteElectronicRecords(@NotBlank(message = "{required}") @PathVariable String electronicRecordIds) throws FebsException {
        String[] ids = electronicRecordIds.split(StringPool.COMMA);
        this.iElectronicRecordsService.deleteElectronicRecords(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
