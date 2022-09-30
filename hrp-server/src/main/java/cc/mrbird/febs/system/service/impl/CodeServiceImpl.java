package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.dao.CodeMapper;
import cc.mrbird.febs.system.domain.Employee;
import cc.mrbird.febs.system.service.ICodeService;
import cc.mrbird.febs.system.service.IEmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
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
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements ICodeService {
    @Autowired
    IEmployeeService iEmployeeService;
    public List<Code> findCode(Integer employeeId) {
        LambdaQueryWrapper<Code> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(Code::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(Code::getEnddate).reversed()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
    @Transactional
    public void createCode(Code code){
        validate(code);
        List<Code>codeList=findCode(code.getEmployeeid()).stream().sorted(Comparator.comparing(Code::getEnddate).reversed()).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(codeList)){
            updateCode(codeList.get(0).setEnddate(DateUtil.getYesterday(code.getStartdate())));
        }
        this.save(code);
    }

    public void updateCode(Code code){
        this.updateById(code);
    }

    public void deleteCodes(String[] codeIds){
        baseMapper.deleteBatchIds(Arrays.asList(codeIds));
    }

    private void validate(Code code){
        LambdaQueryWrapper<Code> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Code::getEmployeecode,code.getEmployeecode());
        queryWrapper.le(Code::getStartdate,code.getStartdate());
        queryWrapper.ge(Code::getEnddate,code.getStartdate());
        List<Code>codes=baseMapper.selectList(queryWrapper);
        if(codes.size()>0){
            Employee employee=iEmployeeService.findEmployeeByCode(codes.get(0).getEmployeecode());
            throw new FebsException(HttpStatus.BAD_REQUEST,"该编号"+employee.getEmployeename()+"正在使用，请停用后再试");
        }
    }

    public List<Code> findCodeMaxEndDateLists() {
        return baseMapper.findCodeMaxEndDateList();
    }
}
