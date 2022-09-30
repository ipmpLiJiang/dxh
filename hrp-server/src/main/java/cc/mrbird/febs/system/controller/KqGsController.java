package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.KqGs;
import cc.mrbird.febs.system.service.IKqGsService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/kq/gs")
public class KqGsController extends BaseController {
    @Autowired
    IKqGsService iKqGsService;
    @GetMapping
    public Map<String, Object> getGs(QueryRequest request, KqGs kqGs)  {
        return getDataTable(iKqGsService.getGsList(request,kqGs));
    }
    @Log("新增工伤")
    @PostMapping
    public Map<String,Object> addGs(@Valid KqGs kqGs)  {
        this.iKqGsService.createGs(kqGs);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改工伤")
    @PutMapping
    public Map<String,Object> updateGs(@Valid KqGs kqGs)  {
        this.iKqGsService.updateGs(kqGs);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除工伤")
    @DeleteMapping("/delete/{gsIds}")
    public Map<String,Object> deleteGs(@NotBlank(message = "{required}") @PathVariable String gsIds) throws FebsException {
        String[] ids = gsIds.split(StringPool.COMMA);
        this.iKqGsService.deleteGs(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
