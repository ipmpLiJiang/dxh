package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.GqTitle;
import cc.mrbird.febs.system.dao.GqTitleMapper;
import cc.mrbird.febs.system.service.IComFileService;
import cc.mrbird.febs.system.service.IGqTitleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class GqTitleServiceImpl extends ServiceImpl<GqTitleMapper, GqTitle> implements IGqTitleService {
    @Autowired
    IComFileService comFileService;

    public List<GqTitle> findGqTitle(Integer employeeId){
        LambdaQueryWrapper<GqTitle> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(GqTitle::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(GqTitle::getGetdate).reversed()).collect(Collectors.toList());
        }else{
            return new ArrayList<>();
        }
    }

    public void createGqTitle(GqTitle gqTitle){
        this.save(gqTitle);
        List<String> idList = new ArrayList<>();
        if(StringUtils.isNotBlank(gqTitle.getZsfileid())) {
            idList.add(gqTitle.getZsfileid());
        }
        if(idList.size() > 0) {
            comFileService.updateComFileByInId(idList,gqTitle.getId());
        }
    }

    public void updateGqTitle(GqTitle gqTitle){
        updateById(gqTitle);
    }
    public void deleteGqTitle(String[] titleIds){
        baseMapper.deleteBatchIds(Arrays.asList(titleIds));
    }

    public List<GqTitle> getOAGqTitle(){
        return baseMapper.getOAGqTitle();
    }

    public void deleteByEmployeeids(List<Integer> employeeids){
        this.baseMapper.deleteByEmployeeids(employeeids);
    }

    public void updateOAGqTitle(){
        this.baseMapper.updateOAGqTitle();
    }

}
