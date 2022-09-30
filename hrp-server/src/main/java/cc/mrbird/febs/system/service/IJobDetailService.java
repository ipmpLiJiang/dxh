package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.JobDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IJobDetailService extends IService<JobDetail> {
    List<JobDetail> findJobDetail();
    void createJobDetail(JobDetail jobDetail);
    List<JobDetail> getOAJobDetail();
    void deleteAll();
}
