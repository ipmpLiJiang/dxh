package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.JobTypeDetail;
import cc.mrbird.febs.system.dao.JobTypeDetailMapper;
import cc.mrbird.febs.system.service.IJobTypeDetailService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class JobTypeDetailServiceImpl extends ServiceImpl<JobTypeDetailMapper, JobTypeDetail> implements IJobTypeDetailService {
    public List<JobTypeDetail> findJobTypeDetail(){
        LambdaQueryWrapper<JobTypeDetail> queryWrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    public void createJobTypeDetailn(JobTypeDetail jobTypeDetail){
        this.save(jobTypeDetail);
    }
    public List<JobTypeDetail> getOAJobTypeDetail(){
        return this.baseMapper.getOAJobTypeDetail();
    }

    public void deleteAll(){
        this.baseMapper.deleteAll();
    }
}
