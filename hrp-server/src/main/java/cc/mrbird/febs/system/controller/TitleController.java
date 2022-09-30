package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.Title;
import cc.mrbird.febs.system.service.ITitleService;
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
@RequestMapping("/system/title")
public class TitleController extends BaseController {
    @Autowired
    ITitleService iTitleService;
    @Log("新增职称信息")
    @PostMapping
    public Map<String,Object> addTitle(@Valid Title title)  {
        this.iTitleService.createTitle(title);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改职称信息")
    @PutMapping
    public Map<String,Object> updateTitle(@Valid Title title)  {
        this.iTitleService.updateTitle(title);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除职称信息")
    @DeleteMapping("/delete/{titleIds}")
    public Map<String,Object> deleteTitles(@NotBlank(message = "{required}") @PathVariable String titleIds) throws FebsException {
        String[] ids = titleIds.split(StringPool.COMMA);
        this.iTitleService.deleteTitle(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
