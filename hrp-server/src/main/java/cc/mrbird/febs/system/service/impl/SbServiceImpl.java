package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SpringContextUtil;
import cc.mrbird.febs.system.dao.SbMapper;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.service.*;
import cc.mrbird.febs.ylj.entity.YljBRecord;
import cc.mrbird.febs.ylj.service.IYljBRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class SbServiceImpl extends ServiceImpl<SbMapper, SbReport> implements ISbService {

    @Autowired
    ISbDeylService iSbDeylService;

    @Autowired
    ISbGsService iSbGsService;

    @Autowired
    ISbShengyuService iSbShengyuService;

    @Autowired
    ISbSyService iSbSyService;

    @Autowired
    ISbYanglaoService iSbYanglaoService;

    @Autowired
    ISbYlService iSbYlService;

    @Autowired
    IYljBRecordService iYljBRecordService;

    private Date tmonth;
    private Date lmonth;

    public List<SbReport> getSummaryReport(Date thismonth) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<SbReport> sbReportList=new ArrayList<>();
        Date lastMonth=DateUtil.getLastMonth(thismonth);
        tmonth=thismonth;
        lmonth=lastMonth;
        this.getSummaryData("sbYanglaoServiceImpl",sbReportList);
        this.getSummaryData("sbYlServiceImpl",sbReportList);
        this.getSummaryData("sbSyServiceImpl",sbReportList);
        this.getSummaryData("sbGsServiceImpl",sbReportList);
        this.getSummaryData("sbShengyuServiceImpl",sbReportList);
        this.getSummaryData("sbDeylServiceImpl",sbReportList);
        this.getSummaryData("sbBzylServiceImpl",sbReportList);

        List<YljBRecord> xzList = iYljBRecordService.getXzByIdCardAndFkssqLists(tmonth,lmonth,false);
        List<YljBRecord> jsList =iYljBRecordService.getJsByIdCardAndFkssqLists(tmonth,lmonth,false);

        SbReport reportYl = iYljBRecordService.getReportYls(tmonth,lmonth);
        reportYl.setXzcount(xzList.size());
        reportYl.setJscount(jsList.size());
        reportYl.setJnsum(reportYl.getYjsum() + reportYl.getBjsum());
        sbReportList.add(reportYl);

        SbReport reportNj = iYljBRecordService.getReportNjs(tmonth,lmonth);
        reportNj.setXzcount(xzList.size());
        reportNj.setJscount(jsList.size());
        reportNj.setJnsum(reportNj.getYjsum() + reportNj.getBjsum());
        sbReportList.add(reportNj);

        return sbReportList;
    }

    public   <T>List<T>getSummaryData(String beanName, List<SbReport> sbReportList) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object bean= SpringContextUtil.getBean(beanName);
        Method reportMethod=bean.getClass().getDeclaredMethod("getReport", Date.class);
        ReflectionUtils.makeAccessible(reportMethod);
        SbReport report= (SbReport) reportMethod.invoke(bean,tmonth);
        report.setJnsum(report.getYjsum()+report.getBjsum());
        Method listMethodXz=bean.getClass().getDeclaredMethod("getXzByInidnumberAndShDateLists", Date.class,Date.class,Boolean.class);
        Method listMethodJs=bean.getClass().getDeclaredMethod("getJsByInidnumberAndShDateLists", Date.class,Date.class,Boolean.class);
        List<T> tListXz = (List<T>) listMethodXz.invoke(bean, tmonth,lmonth,false);
        report.setXzcount(tListXz.size());
        List<T> tListJs = (List<T>) listMethodJs.invoke(bean, tmonth,lmonth,false);
        report.setJscount(tListJs.size());
        //        Method listMethod=bean.getClass().getDeclaredMethod("getJnList", Date.class,Boolean.class,String.class);
