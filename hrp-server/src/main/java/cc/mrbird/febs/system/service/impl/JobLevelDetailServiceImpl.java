package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.JobLevelDetail;
import cc.mrbird.febs.system.dao.JobLevelDetailMapper;
import cc.mrbird.febs.system.service.IJobLevelDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class JobLevelDetailServiceImpl extends ServiceImpl<JobLevelDetailMapper, JobLevelDetail> implements IJobLevelDetailService {
    public List<JobLevelDetail> findJobLevelDetail(Integer jobTypeId){
        LambdaQueryWrapper<JobLevelDetail> queryWrapper = new LambdaQueryWrapper<>();
        if(jobTypeId!=null){
            queryWrapper.eq(JobLevelDetail::getJobtypeid, jobTypeId);
        }
        //queryWrapper.eq(JobLevelDetail::getTitleid, titleId);
        return baseMapper.selectList(queryWrapper);
    }

    public String getName(List<JobLevelDetail>jobLevelDetailList,String jobLevel){
        String name="";
        for(JobLevelDetail jobLevelDetail:jobLevelDetailList){
            if(jobLevelDetail.getKeyy()==Integer.parseInt(jobLevel)){
                name=jobLevelDetail.getValuee();
                break;
            }
        }
        return name;
    }
}
