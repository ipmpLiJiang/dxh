package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.KqJk;
import cc.mrbird.febs.system.domain.KqRmxb;
import cc.mrbird.febs.system.domain.KqSqb;
import cc.mrbird.febs.system.dao.KqSqbMapper;
import cc.mrbird.febs.system.service.IKqJkService;
import cc.mrbird.febs.system.service.IKqRmxbService;
import cc.mrbird.febs.system.service.IKqSqbService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class KqSqbServiceImpl extends ServiceImpl<KqSqbMapper, KqSqb> implements IKqSqbService {
    @Autowired
    IKqRmxbService iKqRmxbService;

    @Autowired
    IKqJkService iKqJkService;
    @Override
    public void createSqb(KqSqb kqSqb){
        this.save(kqSqb);
    }

    @Override
    public IPage<KqSqb> findKqSqbs(QueryRequest request,KqSqb kqSqb){
        LambdaQueryWrapper<KqSqb> queryWrapper = new LambdaQueryWrapper<>();
        if(kqSqb.getShstatus()!=null){
            queryWrapper.eq(KqSqb::getShstatus,kqSqb.getShstatus());
        }
        if(kqSqb.getPbdate()!=null){
            queryWrapper.eq(KqSqb::getPbdate,kqSqb.getPbdate());
        }
        if(kqSqb.getPbdept()!=null){
            queryWrapper.eq(KqSqb::getPbdept,kqSqb.getPbdept());
        }
        Page<KqSqb> page = new Page<>();
        SortUtil.handlePageSort(request, page, true);
        return baseMapper.selectPage(page,queryWrapper);
    }

    public List<KqSqb> findKqSqbs(KqSqb kqSqb){
        LambdaQueryWrapper<KqSqb> queryWrapper = new LambdaQueryWrapper<>();
        if(kqSqb.getShstatus()!=null){
            queryWrapper.eq(KqSqb::getShstatus,kqSqb.getShstatus());
        }
        if(kqSqb.getPbdate()!=null){
            queryWrapper.eq(KqSqb::getPbdate,kqSqb.getPbdate());
        }
        if(kqSqb.getPbdept()!=null){
            queryWrapper.eq(KqSqb::getPbdept,kqSqb.getPbdept());
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void updateSqb(KqSqb kqSqb){
        this.updateById(kqSqb);
    }

    public KqSqb findByPbDeptAndPbDate(KqRmxb kqRmxb){
        LambdaQueryWrapper<KqSqb> queryWrapper = new LambdaQueryWrapper<>();
        if(kqRmxb.getStartdate()!=null){
            queryWrapper.eq(KqSqb::getPbdate,kqRmxb.getStartdate());
            queryWrapper.le(KqSqb::getPbdate, DateUtil.getLastDayOfMonth(kqRmxb.getStartdate()));
        }
        if(kqRmxb.getPbdept()!=null){
            queryWrapper.eq(KqSqb::getPbdept,kqRmxb.getPbdept());
        }
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional
    public void shSqb(KqSqb kqSqb){
        kqSqb.setShstatus(kqSqb.getShstatus());
        updateById(kqSqb);
        KqRmxb kqRmxb=new KqRmxb();
        kqRmxb.setSqbid(kqSqb.getId());
        kqRmxb.setShstatus(kqSqb.getShstatus());
        iKqRmxbService.updateBySqbId(kqRmxb);
    }

}
