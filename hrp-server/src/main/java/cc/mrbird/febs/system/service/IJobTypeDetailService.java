package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.JobTypeDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IJobTypeDetailService extends IService<JobTypeDetail> {
    List<JobTypeDetail> findJobTypeDetail();
    void createJobTypeDetailn(JobTypeDetail jobTypeDetail);
    List<JobTypeDetail> getOAJobTypeDetail();
    void deleteAll();
}
