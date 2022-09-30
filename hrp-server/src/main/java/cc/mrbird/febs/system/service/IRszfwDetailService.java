package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.RszfwDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IRszfwDetailService extends IService<RszfwDetail> {
    List<RszfwDetail> findRszfwDetail(Integer rsfwid);

    List<RszfwDetail> findAllRszfwDetail();

    String getName(List<RszfwDetail> rszfwDetails,String rszfw);
}
