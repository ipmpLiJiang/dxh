package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.GqTitle;
import cc.mrbird.febs.system.service.IGqTitleService;
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
@RequestMapping("/system/gq/title")
public class GqTitleController extends BaseController {
    @Autowired
    IGqTitleService iGqTitleService;
    @Log("新增职称信息")
    @PostMapping
    public Map<String,Object> addTitle(@Valid GqTitle gqTitle)  {
        this.iGqTitleService.createGqTitle(gqTitle);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改职称信息")
    @PutMapping
    public Map<String,Object> updateTitle(@Valid GqTitle gqTitle)  {
        this.iGqTitleService.updateGqTitle(gqTitle);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除职称信息")
    @DeleteMapping("/delete/{titleIds}")
    public Map<String,Object> deleteTitles(@NotBlank(message = "{required}") @PathVariable String titleIds) throws FebsException {
        String[] ids = titleIds.split(StringPool.COMMA);
        this.iGqTitleService.deleteGqTitle(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
