package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.JobType;
import cc.mrbird.febs.system.service.IJobTypeService;
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
@RequestMapping("/system/job/type")
public class JobTypeController extends BaseController {
    @Autowired
    IJobTypeService jobTypeService;
    @Log("新增岗位类型")
    @PostMapping
    public Map<String,Object> addJobType(@Valid JobType jobType)  {
        this.jobTypeService.createJobType(jobType);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改岗位类型")
    @PutMapping
    public Map<String,Object> updateJobType(@Valid JobType jobType)  {
        this.jobTypeService.updateJobType(jobType);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除岗位类型")
    @DeleteMapping("/delete/{JobTypeIds}")
    public Map<String,Object> deleteJobTypes(@NotBlank(message = "{required}") @PathVariable String JobTypeIds) throws FebsException {
        String[] ids = JobTypeIds.split(StringPool.COMMA);
        this.jobTypeService.deleteJobType(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);

    }
}
