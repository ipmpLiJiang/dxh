package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.TitleGet;
import cc.mrbird.febs.system.service.ITitleGetService;
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
@RequestMapping("/system/titleget")
public class TitleGetController extends BaseController {

    @Autowired
    ITitleGetService iTitleGetService;
    @Log("新增取得职称信息")
    @PostMapping
    public Map<String,Object> addTitle(@Valid TitleGet title)  {
        this.iTitleGetService.createTitleGet(title);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改取得职称信息")
    @PutMapping
    public Map<String,Object> updateTitle(@Valid TitleGet title)  {
        this.iTitleGetService.updateTitleGet(title);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除取得职称信息")
    @DeleteMapping("/delete/{titleIds}")
    public Map<String,Object> deleteTitles(@NotBlank(message = "{required}") @PathVariable String titleIds) throws FebsException {
        String[] ids = titleIds.split(StringPool.COMMA);
        this.iTitleGetService.deleteTitleGet(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
