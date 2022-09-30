package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.AdministrativeLevel;
import cc.mrbird.febs.system.service.IAdministrativeLevelService;
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
@RequestMapping("/system/administrative/level")
public class AdministrativeLevelController extends BaseController {
    @Autowired
    IAdministrativeLevelService iAdministrativeLevelService;

    @Log("新增行政级别")
    @PostMapping
    public Map<String,Object> addAdministrativeLevel(@Valid AdministrativeLevel administrativeLevel)  {
        this.iAdministrativeLevelService.createAdministrativeLevel(administrativeLevel);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改行政级别")
    @PutMapping
    public Map<String,Object> updateAdministrativeLevel(@Valid AdministrativeLevel administrativeLevel)  {
        this.iAdministrativeLevelService.updateAdministrativeLevel(administrativeLevel);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除行政级别")
    @DeleteMapping("/delete/{administrativeLevelIds}")
    public  Map<String,Object> deleteAdministrativeLevels(@NotBlank(message = "{required}") @PathVariable String administrativeLevelIds) throws FebsException {
        String[] ids = administrativeLevelIds.split(StringPool.COMMA);
        this.iAdministrativeLevelService.deleteAdministrativeLevels(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);

    }
}
