package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.dao.SbSyMapper;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbSy;
import cc.mrbird.febs.system.service.ICodeService;
import cc.mrbird.febs.system.service.ISbSyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class SbSyServiceImpl extends ServiceImpl<SbSyMapper, SbSy> implements ISbSyService {
    @Autowired
    ICodeService codeService;

    public SbReport getReport(Date thismonth){
        return this.baseMapper.getReport(thismonth, DateUtil.getLastMonth(thismonth));
    }

    public List<SbSy> getJnList(Date thismonth,Boolean bjFlag,String type){
        LambdaQueryWrapper<SbSy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SbSy::getShdate, thismonth);
        if(bjFlag!=null){
            if(bjFlag){
                queryWrapper.eq(SbSy::getJftype,2);
            }else{
                queryWrapper.eq(SbSy::getJftype,1);
            }
        }
        List<SbSy> sbSyList=baseMapper.selectList(queryWrapper);
        sbSyList.stream().forEach(sbSy ->{
            sbSy.setType(type);
            sbSy.setBxtype(3);
        });
        return sbSyList;
    }

    public IPage<SbSy> getHistoryList(QueryRequest request, SbReport sbReport){
        Page<SbSy> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.getHistoryList(page,sbReport);
    }

    public List<SbSy> getHistoryList(SbReport sbReport){
        return this.baseMapper.getHistoryList(sbReport);
    }

    public void deleteByDate(Date shDate){
        baseMapper.deleteByDate(shDate);
    }

    public List<SbSy> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId) {
        List<SbSy> list= baseMapper.getXzByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    private void getEmployeeid (List<SbSy> list) {
        List<Code> codeList = codeService.findCodeMaxEndDateLists();
        List<Code> query = new ArrayList<>();
        for (SbSy item : list){
            query = codeList.stream().filter(s->s.getEmployeeid().equals(item.getEmployeeid())).collect(Collectors.toList());
            if(query.size() > 0) {
                item.setEmployeecode(query.get(0).getEmployeecode());
            }
        }
    }

    public List<SbSy> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId){
        List<SbSy> list= baseMapper.getJsByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    public List<SbSy> getBjByShDateLists(Date bshdate){
        List<SbSy> list= baseMapper.getBjByShDateList(bshdate);
        this.getEmployeeid(list);
        return  list;
    }

    public List<SbSy> getSyByIdNumberNotEmolumentLists(Date shdate,Integer jftype) {
        return baseMapper.getSyByIdNumberNotEmolumentList(shdate, jftype);
    }
}

