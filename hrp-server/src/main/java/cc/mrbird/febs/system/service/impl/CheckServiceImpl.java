package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.Checkinfo;
import cc.mrbird.febs.system.dao.CheckMapper;
import cc.mrbird.febs.system.service.ICheckService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class CheckServiceImpl extends ServiceImpl<CheckMapper, Checkinfo> implements ICheckService {
    public List<Checkinfo> findCheck(Integer employeeId){
        LambdaQueryWrapper<Checkinfo> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(Checkinfo::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(Checkinfo::getYear).reversed()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
    public void createCheck(Checkinfo check){
        this.save(check);
    }

    public void updateCheck(Checkinfo check){
        this.updateById(check);
    }

    public void deleteChecks(String[] checkIds){
        baseMapper.deleteBatchIds(Arrays.asList(checkIds));
    }

    @Override
    public IPage<Checkinfo> findCheckinfoReport(Page page, Checkinfo check) {
        return this.baseMapper.findCheckinfoReport(page,check);
    }

    @Override
    public List<Checkinfo> findCheckinfoReport2(String deptids,
                                                Date inputdate,
                                                Date startdate,
                                                Date enddate,
                                                Integer employeeid,
                                                String employeename,
                                                Long result) {
        return this.baseMapper.findCheckinfoReport2(deptids,inputdate,startdate,enddate,employeeid,employeename,result);
    }


}
