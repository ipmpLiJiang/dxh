package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.Code;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author MrBird
 */
public interface CodeMapper extends BaseMapper<Code> {

    List<Code> findCodeMaxEndDateList();

}
