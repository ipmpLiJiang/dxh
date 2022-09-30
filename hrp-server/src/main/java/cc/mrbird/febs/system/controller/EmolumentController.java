package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.service.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/emolument")
public class EmolumentController extends BaseController {
    private String message;
    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    IEmolumentService iEmolumentService;

    @Autowired
    IPeriodService iPeriodService;

    @Autowired
    DictService dictService;

    @Autowired
    FileService fileService;
    @GetMapping
    public Map<String, Object> getEmolumentList(QueryRequest request,Emolument emolument) {
        Page<Employee> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        Map<String,Object> data=getDataTable(this.iEmolumentService.getEmolumentList(page,emolument));
        data.put("sum",iEmolumentService.getTotalEmolument(emolument));
        return data;
    }
    @GetMapping("/list")
    public Map<String, Object> getEmolumentList2(QueryRequest request,Emolument emolument) {
        Page<Employee> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        Map<String,Object> data=getDataTable(this.iEmolumentService.getEmolumentList_kq(page,emolument));
        data.put("sum",iEmolumentService.getTotalEmolument(emolument));
        return data;
    }

    @GetMapping("/getCancelEmolumentList")
    public Map<String, Object> getCancelEmolumentList(QueryRequest request,Emolument emolument) {
        return getDataTable(this.iEmolumentService.getCancelEmolumentList(request,emolument));
    }

    @GetMapping("/getUnEmolumentList")
    public Map<String, Object> getUnEmolumentList(QueryRequest request,Emolument emolument) {
        return getDataTable(this.iEmolumentService.getUnEmolumentList(request,emolument));
    }

    @GetMapping("/getDifferentReport")
    public Map<String, Object> getDifferentReport(QueryRequest request,Emolument emolument){
        Map<String,Object> data=getDataTable(iEmolumentService.getDifferentReport(request,emolument));
        Emolument hj = iEmolumentService.getTotalDifferentReport(emolument);
        if(hj.getCe() == 0) {
            hj = new Emolument();
        }

        data.put("sum", hj);
        return data;
    }

    @GetMapping("/getDifferentReportDetail")
    public FebsResponse getDifferentReportDetail(Emolument emolument) {
        ModelMap map = new ModelMap();
        int success = 0;
        List<EmolumentDifferentReportDetail> detailList = new ArrayList<>();
        try {
            emolument.setEnddate(DateUtil.getLastMonth(emolument.getStartdate()));
            List<Emolument> list = this.iEmolumentService.getDifferentReportDetail(emolument);
            // 本月
            List<Emolument> currentList = list.stream().filter(s->s.getPeriod().equals(emolument.getStartdate())).collect(Collectors.toList());
            // 上月
            List<Emolument> lastList = list.stream().filter(s->s.getPeriod().equals(emolument.getEnddate())).collect(Collectors.toList());

            Emolument entity = new Emolument();
            this.getEmolumentResetMr(entity);

            Emolument currentEmolument = new Emolument();
            Emolument lastEmolument = new Emolument();
            if (currentList.size() > 0) {
                currentEmolument = currentList.get(0);
                this.getEmolumentResetMr(currentEmolument);
            } else {
                currentEmolument = entity;
            }

            if (lastList.size() > 0) {
                lastEmolument = lastList.get(0);
                this.getEmolumentResetMr(lastEmolument);
            } else {
                lastEmolument = entity;
            }


            detailList = this.getEmolumentDifferentDetaillList(currentEmolument,lastEmolument);
            if(detailList.size() > 0) {
                Double sum = 0d;
                EmolumentDifferentReportDetail sumEntity = new EmolumentDifferentReportDetail();
                for (EmolumentDifferentReportDetail item : detailList) {
                    sum += item.getDifferentPay();
                }
                sumEntity.setProject("合计");
                sumEntity.setDifferentPay(sum);
                String fat = NumberUtil.decimalFormat("0.00",sumEntity.getDifferentPay());
                sumEntity.setDifferentPayStr(fat);
                detailList.add(sumEntity);
            }
            success = 1;
        } catch (Exception e) {
            message = "查询失败";
        }
        map.put("message", message);
        map.put("success", success);
        map.put("data", detailList);
        return new FebsResponse().data(map);
    }

