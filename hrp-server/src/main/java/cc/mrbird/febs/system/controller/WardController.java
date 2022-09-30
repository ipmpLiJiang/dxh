package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.system.domain.Ward;
import cc.mrbird.febs.system.service.IWardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MrBird
 */
@RestController
@RequestMapping("/system/ward")
public class WardController {
    @Autowired
    IWardService iWardService;
    /**
     * 根据科室获取病区
     */
    @GetMapping
    public List<Ward> wardList(int id) {
        return this.iWardService.findWard(id);
    }
}
