package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.AdministrativePost;
import cc.mrbird.febs.system.service.IAdministrativePostService;
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
@RequestMapping("/system/administrative/post")
public class AdministrativePostController extends BaseController {
    @Autowired
    IAdministrativePostService iAdministrativePostService;

    @Log("新增行政职务")
    @PostMapping
    public Map<String,Object> addAdministrativePost(@Valid AdministrativePost administrativePost)  {
        this.iAdministrativePostService.createAdministrativePost(administrativePost);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改行政职务")
    @PutMapping
    public Map<String,Object> updateAdministrativePost(@Valid AdministrativePost administrativePost)  {
        this.iAdministrativePostService.updateAdministrativePost(administrativePost);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除行政职务")
    @DeleteMapping("/delete/{administrativePostIds}")
    public Map<String,Object> deleteAdministrativePosts(@NotBlank(message = "{required}") @PathVariable String administrativePostIds) throws FebsException {
        String[] ids = administrativePostIds.split(StringPool.COMMA);
        this.iAdministrativePostService.deleteAdministrativePosts(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
