package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbWts;
import cc.mrbird.febs.system.service.ISbWtsService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/sbwts")
public class SbWtsController extends BaseController {
    String message = "";
    @Autowired
    ISbWtsService iSbWtsService;

    @GetMapping("getList")
    public FebsResponse getLists(QueryRequest request, SbWts sbWts) {
        List<SbWts> list = iSbWtsService.getSbWtsList(sbWts);
        return  new FebsResponse().data(list);
    }

    @PostMapping("export")
    public void export(HttpServletResponse response, SbWts sbWts) throws FebsException {
        try {
            List<SbWts> list = iSbWtsService.getSbWtsList(sbWts);
            ExcelKit.$Export(SbWts.class, response).downXlsx(list, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

}
