package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.dao.KqJkMapper;
import cc.mrbird.febs.system.service.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class KqJkServiceImpl extends ServiceImpl<KqJkMapper, KqJk> implements IKqJkService {
    @Autowired
    IKqHzService iKqHzService;

    @Autowired
    IKqRmxbService iKqRmxbService;

    @Autowired
    IKqSqbService iKqSqbService;

    @Autowired
    ICodeService codeService;

    @Autowired
    IEmployeeService iEmployeeService;
    public IPage<KqJk> findKqjk(QueryRequest request, KqJk kqJk) {
        Page<KqJk> page = new Page<>();
        kqJk.setEnddate(DateUtil.getLastDayOfMonth(kqJk.getStartdate()));
        SortUtil.handlePageSort(request, page, false);
        IPage<KqJk> p = this.baseMapper.getKqJkList(page,kqJk);
        List<Code> codeList = codeService.findCodeMaxEndDateLists();
        List<Code> query = new ArrayList<>();

        for (KqJk item : p.getRecords()){
            Integer eid = StringUtils.isNotBlank(item.getEmployeeid()) ? Integer.parseInt(item.getEmployeeid()) : -1;
            query = codeList.stream().filter(s->s.getEmployeeid().equals(eid)).collect(Collectors.toList());
            if(query.size() > 0) {
                item.setEmployeecode(query.get(0).getEmployeecode());
            }
        }
        return p;
    }

    public List<KqJk> findKqjk(KqJk kqJk){
        kqJk.setEnddate(DateUtil.getLastDayOfMonth(kqJk.getStartdate()));
        return this.baseMapper.getKqJkList(kqJk);
    }

    public KqJk findKqjk(Date startDate,String idnumber){
        LambdaQueryWrapper<KqJk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KqJk::getIdnumber,idnumber);
        queryWrapper.eq(KqJk::getStartdate,startDate);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<KqJk> getYmxById(String id){
        LambdaQueryWrapper<KqJk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KqJk::getSqbid,id);
        return baseMapper.selectList(queryWrapper);
    }

    public List<KqJk>findByDeptAndDate(String pbDept,Date startDate){
        LambdaQueryWrapper<KqJk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KqJk::getTjdept,pbDept);
        queryWrapper.ge(KqJk::getStartdate,startDate);
        queryWrapper.le(KqJk::getEnddate,DateUtil.getLastDayOfMonth(startDate));
        return baseMapper.selectList(queryWrapper);
    }

    public IPage<KqJk> findDuplicateList(QueryRequest request, KqJk kqJk) {
        Page<KqJk> page = new Page<>();
        kqJk.setEnddate(DateUtil.getLastDayOfMonth(kqJk.getStartdate()));
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.findDuplicateList(page,kqJk);
    }
    public void createKqjk(KqJk kqJk){
        this.save(kqJk);
    }

    @Transactional
    @Override
    public void batchCreateKqjk(KqRmxb kqRmxb){
        KqSqb kqSqb=iKqSqbService.findByPbDeptAndPbDate(kqRmxb);
        kqRmxb.setEnddate(DateUtil.getLastDayOfMonth(kqRmxb.getStartdate()));
        List<Employee> employeeList=iEmployeeService.getEmployeeByKqDept(kqRmxb.getPbdept(),kqRmxb.getStartdate(),kqRmxb.getEnddate(),kqRmxb.getRszfws());
        List<KqJk> kqJkList=iKqRmxbService.createKqJkByMonth(kqRmxb);
        if(kqSqb==null){
            kqSqb=new KqSqb();
            kqSqb.setPbdate(kqRmxb.getStartdate());
            kqSqb.setTjdate(new Date());
            kqSqb.setPbdept(kqRmxb.getPbdept());
            kqSqb.setShstatus(1);
            kqSqb.setTjcount(kqJkList.size());
            kqSqb.setKscount(employeeList.size());
            iKqSqbService.createSqb(kqSqb);
        }else{
            kqSqb.setShstatus(1);
            iKqSqbService.updateSqb(kqSqb);
        }
        kqRmxb.setShstatus(1);
        kqRmxb.setSqbid(kqSqb.getId());
        deleteBySqbId(kqSqb.getId());
        for(KqJk k:kqJkList){
            k.setSqbid(kqSqb.getId());
        }
        iKqRmxbService.batchUpdateSqbInfo(kqRmxb);
        this.saveBatch(kqJkList);
    }
    public void updateKqjk(KqJk kqJk){
        updateById(kqJk);
    }

    public void createHzList(QueryRequest request, KqPeriod period) throws IllegalAccessException {
        KqJk kqJk=new KqJk();
        kqJk.setStartdate(period.getPeriod());
        iKqHzService.jkToHzList(this.baseMapper.getKqJkList(kqJk),period.getPeriod());
    }

    public List<KqJk> getOAKqJk(){
        return baseMapper.getOAKqJk();
    }

    public void updateOAKqJk(Date updatetime){
        this.baseMapper.updateOAKqJk(updatetime);
    }

    public void deleteKqJk(String[] kqjkids){
        baseMapper.deleteBatchIds(Arrays.asList(kqjkids));
    }

    public void deleteBySqbId(String sqbId){
        LambdaQueryWrapper<KqJk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KqJk::getSqbid,sqbId);
        baseMapper.delete(queryWrapper);
    }
}
