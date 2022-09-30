package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.Emolument;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface EmolumentMapper extends BaseMapper<Emolument> {
    IPage<Emolument> initEmolumentList(Page page);
    /**
     * 查询当月的薪酬数据
     */
    IPage<Emolument> getEmolumentList(Page page,@Param("emolument")Emolument emolument);
    IPage<Emolument> getEmolumentList_kq(Page page,@Param("emolument")Emolument emolument);

    List<Emolument> getEmolumentList(@Param("emolument")Emolument emolument);

    List<Emolument> getEmolumentList_kq(@Param("emolument")Emolument emolument);

    Emolument getEmolumentByEmployeeidAndPeriod(Date period,Integer employeeid);

    Emolument getTotalEmolument(@Param("emolument")Emolument emolument);

    IPage<Emolument>getCancelEmolumentList(Page page, Emolument emolument);


    IPage<Emolument> getDifferentReport(Page page, @Param("emolument")Emolument emolument);

    List<Emolument> getDifferentReport1(@Param("emolument")Emolument emolument);

    Emolument getTotalDifferentReport(@Param("emolument")Emolument emolument);

    List<Emolument> getDifferentReportDetail(@Param("emolument")Emolument emolument);
    /**
     * 查询没有起薪的员工数据
     */
    IPage<Emolument> getUnEmolumentList(Page page,@Param("emolument")Emolument emolument);
    List<Emolument> getUnEmolumentList(@Param("emolument")Emolument emolument);

    void passEmolument(@Param("periodId") int periodId);

    void updateByEmployeeids(@Param("emolument")Emolument emolument);
    void batchUpdate(@Param("emolumentList")List<Emolument>emolumentList);

    void updateByIdnumbers(@Param("emolumentList")List<Emolument>emolumentList);

    void updateByEmployeeIdAndPeriod(@Param("emolument")Emolument kj,@Param("periodStr")String periodStr);

}
