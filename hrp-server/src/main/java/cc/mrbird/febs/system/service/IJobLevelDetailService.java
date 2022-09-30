package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.JobLevelDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IJobLevelDetailService extends IService<JobLevelDetail> {
    List<JobLevelDetail> findJobLevelDetail(Integer jobTypeId);
    String getName(List<JobLevelDetail> jobLevelDetailList,String jobLevel);
}
