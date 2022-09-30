package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Code;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface ICodeService extends IService<Code> {
    List<Code> findCode(Integer employeeId);
    void createCode(Code code);

    void updateCode(Code code);

    void deleteCodes(String[] codeIds);

    List<Code> findCodeMaxEndDateLists();
}
