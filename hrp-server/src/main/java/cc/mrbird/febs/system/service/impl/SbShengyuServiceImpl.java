package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbShengyu;
import cc.mrbird.febs.system.dao.SbShengyuMapper;
import cc.mrbird.febs.system.service.ICodeService;
import cc.mrbird.febs.system.service.ISbShengyuService;
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
public class SbShengyuServiceImpl extends ServiceImpl<SbShengyuMapper, SbShengyu> implements ISbShengyuService {
    @Autowired
    ICodeService codeService;

    public SbReport getReport(Date thismonth){
        return this.baseMapper.getReport(thismonth, DateUtil.getLastMonth(thismonth));
    }

    public List<SbShengyu> getJnList(Date thismonth,Boolean bjFlag,String type){
        LambdaQueryWrapper<SbShengyu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SbShengyu::getShdate, thismonth);
        if(bjFlag!=null){
            if(bjFlag){
                queryWrapper.eq(SbShengyu::getJftype,2);
            }else{
                queryWrapper.eq(SbShengyu::getJftype,1);
            }
        }
        List<SbShengyu> shengyuList=baseMapper.selectList(queryWrapper);
        shengyuList.stream().forEach(sbShengyu ->{
            sbShengyu.setType(type);
            sbShengyu.setBxtype(5);
        });
        return shengyuList;
    }

    public IPage<SbShengyu> getHistoryList(QueryRequest request, SbReport sbReport){
        Page<SbShengyu> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.getHistoryList(page,sbReport);
    }

    public List<SbShengyu> getHistoryList( SbReport sbReport){
        return this.baseMapper.getHistoryList(sbReport);
    }

    public void deleteByDate(Date shDate){
        baseMapper.deleteByDate(shDate);
    }

    public List<SbShengyu> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId) {
        List<SbShengyu> list= baseMapper.getXzByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    private void getEmployeeid (List<SbShengyu> list) {
        List<Code> codeList = codeService.findCodeMaxEndDateLists();
        List<Code> query = new ArrayList<>();
        for (SbShengyu item : list){
            query = codeList.stream().filter(s->s.getEmployeeid().equals(item.getEmployeeid())).collect(Collectors.toList());
            if(query.size() > 0) {
                item.setEmployeecode(query.get(0).getEmployeecode());
            }
        }
    }

    public List<SbShengyu> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId){
        List<SbShengyu> list= baseMapper.getJsByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    public List<SbShengyu> getBjByShDateLists(Date bshdate){
        List<SbShengyu> list= baseMapper.getBjByShDateList(bshdate);
        this.getEmployeeid(list);
        return  list;
    }
}
