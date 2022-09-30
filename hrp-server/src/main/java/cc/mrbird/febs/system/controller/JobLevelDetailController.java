package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.system.domain.JobLevelDetail;
import cc.mrbird.febs.system.service.IJobLevelDetailService;
import cc.mrbird.febs.system.service.IJobTypeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MrBird
 */
@RestController
@RequestMapping("/system/job/level/detail")
public class JobLevelDetailController {
    @Autowired
    IJobLevelDetailService iJobLevelDetailService;
    /**
     * 根据岗位类型和职称获取岗位等级
     */
    @GetMapping
    public List<JobLevelDetail> jobLevelDetailList(Integer id) {
        return this.iJobLevelDetailService.findJobLevelDetail(id);
    }
}
