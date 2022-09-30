package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.ContinueEducation;
import cc.mrbird.febs.system.service.IContinueEducationService;
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
@RequestMapping("/system/continue/education")
public class ContinueEducationController extends BaseController {
    @Autowired
    IContinueEducationService iContinueEducationService;
    @Log("新增进修信息")
    @PostMapping
    public Map<String,Object> addContinueEducation(@Valid ContinueEducation continueEducation)  {
        this.iContinueEducationService.createContinueEducation(continueEducation);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改进修信息")
    @PutMapping
    public Map<String,Object> updateContinueEducation(@Valid ContinueEducation continueEducation)  {
        this.iContinueEducationService.updateContinueEducation(continueEducation);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }
    @Log("删除进修信息")
    @DeleteMapping("/delete/{continueEducationIds}")
    public Map<String,Object> deleteContinueEducation(@NotBlank(message = "{required}") @PathVariable String continueEducationIds) throws FebsException {
        String[] ids = continueEducationIds.split(StringPool.COMMA);
        this.iContinueEducationService.deleteContinueEducations(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
