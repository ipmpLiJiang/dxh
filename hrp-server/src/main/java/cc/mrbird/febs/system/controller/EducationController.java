package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.Education;
import cc.mrbird.febs.system.service.IEducationService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@RestController
@RequestMapping("/system/education")
public class EducationController extends BaseController {
    @Autowired
    IEducationService iEducationService;

    @GetMapping
    public List<Education> getOaEducationList() {
        return this.iEducationService.getOAEducation();
    }

    @Log("新增教育经历")
    @PostMapping
    public Map<String,Object> addEducation(@Valid Education education)  {
        this.iEducationService.createEducation(education);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改教育经历")
    @PutMapping
    public Map<String,Object> updateEducation(@Valid Education education)  {
        this.iEducationService.updateEducation(education);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除教育经历")
    @DeleteMapping("/delete/{educationIds}")
    public Map<String,Object> deleteEducations(@NotBlank(message = "{required}") @PathVariable String educationIds) throws FebsException {
        String[] ids = educationIds.split(StringPool.COMMA);
        this.iEducationService.deleteEducations(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }


}
