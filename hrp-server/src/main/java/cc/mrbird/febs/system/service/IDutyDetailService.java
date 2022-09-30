package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.DutyDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IDutyDetailService extends IService<DutyDetail> {
    List<DutyDetail> findDutyDetail(Integer titleId);

    String getName(List<DutyDetail>dutyDetailList,String jobLevel);
}
