package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Contract;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IContractService extends IService<Contract> {
    List<Contract> findContract(Integer employeeId);
    void createContract(Contract contract);

    void updateContract(Contract contract);

    void deleteContracts(String[] contractIds);

    void addOAContracts(Contract contract);

    IPage<Contract> getContractReport(Page page, Contract contract);

    List<Contract> getContractReport(Contract contract);
}
