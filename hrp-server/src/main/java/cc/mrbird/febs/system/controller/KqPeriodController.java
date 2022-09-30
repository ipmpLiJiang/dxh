package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.KqPeriod;
import cc.mrbird.febs.system.service.IKqPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author MrBird
 */
@RestController
@RequestMapping("/system/kq/period")
public class KqPeriodController extends BaseController {
    @Autowired
    IKqPeriodService iKqPeriodService;
    @GetMapping
    public KqPeriod getLatestPeriod() {
        return this.iKqPeriodService.getLatestPeriod();
    }

    @Log("开启期间")
    @PostMapping
    public KqPeriod openPeriod(QueryRequest request, KqPeriod period) throws FebsException {
        return iKqPeriodService.openPeriod(request,period);
    }
}
