package cc.mrbird.febs.system.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
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
@Excel("社保差异")
public class SbDifferent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelField(value = "险种", required = true, writeConverterExp = "1=基本养老,2=基本医疗,3=失业,4=工伤,5=生育,6=大额医疗,7=公务员补助医疗")
    private transient Integer bxtype;

    @ExcelField(value = "类型")

    private transient String type;

    /**
     * 职工姓名
     */
    @ExcelField(value = "姓名", required = true)
    private String employeename;

    /**
     * 社保号
     */
    @ExcelField(value = "社保个人编号")
    private Long socialnum;

    /**
     * 审核日期
     */
    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    @ExcelField(value = "审核日期", required = true, dateFormat = "yyyyMM")
    private Date shdate;

    /**
     * 缴纳年月
     */
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    @ExcelField(value = "缴费日期", required = true, dateFormat = "yyyyMM")
    private Date paydate;


}
