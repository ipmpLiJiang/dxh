package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.service.ICodeService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
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
@RequestMapping("/system/code")
public class CodeController extends BaseController {
    @Autowired
    ICodeService iCodeService;
    @GetMapping
    public List<Code> CodeList(Integer employeeid) {
        return this.iCodeService.findCode(employeeid);
    }

    @Log("新增员工编号")
    @PostMapping
    public Map<String,Object> addCode(@Valid Code code)  {
        this.iCodeService.createCode(code);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改员工编号")
    @PutMapping
    public Map<String,Object> updateCode(@Valid Code code) {
        this.iCodeService.updateCode(code);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除员工编号")
        @DeleteMapping("/delete/{codeIds}")
    public Map<String,Object> deleteCodes(@NotBlank(message = "{required}") @PathVariable String codeIds) throws FebsException {
        String[] ids = codeIds.split(StringPool.COMMA);
        this.iCodeService.deleteCodes(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
