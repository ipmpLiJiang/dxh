package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.WorkExperience;
import cc.mrbird.febs.system.service.IWorkExperienceService;
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
@RequestMapping("/system/work-experience")
public class WorkExperienceController extends BaseController {

    @Autowired
    IWorkExperienceService iWorkExperienceService;
    @Log("新增工作学习经历")
    @PostMapping
    public Map<String,Object> addTitle(@Valid WorkExperience workExperience)  {
        this.iWorkExperienceService.createWorkExperience(workExperience);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改工作学习经历")
    @PutMapping
    public Map<String,Object> updateTitle(@Valid WorkExperience workExperience)  {
        this.iWorkExperienceService.updateWorkExperience(workExperience);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除工作学习经历")
    @DeleteMapping("/delete/{titleIds}")
    public Map<String,Object> deleteTitles(@NotBlank(message = "{required}") @PathVariable String titleIds) throws FebsException {
        String[] ids = titleIds.split(StringPool.COMMA);
        this.iWorkExperienceService.deleteWorkExperience(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
