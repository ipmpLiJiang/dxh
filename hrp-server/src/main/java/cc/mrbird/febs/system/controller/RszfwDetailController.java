package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.system.domain.RszfwDetail;
import cc.mrbird.febs.system.service.IRszfwDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MrBird
 */
@RestController
@RequestMapping("/system/rszfw/detail")
public class RszfwDetailController {
    @Autowired
    IRszfwDetailService iRszfwDetailService;
    /**
     * 根据人事范围获取人事子范围
     */
    @GetMapping
    public List<RszfwDetail> rszfwDetailList(Integer id) {
        return this.iRszfwDetailService.findRszfwDetail(id);
    }

    @GetMapping("/findAllRszfwDetail")
    public List<RszfwDetail> allRszfwDetail(){
        return iRszfwDetailService.findAllRszfwDetail();
    }
}
