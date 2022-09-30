package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.ElectronicRecords;
import cc.mrbird.febs.system.domain.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IElectronicRecordsService extends IService<ElectronicRecords> {
    List<ElectronicRecords> findElectronicRecords(Integer employeeId);

    void createElectronicRecord(ElectronicRecords electronicRecords);

    void updateElectronicRecord(ElectronicRecords electronicRecords);

    void deleteElectronicRecords(String[] electronicRecordIds);
}
