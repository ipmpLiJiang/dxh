package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.TitleDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface ITitleDetailService extends IService<TitleDetail> {
    List<TitleDetail> findTitleDetail();

    String getName(List<TitleDetail>titleDetailList,String jobLevel);

}
