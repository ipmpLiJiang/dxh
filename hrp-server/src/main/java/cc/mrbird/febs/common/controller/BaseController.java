package cc.mrbird.febs.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    protected Map<String, Object> getDataTable(IPage<?> pageInfo) {
        Map<String, Object> rspData = new HashMap<>();
        rspData.put("rows", pageInfo.getRecords());
        rspData.put("total", pageInfo.getTotal());
        Map<String,String> pageMap=new HashMap<>();
        pageMap.put("currentPage",pageInfo.getCurrent()+"");
        pageMap.put("pageSize",pageInfo.getSize()+"");
        rspData.put("pageInfo",pageMap);
        return rspData;
    }

    protected Map<String, Object>getResult(HttpStatus status, String message,Object o){
        Map<String,Object> result=new HashMap<>();
        result.put("status", status);
        result.put("timestamp",new Date());
        result.put("message",message);
        result.put("data",o);
        return result;
    }


}
