package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.system.domain.SbShengyu;
import cc.mrbird.febs.system.service.ISbShengyuService;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
@RestController
@RequestMapping("/system/sb-shengyu")
public class SbShengyuController {
    String message = "";
    @Autowired
    ISbShengyuService sbShengyuService;

    @GetMapping("/getXz")
    public FebsResponse getXz(String dtr) {
        ModelMap map = new ModelMap();
        int success = 0;
        List<SbShengyu> list = new ArrayList<>();
        try {
            Date bshdate = DateUtil.parse(dtr + "-01");
            Date ushdate = DateUtil.offset(bshdate, DateField.MONTH, -1);
            list = this.sbShengyuService.getXzByInidnumberAndShDateLists(bshdate, ushdate,true);
            success = 1;
        } catch (Exception e) {
            message = "查询失败";
        }
        map.put("message", message);
        map.put("success", success);
        map.put("data", list);
        return new FebsResponse().data(map);
    }

    @GetMapping("/getJs")
    public FebsResponse getJs(String dtr) {
        ModelMap map = new ModelMap();
        int success = 0;
        Date bshdate = DateUtil.parse(dtr + "-01");
        Date ushdate = DateUtil.offset(bshdate, DateField.MONTH, -1);
        List<SbShengyu> list = new ArrayList<>();
        try {
            list = this.sbShengyuService.getJsByInidnumberAndShDateLists(bshdate, ushdate,true);
            success = 1;
        } catch (Exception e) {
            message = "查询失败";
        }
        map.put("message", message);
        map.put("success", success);
        map.put("data", list);
        return new FebsResponse().data(map);
    }

    @GetMapping("/getBj")
    public FebsResponse getBj(String dtr) {
        ModelMap map = new ModelMap();
        int success = 0;
        List<SbShengyu> list = new ArrayList<>();
        try {
            Date bshdate = DateUtil.parse(dtr + "-01");
            list = this.sbShengyuService.getBjByShDateLists(bshdate);
            success = 1;
        } catch (Exception e) {
            message = "查询失败";
        }
        map.put("message", message);
        map.put("success", success);
        map.put("data", list);
        return new FebsResponse().data(map);
    }
}
