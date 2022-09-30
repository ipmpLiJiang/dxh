package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.domain.SbDeyl;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbYanglao;
import cc.mrbird.febs.system.dao.SbYanglaoMapper;
import cc.mrbird.febs.system.service.ICodeService;
import cc.mrbird.febs.system.service.ISbYanglaoService;
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
public class SbYanglaoServiceImpl extends ServiceImpl<SbYanglaoMapper, SbYanglao> implements ISbYanglaoService {

    @Autowired
    ICodeService codeService;

    public SbReport getReport(Date thismonth){
        return this.baseMapper.getReport(thismonth, DateUtil.getLastMonth(thismonth));
    }

    public List<SbYanglao> getJnList(Date thismonth,Boolean bjFlag,String type){
        LambdaQueryWrapper<SbYanglao> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SbYanglao::getShdate, thismonth);
        if(bjFlag!=null){
            if(bjFlag){
                queryWrapper.eq(SbYanglao::getJftype,2);
            }else{
                queryWrapper.eq(SbYanglao::getJftype,1);
            }
        }
        List<SbYanglao> sbYanglaoList=baseMapper.selectList(queryWrapper);
        sbYanglaoList.stream().forEach(sbYanglao ->{
            sbYanglao.setType(type);
            sbYanglao.setBxtype(1);
        });
        return sbYanglaoList;
    }

    public IPage<SbYanglao> getHistoryList(QueryRequest request, SbReport sbReport){
        Page<SbYanglao> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.getHistoryList(page,sbReport);
    }

    public List<SbYanglao> getHistoryList(SbReport sbReport){
        return this.baseMapper.getHistoryList(sbReport);
    }

    public void deleteByDate(Date shDate){
        baseMapper.deleteByDate(shDate);
    }

    public List<SbYanglao> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId) {
        List<SbYanglao> list= baseMapper.getXzByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    private void getEmployeeid (List<SbYanglao> list) {
        List<Code> codeList = codeService.findCodeMaxEndDateLists();
        List<Code> query = new ArrayList<>();
        for (SbYanglao item : list){
            query = codeList.stream().filter(s->s.getEmployeeid().equals(item.getEmployeeid())).collect(Collectors.toList());
            if(query.size() > 0) {
                item.setEmployeecode(query.get(0).getEmployeecode());
            }
        }
    }

    public List<SbYanglao> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId){
        List<SbYanglao> list= baseMapper.getJsByInidnumberAndShDateList(bshdate,ushdate);
        if(isId!=null && isId) {
            this.getEmployeeid(list);
        }
        return  list;
    }

    public List<SbYanglao> getBjByShDateLists(Date bshdate){
        List<SbYanglao> list= baseMapper.getBjByShDateList(bshdate);
        this.getEmployeeid(list);
        return  list;
    }

    public List<SbYanglao> getYanglaoByIdNumberNotEmolumentLists(Date shdate,Integer jftype) {
        return baseMapper.getYanglaoByIdNumberNotEmolumentList(shdate,jftype);
    }
}