    private List<EmolumentDifferentReportDetail> getEmolumentDifferentDetaillList (Emolument ce, Emolument le) {
        List<EmolumentDifferentReportDetail> list = new ArrayList<>();

        EmolumentDifferentReportDetail edre = null;
        // qt 其他1
        if (!ce.getQt().equals(le.getQt())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("其他");
            edre.setLastPay(le.getQt());
            edre.setCurrentPay(ce.getQt());
            edre.setDifferentPay(ce.getQt() - le.getQt());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // gwgz 岗位工资2
        if (!ce.getGwgz().equals(le.getGwgz())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("岗位工资(元)");
            edre.setLastPay(le.getGwgz());
            edre.setCurrentPay(ce.getGwgz());
            edre.setDifferentPay(ce.getGwgz() - le.getGwgz());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // cxjt 冲销津贴3
        if (!ce.getCxjt().equals(le.getCxjt())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("冲销津贴(元)");
            edre.setLastPay(le.getCxjt());
            edre.setCurrentPay(ce.getCxjt());
            edre.setDifferentPay(ce.getCxjt() - le.getCxjt());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // xjgz 薪级工资4
        if (!ce.getXjgz().equals(le.getXjgz())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("薪级工资(元)");
            edre.setLastPay(le.getXjgz());
            edre.setCurrentPay(ce.getXjgz());
            edre.setDifferentPay(ce.getXjgz() - le.getXjgz());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // hljt 护龄津贴5
        if (!ce.getHljt().equals(le.getHljt())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("护龄津贴(元)");
            edre.setLastPay(le.getHljt());
            edre.setCurrentPay(ce.getHljt());
            edre.setDifferentPay(ce.getHljt() - le.getHljt());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // hs 护士10%6
        if (!ce.getHs().equals(le.getHs())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("护士10%(元)");
            edre.setLastPay(le.getHs());
            edre.setCurrentPay(ce.getHs());
            edre.setDifferentPay(ce.getHs() - le.getHs());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // gcb 高出部7
        if (!ce.getGcb().equals(le.getGcb())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("高出部(元)");
            edre.setLastPay(le.getGcb());
            edre.setCurrentPay(ce.getGcb());
            edre.setDifferentPay(ce.getGcb() - le.getGcb());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // tgjt 特岗津贴8
        if (!ce.getTgjt().equals(le.getTgjt())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("特岗津贴(元)");
            edre.setLastPay(le.getTgjt());
            edre.setCurrentPay(ce.getTgjt());
            edre.setDifferentPay(ce.getTgjt() - le.getTgjt());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // ft 房贴9
        if (!ce.getFt().equals(le.getFt())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("房贴(元)");
            edre.setLastPay(le.getFt());
            edre.setCurrentPay(ce.getFt());
            edre.setDifferentPay(ce.getFt() - le.getFt());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // wsf 卫生费10
        if (!ce.getWsf().equals(le.getWsf())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("卫生费(元)");
            edre.setLastPay(le.getWsf());
            edre.setCurrentPay(ce.getWsf());
            edre.setDifferentPay(ce.getWsf() - le.getWsf());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // dzbt 独子补贴11
        if (!ce.getDzbt().equals(le.getDzbt())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("独子补贴(元)");
            edre.setLastPay(le.getDzbt());
            edre.setCurrentPay(ce.getDzbt());
            edre.setDifferentPay(ce.getDzbt() - le.getDzbt());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // jcxjx 基础性绩效12
        if (!ce.getJcxjx().equals(le.getJcxjx())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("基础性绩效(元)");
            edre.setLastPay(le.getJcxjx());
            edre.setCurrentPay(ce.getJcxjx());
            edre.setDifferentPay(ce.getJcxjx() - le.getJcxjx());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // gp 高聘13
        if (!ce.getGp().equals(le.getGp())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("高聘(元)");
            edre.setLastPay(le.getGp());
            edre.setCurrentPay(ce.getGp());
            edre.setDifferentPay(ce.getGp() - le.getGp());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // jtbt 交通补贴14
        if (!ce.getJtbt().equals(le.getJtbt())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("交通补贴(元)");
            edre.setLastPay(le.getJtbt());
            edre.setCurrentPay(ce.getJtbt());
            edre.setDifferentPay(ce.getJtbt() - le.getJtbt());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // wybt 物业补贴15
        if (!ce.getWybt().equals(le.getWybt())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("物业补贴(元)");
            edre.setLastPay(le.getWybt());
            edre.setCurrentPay(ce.getWybt());
            edre.setDifferentPay(ce.getWybt() - le.getWybt());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // cebt 差额补贴16
        if (!ce.getCebt().equals(le.getCebt())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("差额补贴(元)");
            edre.setLastPay(le.getCebt());
            edre.setCurrentPay(ce.getCebt());
            edre.setDifferentPay(ce.getCebt() - le.getCebt());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // gzyf 工资预付17
        if (!ce.getGzyf().equals(le.getGzyf())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("工资预付(元)");
            edre.setLastPay(le.getGzyf());
            edre.setCurrentPay(ce.getGzyf());
            edre.setDifferentPay(ce.getGzyf() - le.getGzyf());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // td2016 调代2016 18
        if (!ce.getTd2016().equals(le.getTd2016())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("调代2016(元)");
            edre.setLastPay(le.getTd2016());
            edre.setCurrentPay(ce.getTd2016());
            edre.setDifferentPay(ce.getTd2016() - le.getTd2016());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // td2017 调代2017 19
        if (!ce.getTd2017().equals(le.getTd2017())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("调代2017(元)");
            edre.setLastPay(le.getTd2017());
            edre.setCurrentPay(ce.getTd2017());
            edre.setDifferentPay(ce.getTd2017() - le.getTd2017());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // td2018 调代2018 20
        if (!ce.getTd2018().equals(le.getTd2018())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("调代2018(元)");
            edre.setLastPay(le.getTd2018());
            edre.setCurrentPay(ce.getTd2018());
            edre.setDifferentPay(ce.getTd2018() - le.getTd2018());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // td2019 调代2019 21
        if (!ce.getTd2019().equals(le.getTd2019())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("调代2019(元)");
            edre.setLastPay(le.getTd2019());
            edre.setCurrentPay(ce.getTd2019());
            edre.setDifferentPay(ce.getTd2019() - le.getTd2019());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // td2020 调代2020 22
        if (!ce.getTd2020().equals(le.getTd2020())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("调代2020(元)");
            edre.setLastPay(le.getTd2020());
            edre.setCurrentPay(ce.getTd2020());
            edre.setDifferentPay(ce.getTd2020() - le.getTd2020());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // td2021 调代2021/差额 23
        if (!ce.getTd2021().equals(le.getTd2021())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("差额(元)");
            edre.setLastPay(le.getTd2021());
            edre.setCurrentPay(ce.getTd2021());
            edre.setDifferentPay(ce.getTd2021() - le.getTd2021());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }
        // sc 上存 24
        if (!ce.getSc().equals(le.getSc())) {
            edre = new EmolumentDifferentReportDetail();
            edre.setProject("上存(元)");
            edre.setLastPay(le.getSc());
            edre.setCurrentPay(ce.getSc());
            edre.setDifferentPay(ce.getSc() - le.getSc());
            String fat = NumberUtil.decimalFormat("0.00",edre.getDifferentPay());
            edre.setDifferentPayStr(fat);
            list.add(edre);
        }

        return list;
    }

    private void getEmolumentResetMr (Emolument emolument) {
        // qt 其他,gwgz 岗位工资,cxjt 冲销津贴,xjgz 薪级工资,hljt 护龄津贴,hs 护士10%,gcb 高出部,tgjt 特岗津贴,ft 房贴,
        // wsf 卫生费,dzbt 独子补贴,jcxjx 基础性绩效,gp 高聘,jtbt 交通补贴,wybt 物业补贴,cebt 差额补贴,gzyf 工资预付,
        // td2016 调代2016,td2017 调代2017,td2018 调代2018,td2019 调代2019,td2020 调代2020,td2021 调代2021/差额,sc 上存
        Double mr = 0d;
        // qt 其他1
        if (emolument.getQt() == null) {
            emolument.setQt(mr);
        }
        // gwgz 岗位工资2
        if (emolument.getGwgz() == null) {
            emolument.setGwgz(mr);
        }
        // cxjt 冲销津贴3
        if (emolument.getCxjt() == null) {
            emolument.setCxjt(mr);
        }
        // xjgz 薪级工资4
        if (emolument.getXjgz() == null) {
            emolument.setXjgz(mr);
        }
        // hljt 护龄津贴5
        if (emolument.getHljt() == null) {
            emolument.setHljt(mr);
        }
        // hs 护士10%6
        if (emolument.getHs() == null) {
            emolument.setHs(mr);
        }
        // gcb 高出部7
        if (emolument.getGcb() == null) {
            emolument.setGcb(mr);
        }
        // tgjt 特岗津贴8
        if (emolument.getTgjt() == null) {
            emolument.setTgjt(mr);
        }
        // ft 房贴9
        if (emolument.getFt() == null) {
            emolument.setFt(mr);
        }
        // wsf 卫生费10
        if (emolument.getWsf() == null) {
            emolument.setWsf(mr);
        }
        // dzbt 独子补贴11
        if (emolument.getDzbt() == null) {
            emolument.setDzbt(mr);
        }
        // jcxjx 基础性绩效12
        if (emolument.getJcxjx() == null) {
            emolument.setJcxjx(mr);
        }
        // gp 高聘13
        if (emolument.getGp() == null) {
            emolument.setGp(mr);
        }
        // jtbt 交通补贴14
        if (emolument.getJtbt() == null) {
            emolument.setJtbt(mr);
        }
        // wybt 物业补贴15
        if (emolument.getWybt() == null) {
            emolument.setWybt(mr);
        }
        // cebt 差额补贴16
        if (emolument.getCebt() == null) {
            emolument.setCebt(mr);
        }
        // gzyf 工资预付17
        if (emolument.getGzyf() == null) {
            emolument.setGzyf(mr);
        }
        // td2016 调代2016 18
        if (emolument.getTd2016() == null) {
            emolument.setTd2016(mr);
        }
        // td2017 调代2017 19
        if (emolument.getTd2017() == null) {
            emolument.setTd2017(mr);
        }
        // td2018 调代2018 20
        if (emolument.getTd2018() == null) {
            emolument.setTd2018(mr);
        }
        // td2019 调代2019 21
        if (emolument.getTd2019() == null) {
            emolument.setTd2019(mr);
        }
        // td2020 调代2020 22
        if (emolument.getTd2020() == null) {
            emolument.setTd2020(mr);
        }
        // td2021 调代2021/差额 23
        if (emolument.getTd2021() == null) {
            emolument.setTd2021(mr);
        }
        // sc 上存 24
        if (emolument.getSc() == null) {
            emolument.setSc(mr);
        }
    }

    @Log("新增薪酬")
    @PostMapping
    public Map<String,Object> addEmolument( Emolument emolument)  {
        this.iEmolumentService.createEmolument(emolument);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("批量新增薪酬")
    @PostMapping("/batchAddEmolument")
    public Map<String,Object> batchAddEmolument(Emolument emolument)  {
        this.iEmolumentService.batchCreateEmolument(emolument);
        return getResult(HttpStatus.OK,FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改薪酬")
    @PutMapping
    public Map<String,Object> updateEmolument(Emolument emolument)  {
        this.iEmolumentService.updateEmolument(emolument);
        return getResult(HttpStatus.OK,FebsConstant.INSERT_SUCCESS,null);
    }
    @Log("批量修改薪酬")
    @PutMapping("/updateEmolumentList")
    public Map<String,Object> updateEmolumentList(Emolument emolument)  {
        iEmolumentService.updateByEmployeeids(emolument);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("批量取消薪酬")
    @PutMapping("/cancelEmolumentList/{employeeids}")
    public Map<String,Object> cancelEmolumentList(@NotBlank(message = "{required}") @PathVariable String employeeids)  {
        List<String> ids= Arrays.asList(employeeids.split(StringPool.COMMA));
        iEmolumentService.cancelEmolumentList(ids);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("薪酬审核通过")
    @GetMapping("/passEmolument")
    public Map<String,Object> passEmolument(Period period) {
        this.iEmolumentService.passEmolument(period);
        return getResult(HttpStatus.OK,FebsConstant.CHECK_SUCCESS,null);
    }

    /**
     * 导出 Excel
     */
    @PostMapping("export")
    public void export( HttpServletResponse response, Emolument emolument) throws FebsException {
        try {
            List<Emolument> list = this.iEmolumentService.getEmolumentList_kq(emolument);
            dictService.getDictNameByEmoluments(list);
//            List<EmolumentExport> listExport =new ArrayList<>();
//            list.forEach(item->{
//                EmolumentExport emolumentExport =new EmolumentExport();
//                BeanUtil.copyProperties(item,emolumentExport, CopyOptions.create().setIgnoreNullValue(true));
//                emolumentExport.setCode(item.getCode());
//                emolumentExport.setEmployeename(item.getEmployeename());
//                listExport.add(emolumentExport);
//                    }
//            );
            ExcelKit.$Export(EmolumentExport.class, response).downXlsx(list, true);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

    /**
     * 导出 Excel
     */
    @PostMapping("cyExport")
    public void cyExport( HttpServletResponse response, Emolument emolument) throws FebsException {
        try {
            List<EmolumentCy> list = this.iEmolumentService.getDifferentReportCyExport(emolument);
            ExcelKit.$Export(EmolumentCy.class, response).downXlsx(list, true);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

    /**
     * 导出 Excel
     */
    @PostMapping("export/qx")
    public void exportQx( HttpServletResponse response, Emolument emolument) throws FebsException {
        try {
            List<Emolument> list = this.iEmolumentService.getUnEmolumentList(emolument);
            dictService.getDictNameByEmoluments(list);
            ExcelKit.$Export(Emolument.class, response).downXlsx(list, true);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

    /**
     * 导入Excel数据，并批量修改薪酬表
     */
    @PostMapping("import")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file) throws FebsException {
        try {
            fileService.validFile(file);
            // 开始导入操作
            long beginTimeMillis = System.currentTimeMillis();
            final List<Emolument> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            ExcelKit.$Import(EmolumentExport.class).readXlsx(file.getInputStream(), new ExcelReadHandler<EmolumentExport>() {
                @Override
                public void onSuccess(int sheet, int row, EmolumentExport emolument) {
                    // 数据校验成功时，加入集合
                    Emolument emolumentImport =new Emolument();
                     BeanUtil.copyProperties(emolument,emolumentImport, CopyOptions.create().setIgnoreNullValue(true));
                    emolumentImport.setCode(emolument.getCode());
                    emolumentImport.setEmployeename(emolument.getEmployeename());
                    data.add(emolumentImport);
                }
                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });
            if (!data.isEmpty()) {
                // 将合法的记录批量入库

                this.iEmolumentService.batchUpdate(data);
            }
            long time = ((System.currentTimeMillis() - beginTimeMillis));
            ImmutableMap<String, Object> result = ImmutableMap.of(
                    "time", time,
                    "data", data,
                    "error", error
            );
            return new FebsResponse().data(result);
        } catch (Exception e) {
            message = "导入Excel数据失败," + e.getMessage();
            log.error(message);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

    /**
     * 导入Excel数据，并批量修改薪酬表
     */
    @PostMapping("import/qx")
    public FebsResponse importExcelsQx(@RequestParam("file") MultipartFile file) throws FebsException {
        try {
            fileService.validFile(file);
            List<Integer> employeeIds=new ArrayList<>();
            Period period=iPeriodService.getLatestPeriod();
            // 开始导入操作
            long beginTimeMillis = System.currentTimeMillis();
            final List<Emolument> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            ExcelKit.$Import(Emolument.class).readXlsx(file.getInputStream(), new ExcelReadHandler<Emolument>() {
                @Override
                public void onSuccess(int sheet, int row, Emolument emolument) {
                    Employee e=iEmployeeService.findEmployeeById(emolument.getEmployeeid());
                    emolument.setIdnumber(e.getIdnumber());
                    emolument.setPeriod(period.getPeriod());
                    emolument.setPeriodid(period.getId());
                    emolument.setIssue(emolument.getQt()+emolument.getGwgz()+emolument.getCxjt()+emolument.getXjgz()+emolument.getHljt()
                    +emolument.getHs()+emolument.getGcb()+emolument.getTgjt()+emolument.getFt()+emolument.getWsf()+emolument.getDzbt()
                    +emolument.getJcxjx()+emolument.getGp()+emolument.getJtbt()+emolument.getWybt()+emolument.getCebt()+emolument.getGzyf()
                    +emolument.getTd2016()+emolument.getTd2017()+emolument.getTd2018()+emolument.getTd2019()+emolument.getTd2020()+emolument.getTd2021()
                    +emolument.getSc());
                    if(e.getEmolumentstatus()==null){
                        data.add(emolument);
                        employeeIds.add(emolument.getEmployeeid());
                    }
                    // 数据校验成功时，加入集合

                }
                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });
            if (!data.isEmpty()) {
                // 将合法的记录批量入库
                this.iEmolumentService.saveBatch(data);
                this.iEmployeeService.updateEmolumentStatus(employeeIds,1);
            }
            long time = ((System.currentTimeMillis() - beginTimeMillis));
            ImmutableMap<String, Object> result = ImmutableMap.of(
                    "time", time,
                    "data", data,
                    "error", error
            );
            return new FebsResponse().data(result);
        } catch (Exception e) {
            message = "导入Excel数据失败," + e.getMessage();
            log.error(message);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }
}
