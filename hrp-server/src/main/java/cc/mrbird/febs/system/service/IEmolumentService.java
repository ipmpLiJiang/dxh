package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.CwKj;
import cc.mrbird.febs.system.domain.Emolument;
import cc.mrbird.febs.system.domain.EmolumentCy;
import cc.mrbird.febs.system.domain.Period;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface IEmolumentService extends IService<Emolument> {
    /**
    *同步上月薪酬数据
    */
    List<Emolument> insertEmolumentList(QueryRequest request,Period period);

    /**
     *获取当月薪酬数据
     */
    IPage<Emolument> getEmolumentList(Page page, Emolument emolument);
    IPage<Emolument> getEmolumentList_kq(Page page, Emolument emolument);

    void  updateByEmployeeIdAndPeriod(Emolument kj, String periodStr);

    List<Emolument> getEmolumentList( Emolument emolument);
    List<Emolument> getEmolumentList_kq( Emolument emolument);

    Emolument getTotalEmolument(Emolument emolument);

    IPage<Emolument> getDifferentReport(QueryRequest request,Emolument emolument);

    List<Emolument> getDifferentReportDetail(Emolument emolument);

    Emolument getTotalDifferentReport(Emolument emolument);

    IPage<Emolument> getUnEmolumentList(QueryRequest request, Emolument emolument);

    List<Emolument> getUnEmolumentList( Emolument emolument);

    IPage<Emolument> getCancelEmolumentList(QueryRequest request, Emolument emolument);

    void createEmolument(Emolument emolument);

    void updateEmolument(Emolument emolument);

    void updateByEmployeeids(Emolument emolument);

    void updateByIdnumbers(List<Emolument> emolumentList);

    void batchUpdate(List<Emolument>emolumentList);

    void cancelEmolumentList(List<String> ids);

    void passEmolument(Period period);

    void batchCreateEmolument(Emolument emolument);

    /**
    * 根据身份证号和期间获取薪酬
    * */
    Emolument getByIdnumberAndPeriod(String idnumber,Date period);

    List<EmolumentCy> getDifferentReportCyExport(Emolument emolument);

}
