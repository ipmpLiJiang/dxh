package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.system.domain.Contract;
import cc.mrbird.febs.system.dao.ContractMapper;
import cc.mrbird.febs.system.service.IContractService;
import cc.mrbird.febs.system.service.IEmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
@Slf4j
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {
    @Autowired
    IEmployeeService iEmployeeService;
    public List<Contract> findContract(Integer employeeId){
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Contract::getEmployeeid, employeeId);
        return baseMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(Contract::getEnddate).reversed()).collect(Collectors.toList());
    }
    @Transactional
    public void createContract(Contract contract){
        List<Contract>contractList=findContract(contract.getEmployeeid());
        if(CollectionUtils.isNotEmpty(contractList)){
            updateContract(contractList.get(0).setEnddate(DateUtil.getYesterday(contract.getStartdate())));
        }
        contract.setCode(iEmployeeService.findEmployeeById(contract.getEmployeeid()).getCode());
        //contract.setId(UUID.randomUUID().toString());// hsc add 2021-11-20
        this.save(contract);
        log.info(contract.getId());
//        this.addOAContracts(contract);


    }

    public void updateContract(Contract contract){
//        this.baseMapper.updateOAContracts(contract);
        this.updateById(contract);
    }

    public void deleteContracts(String[] contractIds){
//        for (String id: contractIds
//             ) {
//            baseMapper.deleteHrht(id);//hsc 先删除中间表hrht
//        }
        baseMapper.deleteBatchIds(Arrays.asList(contractIds));
    }

    public void addOAContracts(Contract contract){
        this.baseMapper.addOAContracts(contract);
    }


    @Override
    public IPage<Contract> getContractReport(Page page, Contract contract){
        return baseMapper.findContractReport(page,contract);
    }

    @Override
    public List<Contract> getContractReport(Contract contract){
        return baseMapper.findContractReport(contract);
    }

}
