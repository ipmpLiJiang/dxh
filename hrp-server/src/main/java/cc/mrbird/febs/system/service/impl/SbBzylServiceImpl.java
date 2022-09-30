package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.dao.SbBzylMapper;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.domain.SbBzyl;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.service.ICodeService;
import cc.mrbird.febs.system.service.ISbBzylService;
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
public class SbBzylServiceImpl extends ServiceImpl<SbBzylMapper, SbBzyl> implements ISbBzylService {
    @Autowired
    ICodeService codeService;

    public SbReport getReport(Date thismonth){
        return this.baseMapper.getReport(thismonth, DateUtil.getLastMonth(thismonth));
    }

    public List<SbBzyl> getJnList(Date thismonth,Boolean bjFlag,String type){
        LambdaQueryWrapper<SbBzyl> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SbBzyl::getShdate, thismonth);
        if(bjFlag!=null){
            if(bjFlag){
                queryWrapper.eq(SbBzyl::getJftype,2);
            }else{
                queryWrapper.eq(SbBzyl::getJftype,1);
            }
        }

        List<SbBzyl> sbDeylList=baseMapper.selectList(queryWrapper);
        sbDeylList.stream().forEach(sbDeyl ->{
            sbDeyl.setType(type);
            sbDeyl.setBxtype(7);
        });
        return sbDeylList;
    }
    public IPage<SbBzyl> getHistoryList(QueryRequest request,SbReport sbReport){
        Page<SbBzyl> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.getHistoryList(page,sbReport);
    }

    public List<SbBzyl> getHistoryList(SbReport sbReport){
        return this.baseMapper.getHistoryList(sbReport);
    }

    public void deleteByDate(Date shDate){
        baseMapper.deleteByDate(shDate);
    }

    public List<SbBzyl> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId) {
        List<SbBzyl> list= baseMapper.getXzByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    private void getEmployeeid (List<SbBzyl> list) {
        List<Code> codeList = codeService.findCodeMaxEndDateLists();
        List<Code> query = new ArrayList<>();
        for (SbBzyl item : list){
            query = codeList.stream().filter(s->s.getEmployeeid().equals(item.getEmployeeid())).collect(Collectors.toList());
            if(query.size() > 0) {
                item.setEmployeecode(query.get(0).getEmployeecode());
            }
        }
    }

    public List<SbBzyl> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId){
        List<SbBzyl> list= baseMapper.getJsByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    public List<SbBzyl> getBjByShDateLists(Date bshdate){
        List<SbBzyl> list= baseMapper.getBjByShDateList(bshdate);
        this.getEmployeeid(list);
        return  list;
    }
}
