package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.EducationBeforeWork;
import cc.mrbird.febs.system.service.IEducationBeforeWorkService;
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
@RequestMapping("/system/education/before/work")
public class EducationBeforeWorkController extends BaseController {
    @Autowired
    IEducationBeforeWorkService iEducationBeforeWorkService;
    @Log("新增职前教育经历")
    @PostMapping
    public Map<String,Object> addEducationBeforeWork(@Valid EducationBeforeWork educationBeforeWork)  {
        this.iEducationBeforeWorkService.createEducationBeforeWork(educationBeforeWork);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改职前教育经历")
    @PutMapping
    public Map<String,Object> updateEducationBeforeWork(@Valid EducationBeforeWork educationBeforeWork)  {
        this.iEducationBeforeWorkService.updateEducationBeforeWork(educationBeforeWork);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除职前教育经历")
    @DeleteMapping("/delete/{educationIds}")
    public Map<String,Object> deleteEducationBeforeWorks(@NotBlank(message = "{required}") @PathVariable String educationIds) throws FebsException {
        String[] ids = educationIds.split(StringPool.COMMA);
        this.iEducationBeforeWorkService.deleteEducationBeforeWorks(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
