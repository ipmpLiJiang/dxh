package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.KqJk;
import cc.mrbird.febs.system.domain.KqSqb;
import cc.mrbird.febs.system.service.IKqJkService;
import cc.mrbird.febs.system.service.IKqSqbService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/kq/sqb")
public class KqSqbController extends BaseController {
    String message;
    @Autowired
    IKqSqbService iKqSqbService;

    @Autowired
    IKqJkService iKqJkService;
    @GetMapping
    public Map<String, Object> getSqbList(QueryRequest request, KqSqb kqSqb)  {
        return getDataTable(iKqSqbService.findKqSqbs(request,kqSqb));
    }

    @GetMapping("/getymxById")
    public List<KqJk> getymxById(String id){
        return iKqJkService.getYmxById(id);
    }

    @PutMapping
    public Map<String,Object> updateSqb(KqSqb kqSqb){
        iKqSqbService.updateSqb(kqSqb);
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS,null);
    }

    @PutMapping("/shSqb")
    public Map<String,Object> shSqb(@RequestBody KqSqb kqSqb){
        iKqSqbService.shSqb(kqSqb);
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS,null);
    }

    /**
     * 导出 Excel
     */
    @PostMapping("export")
    public void export(HttpServletResponse response, KqSqb kqSqb) throws FebsException {
        try {
            List<KqSqb> list = this.iKqSqbService.findKqSqbs(kqSqb);
            ExcelKit.$Export(KqSqb.class, response).downXlsx(list, true);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }
}
