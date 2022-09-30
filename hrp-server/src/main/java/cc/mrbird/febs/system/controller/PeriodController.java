package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.Period;
import cc.mrbird.febs.system.service.IEmolumentService;
import cc.mrbird.febs.system.service.IPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author MrBird
 */
@RestController
@RequestMapping("/system/period")
public class PeriodController extends BaseController {
    @Autowired
    IPeriodService iPeriodService;
    @Autowired
    IEmolumentService iEmolumentService;
    @GetMapping
    public Period getLatestPeriod() {
        return this.iPeriodService.getLatestPeriod();
    }

    @Log("开启期间")
    @PostMapping
    public Period openPeriod(QueryRequest request,Period period) throws FebsException {
        return this.iPeriodService.openPeriod(request,period);
    }
}
