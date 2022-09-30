package cc.mrbird.febs.ylj.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.service.ICodeService;
import cc.mrbird.febs.ylj.entity.YljBRecord;
import cc.mrbird.febs.ylj.dao.YljBRecordMapper;
import cc.mrbird.febs.ylj.service.IYljBRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author viki
 * @since 2021-11-25
 */
@Slf4j
@Service("IYljBRecordService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YljBRecordServiceImpl extends ServiceImpl<YljBRecordMapper, YljBRecord> implements IYljBRecordService {

    @Autowired
    ICodeService codeService;

    @Override
    public IPage<YljBRecord> findYljBRecords(QueryRequest request, YljBRecord yljBRecord) {
        try {
            LambdaQueryWrapper<YljBRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(YljBRecord::getIsDeletemark, 1);//1是未删 0是已删
            if (StringUtils.isNotEmpty(yljBRecord.getUserName())) {
                queryWrapper.like(YljBRecord::getUserName, yljBRecord.getUserName());
            }
            if (yljBRecord.getStartDate() !=null) {
                queryWrapper.ge(YljBRecord::getStartDate, yljBRecord.getStartDate());
            }
            if (yljBRecord.getEndDate() != null) {
                queryWrapper.le(YljBRecord::getEndDate, yljBRecord.getEndDate());
            }

            Page<YljBRecord> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public IPage<YljBRecord> findYljBRecordList(QueryRequest request, YljBRecord yljBRecord) {
        try {
            Page<YljBRecord> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            return this.baseMapper.findYljBRecord(page, yljBRecord);
        } catch (Exception e) {
            log.error("获取失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createYljBRecord(YljBRecord yljBRecord) {
        yljBRecord.setCreateTime(new Date());
        yljBRecord.setIsDeletemark(1);
        this.save(yljBRecord);
    }

    @Override
    @Transactional
    public void updateYljBRecord(YljBRecord yljBRecord) {
        yljBRecord.setModifyTime(new Date());
        this.baseMapper.updateYljBRecord(yljBRecord);
    }

    @Override
    @Transactional
    public void deleteYljBRecords(String[] Ids) {
        List<String> list = Arrays.asList(Ids);
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public List<YljBRecord> getAll(String userAccount, String dcaYear) {
        LambdaQueryWrapper<YljBRecord> queryWrapper = new LambdaQueryWrapper<>();

        return this.baseMapper.selectList(queryWrapper);
    }

        @Override
        @Transactional
    public void insertYljBRecord(List<YljBRecord> insertList) {
        if (insertList.size() > 0) {
            Date fkssq = insertList.get(0).getFkssq();
            LambdaQueryWrapper<YljBRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(YljBRecord::getFkssq, fkssq);
            this.remove(wrapper);
            for (YljBRecord item : insertList) {
                item.setCreateTime(new Date());
                item.setIsDeletemark(1);
            }
            this.saveBatch(insertList);
        }
    }

    @Override
    public SbReport getReportYls(Date bkssq, Date ukssq){
        return this.baseMapper.getReportYl(bkssq, ukssq);
    }

    @Override
    public SbReport getReportNjs(Date bkssq, Date ukssq){
        return this.baseMapper.getReportNj(bkssq, ukssq);
    }

    private void getEmployeeid (List<YljBRecord> list) {
        List<Code> codeList = codeService.findCodeMaxEndDateLists();
        List<Code> query = new ArrayList<>();
        for (YljBRecord item : list){
            query = codeList.stream().filter(s->s.getEmployeeid().equals(item.getEmployeeid())).collect(Collectors.toList());
            if(query.size() > 0) {
                item.setEmployeecode(query.get(0).getEmployeecode());
            }
            item.setEmployeename(item.getUserName());
        }
    }

    @Override
    public List<YljBRecord> getXzByIdCardAndFkssqLists(Date bkssq, Date ukssq,boolean isId){
        List<YljBRecord> list = this.baseMapper.getXzByIdCardAndFkssqList(bkssq,ukssq);
        if(isId) {
            this.getEmployeeid(list);
        }
        return list;
    }

    @Override
    public List<YljBRecord> getJsByIdCardAndFkssqLists(Date bkssq, Date ukssq,boolean isId){
        List<YljBRecord> list = this.baseMapper.getJsByIdCardAndFkssqList(bkssq,ukssq);
        if(isId) {
            this.getEmployeeid(list);
        }
        return list;
    }

    @Override
    public List<YljBRecord> getBjByFkssqLists(Date bkssq){
        List<YljBRecord> list = this.baseMapper.getBjByFkssqList(bkssq);
        this.getEmployeeid(list);
        return list;
    }

    @Override
    public List<YljBRecord>  getRecordByIdCardNotEmolumentLists(Date bkssq,String jflx) {
        return this.baseMapper.getRecordByIdCardNotEmolumentList(bkssq, jflx);
    }
}