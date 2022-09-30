package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.Dict;
import cc.mrbird.febs.system.domain.Employee;
import cc.mrbird.febs.system.domain.Tsxmmx;
import cc.mrbird.febs.system.dao.TsxmmxMapper;
import cc.mrbird.febs.system.service.DictService;
import cc.mrbird.febs.system.service.IEmployeeService;
import cc.mrbird.febs.system.service.ITsxmmxService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author MrBird
 */
@Service
public class TsxmmxServiceImpl extends ServiceImpl<TsxmmxMapper, Tsxmmx> implements ITsxmmxService {
    @Autowired
    DictService dictService;

    @Autowired
    IEmployeeService iEmployeeService;
    public IPage<Tsxmmx> findGzxms(Tsxmmx tsxmmx, QueryRequest request){

        Page<Tsxmmx> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.findGzxms(page, tsxmmx);
    }
    public void createGzxm(Tsxmmx tsxmmx){
        this.save(tsxmmx);
    }

    public void updateGzxm(Tsxmmx tsxmmx){
        this.updateById(tsxmmx);
    }

    public void deleteGzxm(String[] gzxmIds){
        baseMapper.deleteBatchIds(Arrays.asList(gzxmIds));
    }

    public void batchCreateGzxm(List<Tsxmmx> tsxmmxList){
        List<Dict> dictList=dictService.getFieldDict("tsxmmx","gzmc");
        for(Tsxmmx t:tsxmmxList){
            Employee e=findEmployeeByCode(t.getEmployeecode());
            t.setEmployeeid(e.getEmployeeid());
            Dict dict=dictList.stream().filter(d->d.getValuee().equals(t.getGzxm())).findFirst().get();
            t.setGzxm(dict.getKeyy());
        }
        this.saveBatch(tsxmmxList);
    }

    public Employee findEmployeeByCode(String code){
        return baseMapper.findEmployeeByCode(code);
    }

    public Float getTotalData(Tsxmmx tsxmmx){
        return this.baseMapper.getTotalData(tsxmmx);
    }
}
