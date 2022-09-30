package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.KqGs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author MrBird
 */
public interface KqGsMapper extends BaseMapper<KqGs> {
    IPage<KqGs> getGsList(Page page, @Param("kqGs")KqGs kqGs);
}
