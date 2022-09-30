package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface DictService extends IService<Dict> {

    IPage<Dict> findDicts(QueryRequest request, Dict dict);

    List<Dict> getFieldDict(String tableName, String fieldName);
    void createDict(Dict dict);

    void updateDict(Dict dicdt);

    void deleteDicts(String[] dictIds);
    Map<String,Object> getDictMap(String deptid);
    void getDictNameByEmployees(List<Employee> employeeList);

    void getDictNameByEvents(List<Event> eventList);

    void getDictNameByEmoluments(List<Emolument> emolumentList);
}
