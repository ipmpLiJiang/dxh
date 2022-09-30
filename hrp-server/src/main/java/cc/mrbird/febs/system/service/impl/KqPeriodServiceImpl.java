package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.KqJk;
import cc.mrbird.febs.system.domain.KqPeriod;
import cc.mrbird.febs.system.dao.KqPeriodMapper;
import cc.mrbird.febs.system.service.IKqJkService;
import cc.mrbird.febs.system.service.IKqPeriodService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MrBird
 */
@Service
public class KqPeriodServiceImpl extends ServiceImpl<KqPeriodMapper, KqPeriod> implements IKqPeriodService {
    @Autowired
    IKqJkService iKqJkService;
    public KqPeriod getLatestPeriod(){
        KqPeriod period=baseMapper.getLatestPeriod();
        return period;
    }

    @Transactional
    public KqPeriod openPeriod(QueryRequest request, KqPeriod period){
        period.setState(1);
        this.updatePeriod(period);
        return period;
    }

    public void updatePeriod(KqPeriod period){
        updateById(period);
    }

    public void createPeriod(KqPeriod period){
        this.save(period);
    }

}
