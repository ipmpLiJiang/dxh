package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.TitleGet;
import cc.mrbird.febs.system.dao.TitleGetMapper;
import cc.mrbird.febs.system.service.IComFileService;
import cc.mrbird.febs.system.service.ITitleGetService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MrBird
 */
@Service
public class TitleGetServiceImpl extends ServiceImpl<TitleGetMapper, TitleGet> implements ITitleGetService {
    @Autowired
    IComFileService comFileService;

    public List<TitleGet> findTitleGet(Integer employeeId){
        LambdaQueryWrapper<TitleGet> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(TitleGet::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper);
        }else{
            return new ArrayList<>();
        }
    }
    public void createTitleGet(TitleGet title){
        this.save(title);
        List<String> idList = new ArrayList<>();
        if(StringUtils.isNotBlank(title.getZsfileid())) {
            idList.add(title.getZsfileid());
        }
        if(idList.size() > 0) {
            comFileService.updateComFileByInId(idList,title.getId());
        }
    }

    public void updateTitleGet(TitleGet title){
        updateById(title);
    }

    public void deleteTitleGet(String[] titleGetIds){
        baseMapper.deleteBatchIds(Arrays.asList(titleGetIds));
    }

    public List<TitleGet> getOATitleGet(){
        return baseMapper.getOATitleGet();
    }

    public void updateOATitleGet(){
        this.baseMapper.updateOATitleGet();
    }

    public void deleteByEmployeeids(List<Integer> employeeids){
        this.baseMapper.deleteByEmployeeids(employeeids);
    }

    public List<TitleGet> findTitleGetByMaxStartDate(){
        return baseMapper.findTitleGetByMaxStartDate();
    }
}
