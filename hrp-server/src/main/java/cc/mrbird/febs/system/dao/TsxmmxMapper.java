package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.Employee;
import cc.mrbird.febs.system.domain.Tsxmmx;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author MrBird
 */
public interface TsxmmxMapper extends BaseMapper<Tsxmmx> {
    IPage<Tsxmmx> findGzxms(Page page, @Param("tsxmmx") Tsxmmx tsxmmx);
    Float getTotalData(@Param("tsxmmx")Tsxmmx tsxmmx);
    Employee findEmployeeByCode(String code);
}
