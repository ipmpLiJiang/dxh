package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.Employee;
import cc.mrbird.febs.system.domain.Event;
import cc.mrbird.febs.system.service.DictService;
import cc.mrbird.febs.system.service.IEventService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/event")
public class EventController extends BaseController {
    private String message;
    @Autowired
    IEventService iEventService;

    @Autowired
    DictService dictService;
    @GetMapping("/getEventReport")
    public Map<String, Object> getEmployeeReport(QueryRequest request, Event event) {
        Page<Employee> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return getDataTable(this.iEventService.getEventReport(page,event));
    }

    /**
     * 导出 Excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Event event) throws FebsException {
        try {
            List<Event> list = this.iEventService.getEventReport(event);
            dictService.getDictNameByEvents(list);
            ExcelKit.$Export(Event.class, response).downXlsx(list, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }
}
