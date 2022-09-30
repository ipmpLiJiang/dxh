package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.dao.PeriodMapper;
import cc.mrbird.febs.system.domain.Emolument;
import cc.mrbird.febs.system.domain.Period;
import cc.mrbird.febs.system.service.IEmolumentService;
import cc.mrbird.febs.system.service.IPeriodService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author MrBird
 */
@Service
public class PeriodServiceImpl extends ServiceImpl<PeriodMapper, Period> implements IPeriodService {
    @Autowired
    IEmolumentService iEmolumentService;
    public Period getLatestPeriod(){
        Period period= baseMapper.getLatestPeriod();
        return period;
    }

    @Transactional
    public Period openPeriod(QueryRequest request, Period period){
        Period p= this.baseMapper.selectById(period.getId());
        if(p!=null&&p.getStatus()!=null&&p.getStatus().equals("1")){
            return  p;
        }

        period.setStatus(1);
        /*if(period.getIsfirststatus()==0){
            period.setIsfirststatus(1);
            iEmolumentService.insertEmolumentList(request,period);
        }*/
        this.updatePeriod(period);
        iEmolumentService.insertEmolumentList(request,period);
        return period;
    }

    public void updatePeriod(Period period){
        updateById(period);
    }

    public void createPeriod(Period period){
        this.save(period);
    }

}
