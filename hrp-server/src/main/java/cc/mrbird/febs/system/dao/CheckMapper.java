package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.Checkinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface CheckMapper extends BaseMapper<Checkinfo> {

    IPage<Checkinfo> findCheckinfoReport(Page page, @Param("check")Checkinfo check);

    List<Checkinfo> findCheckinfoReport2(
            @Param("deptids") String deptids,
            @Param("inputdate") Date inputdate,
            @Param("startdate") Date startdate,
            @Param("enddate") Date enddate,
             @Param("employeeid") Integer employeeid,
             @Param("employeename") String employeename,
            @Param("result") Long result);
}
