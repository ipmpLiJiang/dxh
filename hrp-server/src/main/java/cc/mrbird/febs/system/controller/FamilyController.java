package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.Education;
import cc.mrbird.febs.system.domain.Family;
import cc.mrbird.febs.system.service.IFamilyService;
import cc.mrbird.febs.system.service.impl.FamilyServiceImpl;
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
@RequestMapping("/system/family")
public class FamilyController extends BaseController{
    @Autowired
    IFamilyService iFamilyService;
    @Log("新增家庭成员")
    @PostMapping
    public Map<String,Object> addFamily(@Valid Family family)  {
        this.iFamilyService.createFamily(family);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改家庭成员")
    @PutMapping
    public Map<String,Object> updateFamily(@Valid Family family)  {
        this.iFamilyService.updateFamily(family);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除家庭成员")
    @DeleteMapping("/delete/{familyIds}")
    public Map<String,Object> deleteFamilies(@NotBlank(message = "{required}") @PathVariable String familyIds) throws FebsException {
        String[] ids = familyIds.split(StringPool.COMMA);
        this.iFamilyService.deleteFamily(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
