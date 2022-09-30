package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.system.domain.DutyDetail;
import cc.mrbird.febs.system.service.IDutyDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MrBird
 */
@RestController
@RequestMapping("/system/duty/detail")
public class DutyDetailController {
    @Autowired
    IDutyDetailService iDutyDetailService;
    @GetMapping
    public List<DutyDetail> dutyDetailList(Integer id) {
        return this.iDutyDetailService.findDutyDetail(id);
    }
}
