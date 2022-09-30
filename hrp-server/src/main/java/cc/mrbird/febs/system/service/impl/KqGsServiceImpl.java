package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.KqGs;
import cc.mrbird.febs.system.dao.KqGsMapper;
import cc.mrbird.febs.system.domain.KqJk;
import cc.mrbird.febs.system.service.IKqGsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author MrBird
 */
@Service
public class KqGsServiceImpl extends ServiceImpl<KqGsMapper, KqGs> implements IKqGsService {
    @Override
    public IPage<KqGs> getGsList(QueryRequest request, KqGs kqGs){
        Page<KqJk> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.getGsList(page,kqGs);
    }
    @Override
    public void createGs(KqGs kqGs){
        this.save(kqGs);
    }

    @Override
    public void updateGs(KqGs kqGs){
        updateById(kqGs);
    }

    @Override
    public void deleteGs(String[] gsIds){
        baseMapper.deleteBatchIds(Arrays.asList(gsIds));
    }
}
