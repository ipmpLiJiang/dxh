package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.system.dao.ComFileMapper;
import cc.mrbird.febs.system.domain.ComFile;
import cc.mrbird.febs.system.domain.Employee;
import cc.mrbird.febs.system.service.IComFileService;
import cc.mrbird.febs.system.service.IEmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class ComFileServiceImpl extends ServiceImpl<ComFileMapper, ComFile> implements IComFileService {
    @Autowired
    IEmployeeService iEmployeeService;

    @Override
    public List<ComFile> findComFile(String id) {
        LambdaQueryWrapper<ComFile> queryWrapper = new LambdaQueryWrapper<>();
        if (id!=null) {
            queryWrapper.eq(ComFile::getId, id);
            return baseMapper.selectList(queryWrapper);
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public void createComFile(ComFile comFile){
        List<ComFile>comFileList=findComFile(comFile.getId());
        if(CollectionUtils.isNotEmpty(comFileList)){
            updateComFile(comFile);
        }
        this.save(comFile);
    }

    public void updateComFile(ComFile comFile){
        this.updateById(comFile);
    }

    public void deleteComFiles(String[] ids){
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Transactional
    public void updateComFileByInId(List<String> idList, String refId) {
        LambdaQueryWrapper<ComFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ComFile::getId,idList);
        ComFile update= new ComFile();
        update.setRefTabId(refId);
        baseMapper.update(update,wrapper);
    }

}
