package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.system.domain.Title;
import cc.mrbird.febs.system.dao.TitleMapper;
import cc.mrbird.febs.system.service.ITitleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
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
public class TitleServiceImpl extends ServiceImpl<TitleMapper, Title> implements ITitleService {

    public List<Title> findTitle(Integer employeeId){
        LambdaQueryWrapper<Title> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(Title::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(Title::getEnddate).reversed()).collect(Collectors.toList());
        }else{
            return new ArrayList<>();
        }
    }
    public void createTitle(Title title){
        List<Title> titleList=findTitle(title.getEmployeeid()).stream().sorted(Comparator.comparing(Title::getEnddate).reversed()).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(titleList)){
            updateTitle(titleList.get(0).setEnddate(DateUtil.getYesterday(title.getStartdate())));
        }
        this.save(title);
    }

    public void updateTitle(Title title){
        updateById(title);
    }
    public void deleteTitle(String[] titleIds){
        baseMapper.deleteBatchIds(Arrays.asList(titleIds));
    }


}
