package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.system.domain.KqJk;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface KqJkMapper extends BaseMapper<KqJk> {
    IPage<KqJk> getKqJkList(Page page, @Param("kqjk")KqJk kqJk);
    List<KqJk> getKqJkList(@Param("kqjk")KqJk kqJk);
    IPage<KqJk> findDuplicateList(Page page, @Param("kqjk")KqJk kqJk);
    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<KqJk> getOAKqJk();

    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateOAKqJk(Date updatetime);
}
