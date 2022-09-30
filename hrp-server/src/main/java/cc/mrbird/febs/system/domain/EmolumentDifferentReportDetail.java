package cc.mrbird.febs.system.domain;

import cc.mrbird.febs.common.utils.CustomerDoubleSerialize;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author MrBird
 */
@Data
public class EmolumentDifferentReportDetail  {

    private  String project;

    private Double lastPay;

    private Double currentPay;

    private Double differentPay;

    private String differentPayStr;
}
