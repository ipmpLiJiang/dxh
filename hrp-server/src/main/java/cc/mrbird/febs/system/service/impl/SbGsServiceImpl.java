package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.domain.SbGs;
import cc.mrbird.febs.system.dao.SbGsMapper;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.service.ICodeService;
import cc.mrbird.febs.system.service.ISbGsService;
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
public class SbGsServiceImpl extends ServiceImpl<SbGsMapper, SbGs> implements ISbGsService {
    @Autowired
    ICodeService codeService;

    public SbReport getReport(Date thismonth){
        return this.baseMapper.getReport(thismonth, DateUtil.getLastMonth(thismonth));
    }

    public List<SbGs> getJnList(Date thismonth,Boolean bjFlag,String type){
        LambdaQueryWrapper<SbGs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SbGs::getShdate, thismonth);
        if(bjFlag!=null){
            if(bjFlag){
                queryWrapper.eq(SbGs::getJftype,2);
            }else{
                queryWrapper.eq(SbGs::getJftype,1);
            }
        }
        List<SbGs> sbGsList=baseMapper.selectList(queryWrapper);
        sbGsList.stream().forEach(sbGs ->{
            sbGs.setType(type);
            sbGs.setBxtype(4);
        });
        return sbGsList;
    }
    public IPage<SbGs> getHistoryList(QueryRequest request,SbReport sbReport){
        Page<SbGs> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.getHistoryList(page,sbReport);
    }

    public List<SbGs> getHistoryList(SbReport sbReport){
        return this.baseMapper.getHistoryList(sbReport);
    }

    public void deleteByDate(Date shDate){
        baseMapper.deleteByDate(shDate);
    }

    public List<SbGs> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId) {
        List<SbGs> list= baseMapper.getXzByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    private void getEmployeeid (List<SbGs> list) {
        List<Code> codeList = codeService.findCodeMaxEndDateLists();
        List<Code> query = new ArrayList<>();
        for (SbGs item : list){
            query = codeList.stream().filter(s->s.getEmployeeid().equals(item.getEmployeeid())).collect(Collectors.toList());
            if(query.size() > 0) {
                item.setEmployeecode(query.get(0).getEmployeecode());
            }
        }
    }

    public List<SbGs> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId){
        List<SbGs> list= baseMapper.getJsByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    public List<SbGs> getBjByShDateLists(Date bshdate){
        List<SbGs> list= baseMapper.getBjByShDateList(bshdate);
        this.getEmployeeid(list);
        return  list;
    }
}
