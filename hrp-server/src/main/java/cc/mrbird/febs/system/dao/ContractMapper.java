package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.system.domain.Contract;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MrBird
 */
public interface ContractMapper extends BaseMapper<Contract> {
    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void addOAContracts(@Param("contract")Contract contract);

    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Delete("delete from hrht where id=#{id}")
    void deleteHrht(@Param("id") String id);

    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    void updateOAContracts(@Param("contract")Contract contract);

    IPage<Contract> findContractReport(Page page, @Param("contract")Contract contract);

    List<Contract> findContractReport(@Param("contract")Contract contract);

}
