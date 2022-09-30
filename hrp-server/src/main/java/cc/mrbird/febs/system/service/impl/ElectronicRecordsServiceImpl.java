package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.ElectronicRecords;
import cc.mrbird.febs.system.dao.ElectronicRecordsMapper;
import cc.mrbird.febs.system.service.IElectronicRecordsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MrBird
 */
@Service
public class ElectronicRecordsServiceImpl extends ServiceImpl<ElectronicRecordsMapper, ElectronicRecords> implements IElectronicRecordsService {

    public List<ElectronicRecords> findElectronicRecords(Integer employeeId){
        LambdaQueryWrapper<ElectronicRecords> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(ElectronicRecords::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper);
        }else{
            return new ArrayList<>();
        }
    }

    public void createElectronicRecord(ElectronicRecords electronicRecords){
        this.save(electronicRecords);
    }
    public void updateElectronicRecord(ElectronicRecords electronicRecords){
        updateById(electronicRecords);
    }

    public void deleteElectronicRecords(String[] electronicRecordIds){
        baseMapper.deleteBatchIds(Arrays.asList(electronicRecordIds));
    }
}
