package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.domain.SbDeyl;
import cc.mrbird.febs.system.dao.SbDeylMapper;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.service.ICodeService;
import cc.mrbird.febs.system.service.ISbDeylService;
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
public class SbDeylServiceImpl extends ServiceImpl<SbDeylMapper, SbDeyl> implements ISbDeylService {
    @Autowired
    ICodeService codeService;

    public SbReport getReport(Date thismonth){
        return this.baseMapper.getReport(thismonth, DateUtil.getLastMonth(thismonth));
    }

    public List<SbDeyl> getJnList(Date thismonth,Boolean bjFlag,String type){
        LambdaQueryWrapper<SbDeyl> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SbDeyl::getShdate, thismonth);
        if(bjFlag!=null){
            if(bjFlag){
                queryWrapper.eq(SbDeyl::getJftype,2);
            }else{
                queryWrapper.eq(SbDeyl::getJftype,1);
            }
        }
        List<SbDeyl> sbDeylList=baseMapper.selectList(queryWrapper);
        sbDeylList.stream().forEach(sbDeyl ->{
            sbDeyl.setType(type);
            sbDeyl.setBxtype(6);
        });
        return sbDeylList;
    }


    public IPage<SbDeyl> getHistoryList(QueryRequest request, SbReport sbReport){
        Page<SbDeyl> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.getHistoryList(page,sbReport);
    }

    public List<SbDeyl> getHistoryList( SbReport sbReport){
        return this.baseMapper.getHistoryList(sbReport);
    }

    public void deleteByDate(Date shDate){
        baseMapper.deleteByDate(shDate);
    }

    public List<SbDeyl> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId) {
        List<SbDeyl> list= baseMapper.getXzByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    private void getEmployeeid (List<SbDeyl> list) {
        List<Code> codeList = codeService.findCodeMaxEndDateLists();
        List<Code> query = new ArrayList<>();
        for (SbDeyl item : list){
            query = codeList.stream().filter(s->s.getEmployeeid().equals(item.getEmployeeid())).collect(Collectors.toList());
            if(query.size() > 0) {
                item.setEmployeecode(query.get(0).getEmployeecode());
            }
        }
    }

    public List<SbDeyl> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId){
        List<SbDeyl> list= baseMapper.getJsByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    public List<SbDeyl> getBjByShDateLists(Date bshdate){
        List<SbDeyl> list= baseMapper.getBjByShDateList(bshdate);
        this.getEmployeeid(list);
        return  list;
    }

    public List<SbDeyl> getDeylByIdNumberNotEmolumentLists(Date shdate,Integer jftype) {
        return  baseMapper.getDeylByIdNumberNotEmolumentList(shdate, jftype);
    }
}
