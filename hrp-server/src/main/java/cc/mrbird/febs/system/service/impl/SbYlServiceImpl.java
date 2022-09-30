package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbYl;
import cc.mrbird.febs.system.dao.SbYlMapper;
import cc.mrbird.febs.system.service.ICodeService;
import cc.mrbird.febs.system.service.ISbYlService;
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
public class SbYlServiceImpl extends ServiceImpl<SbYlMapper, SbYl> implements ISbYlService {
    @Autowired
    ICodeService codeService;

    public SbReport getReport(Date thismonth){
        return this.baseMapper.getReport(thismonth, DateUtil.getLastMonth(thismonth));
    }

    public List<SbYl> getJnList(Date thismonth,Boolean bjFlag,String type){
        LambdaQueryWrapper<SbYl> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SbYl::getShdate, thismonth);
        if(bjFlag!=null){
            if(bjFlag){
                queryWrapper.eq(SbYl::getJftype,2);
            }else{
                queryWrapper.eq(SbYl::getJftype,1);
            }
        }
        List<SbYl> sbYlList=baseMapper.selectList(queryWrapper);
        sbYlList.stream().forEach(sbYl ->{
            sbYl.setType(type);
            sbYl.setBxtype(2);
        });
        return sbYlList;
    }

    public IPage<SbYl> getHistoryList(QueryRequest request, SbReport sbReport){
        Page<SbYl> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.getHistoryList(page,sbReport);
    }

    public List<SbYl> getHistoryList(SbReport sbReport){
        return this.baseMapper.getHistoryList(sbReport);
    }

    public void deleteByDate(Date shDate){
        baseMapper.deleteByDate(shDate);
    }

    public List<SbYl> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId) {
        List<SbYl> list= baseMapper.getXzByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    private void getEmployeeid (List<SbYl> list) {
        List<Code> codeList = codeService.findCodeMaxEndDateLists();
        List<Code> query = new ArrayList<>();
        for (SbYl item : list){
            query = codeList.stream().filter(s->s.getEmployeeid().equals(item.getEmployeeid())).collect(Collectors.toList());
            if(query.size() > 0) {
                item.setEmployeecode(query.get(0).getEmployeecode());
            }
        }
    }

    public List<SbYl> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId){
        List<SbYl> list= baseMapper.getJsByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    public List<SbYl> getBjByShDateLists(Date bshdate){
        List<SbYl> list= baseMapper.getBjByShDateList(bshdate);
        this.getEmployeeid(list);
        return  list;
    }

    public List<SbYl> getYlByIdNumberNotEmolumentLists(Date shdate,Integer jftype) {
        return baseMapper.getYlByIdNumberNotEmolumentList(shdate, jftype);
    }
}
