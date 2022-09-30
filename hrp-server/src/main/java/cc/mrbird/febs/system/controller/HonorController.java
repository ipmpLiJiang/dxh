package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.Honor;
import cc.mrbird.febs.system.service.IHonorService;
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
@RequestMapping("/system/honor")
public class HonorController extends BaseController {
    @Autowired
    IHonorService iHonorService;
    @Log("新增荣誉")
    @PostMapping
    public Map<String,Object> addHonor(@Valid Honor honor)  {
        this.iHonorService.createHonor(honor);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改荣誉")
    @PutMapping
    public Map<String,Object> updateHonor(@Valid Honor honor)  {
        this.iHonorService.updateHonor(honor);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除荣誉")
    @DeleteMapping("/delete/{honorIds}")
    public Map<String,Object> deleteHonors(@NotBlank(message = "{required}") @PathVariable String honorIds) throws FebsException {
        String[] ids = honorIds.split(StringPool.COMMA);
        this.iHonorService.deleteHonor(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
