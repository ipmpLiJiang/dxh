package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.ExportExcelUtils;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.service.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/kq/rmxb")
public class KqRmxbController extends BaseController {
    private String message;
    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    IKqRmxbService iKqRmxbService;

    @Autowired
    IKqJkService iKqJkService;

    @Autowired
    IKqPbDetailService iKqPbDetailService;

    @Autowired
    FileService fileService;


    @GetMapping("getRmxList")
    public Map<String, Object> getRmxList(QueryRequest request,KqRmxb kqRmxb) {
        return getDataTable(this.iKqRmxbService.getRmxList(kqRmxb,request));
    }

    @GetMapping("/getPbByWeek")
    public Map<String,Object> getPbByWeek(String pbdept, Date startdate, String username,String rszfws){
        return iKqRmxbService.getByStartDate(pbdept,startdate,username,rszfws);
    }

    @GetMapping("/getPbByMonth")
    public Map<String,Object> getPbByMonth(KqRmxb kqRmxb){
        return iKqRmxbService.getPbByMonth(kqRmxb);
    }

    @GetMapping("/pb-report")
    public Map<String, Object> getPbReport(QueryRequest request,KqRmxb kqRmxb){
        return getDataTable(this.iKqRmxbService.getPbReport(kqRmxb,request));
    }
    @PostMapping("/pb-export")
    public void getPbReport2(HttpServletResponse response,String dataJson,KqRmxb kqRmxb){

        try {
            List<KqPbDetail> kq=iKqPbDetailService.getPbList(null,null);
            List<KqRmxb> list= this.iKqRmxbService.getPbReportExport(kqRmxb);
            list.forEach(p->{
                String name= kq.stream().filter(t->t.getKeyy().equals(Integer.parseInt(p.getScheduling()))).findFirst().get().getValuee();
                p.setScheduling(name);
            });
            ExportExcelUtils.exportCustomExcel_han(response, list,dataJson,"");
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }


    @PostMapping
    public Map<String,Object> createKqjk(KqRmxb kqRmxb){
        iKqJkService.batchCreateKqjk(kqRmxb);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @PutMapping("/{pbdept}/{username}")
    @Log("更新排班")
    public Map<String,Object> updateByWeek(@PathVariable(value = "pbdept") String pbdept,@PathVariable(value = "username") String username,@RequestBody List<Employee> employees){
        iKqRmxbService.updateByWeek(pbdept,username,employees);
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("修改排班")
    @PutMapping("/updateRmx")
    public Map<String,Object> updateRmx(@Valid KqRmxb kqRmxb) {
        this.iKqRmxbService.updateKqRmxb(kqRmxb);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @PutMapping("/batchSetschedul")
    public Map<String,Object> batchSetschedul(KqRmxb kqRmxb){
        iKqRmxbService.batchSetschedul(kqRmxb);
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS,null);
    }

    /**
     * 导出 Excel
     */
    @PostMapping("/export/week")
    public void exportWeek(HttpServletResponse response, String pbdept, Date startdate, String username,String rszfws) throws FebsException {
        try {
            List<Employee> employeeList = (List<Employee>) iKqRmxbService.getByStartDate(pbdept,startdate,username,rszfws).get("employeeList");
            List<KqRmxb> rmxList=new ArrayList<>();
            for(Employee e: employeeList){
                for(KqRmxb rmx:e.getRmxList()){
                    if(rmx.getEditflag()){
                        rmxList.add(rmx);
                    }
                }
            }
            iKqPbDetailService.getPbName(rmxList);
            ExcelKit.$Export(KqRmxb.class, response).downXlsx(rmxList, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

    /**
     * 导出 Excel
     */
    @PostMapping("/export/month")
    public void exportMonth(HttpServletResponse response, KqRmxb kqRmxb) throws FebsException {
        try {
            List<KqJk> list = (List<KqJk>) iKqRmxbService.getPbByMonth(kqRmxb).get("pblist");
            iKqPbDetailService.getPbNameByKqjk(list);
            ExcelKit.$Export(KqJkExcel.class, response).downXlsx(list, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

    /**
     * 导入Excel数据
     */
    @PostMapping("/import/{pbdept}")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file,@PathVariable(value = "pbdept") String pbdept) throws FebsException {
        try {
            fileService.validFile(file);
            // 开始导入操作
            long beginTimeMillis = System.currentTimeMillis();
            final List<KqRmxb> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            ExcelKit.$Import(KqRmxb.class).readXlsx(file.getInputStream(), new ExcelReadHandler<KqRmxb>() {
                @Override
                public void onSuccess(int sheet, int row, KqRmxb kqRmxb) {
                    // 数据校验成功时，加入集合
                    data.add(kqRmxb);
                }
                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });
            if (!data.isEmpty()) {
                // 将合法的记录批量入库
                this.iKqRmxbService.batchUpdate(data,pbdept);
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