//        List<T> tList = (List<T>) listMethod.invoke(bean, tmonth,null,null);
//        List<T>lastTList= (List<T>) listMethod.invoke(bean,lmonth,null,null);
//        report.setXzcount(tList.stream().filter(t->!lastTList.stream().anyMatch(l->
//        {
//            try {
//                return validate(t,l);
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            return false;
//        })).collect(Collectors.toList()).size());
//
//        report.setJscount(lastTList.stream().filter(l->!tList.stream().anyMatch(t-> {
//            try {
//                return validate(t,l);
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            return false;
//        })).collect(Collectors.toList()).size());
//        report.setXzcount(report.getXzcount()-report.getBjcount());
        sbReportList.add(report);
        return (List<T>) sbReportList;
    }
    public Object getHistoryUtil(SbReport sbReport){
        String beanName="";
        if(sbReport.getBxtype()== FebsConstant.SB_BZYL){
            beanName="sbBzylServiceImpl";
        }else if(sbReport.getBxtype()==FebsConstant.SB_DEYL){
            beanName="sbDeylServiceImpl";
        }else if(sbReport.getBxtype()==FebsConstant.SB_GS){
            beanName="sbGsServiceImpl";
        }else if(sbReport.getBxtype()==FebsConstant.SB_SHENGYU){
            beanName="sbShengyuServiceImpl";
        }else if(sbReport.getBxtype()==FebsConstant.SB_SY){
            beanName="sbSyServiceImpl";
        }else if(sbReport.getBxtype()==FebsConstant.SB_YANGLAO){
            beanName="sbYanglaoServiceImpl";
        }else if(sbReport.getBxtype()==FebsConstant.SB_YL){
            beanName="sbYlServiceImpl";
        }
        return SpringContextUtil.getBean(beanName);
    }

    public <T> IPage<T> getHistoryList(QueryRequest request,SbReport sbReport) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        Object bean=getHistoryUtil(sbReport);
        Method getListMethod=bean.getClass().getDeclaredMethod("getHistoryList", QueryRequest.class,SbReport.class);
        ReflectionUtils.makeAccessible(getListMethod);
        return  (IPage<T>) getListMethod.invoke(bean,request,sbReport);
    }

    public <T> List<T> getHistoryList(SbReport sbReport) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        Object bean=getHistoryUtil(sbReport);
        Method getListMethod=bean.getClass().getDeclaredMethod("getHistoryList",SbReport.class);
        ReflectionUtils.makeAccessible(getListMethod);
        return  (List<T>) getListMethod.invoke(bean,sbReport);
    }

    private  <T>  boolean validate(T l,T item) throws NoSuchFieldException, IllegalAccessException {
        Field f=l.getClass().getDeclaredField("idnumber");
        f.setAccessible(true);
        Object s=f.get(l);
        Field f1=item.getClass().getDeclaredField("idnumber");
        f1.setAccessible(true);
        Object s1=f1.get(item);
        return s.equals(s1);
    }

    public  <T>List<T> getDifferentReport(Date thismonth) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<T> result=new ArrayList<>();
        this.getDifferentDetail("sbYanglaoServiceImpl",result,thismonth);
        this.getDifferentDetail("sbYlServiceImpl",result,thismonth);
        this.getDifferentDetail("sbSyServiceImpl",result,thismonth);
        this.getDifferentDetail("sbGsServiceImpl",result,thismonth);
        this.getDifferentDetail("sbShengyuServiceImpl",result,thismonth);
        this.getDifferentDetail("sbDeylServiceImpl",result,thismonth);
        this.getDifferentDetail("sbBzylServiceImpl",result,thismonth);
        return result;
    }

    private <T>void getDifferentDetail(String beanName, List<T> list, Date thismonth) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Object bean= SpringContextUtil.getBean(beanName);
        Method listMethod=bean.getClass().getDeclaredMethod("getJnList", Date.class,Boolean.class,String.class);
        Date lastMonth=DateUtil.getLastMonth(thismonth);
        List<T> tList = (List<T>) listMethod.invoke(bean, thismonth,false,FebsConstant.SB_TYPE_ZENG);
        List<T>lastTList= (List<T>) listMethod.invoke(bean,lastMonth,false,FebsConstant.SB_TYPE_JIAN);
        List<T>xzList=tList.stream().filter(t->!lastTList.stream().anyMatch(l->
        {
            try {
                return validate(t,l);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        })).collect(Collectors.toList());
        List<T>jsList=lastTList.stream().filter(l->!tList.stream().anyMatch(t-> {
            try {
                return validate(t,l);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        })).collect(Collectors.toList());
        List<T> bjList=(List<T>) listMethod.invoke(bean, thismonth,true,FebsConstant.SB_TYPE_BU);
        list.addAll(xzList);
        list.addAll(jsList);
        list.addAll(bjList);
    }
}
