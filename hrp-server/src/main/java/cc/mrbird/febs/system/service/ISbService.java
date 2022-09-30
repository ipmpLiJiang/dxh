package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.SbReport;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface ISbService extends IService<SbReport> {
    List<SbReport> getSummaryReport(Date thismonth) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
    <T> IPage<T> getHistoryList(QueryRequest request,SbReport sbReport) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    <T> List<T> getHistoryList(SbReport sbReport) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    <T>List<T> getDifferentReport(Date thismonth) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;
}
