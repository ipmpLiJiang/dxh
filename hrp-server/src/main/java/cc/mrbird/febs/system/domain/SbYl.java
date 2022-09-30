package cc.mrbird.febs.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@Excel("sheet2")
public class SbYl implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 社保号
     */
    @ExcelField(value = "个人编号")
    private Long socialnum;

    /**
     * 职工姓名
     */
    @ExcelField(value = "姓名")
    private String employeename;

    @ExcelField(value = "公民身份号码")
    private String idnumber;

    @ExcelField(value = "险种类型", required = true, readConverterExp = "基本养老=1")
    private transient Integer bxtype;

    /**
     * 缴纳年月
     */
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    @ExcelField(value = "缴费年月", required = true, dateFormat = "yyyyMM")
    private Date paydate;

    /**
     * 审核日期
     */
    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    @ExcelField(value = "审核年月", required = true, dateFormat = "yyyyMM")
    private Date shdate;

    @ExcelField(value = "缴费基数")
    private  Float js;

    @ExcelField(value = "应缴类型",readConverterExp = "月核定=1,补缴=2")
    private Integer jftype;

    @ExcelField(value = "应缴金额")
    private Float yj;

    /**
     * 医疗单位缴纳
     */
    @ExcelField(value = "单位应缴金额")
    private Float dw;

    /**
     * 医疗个人缴纳
     */
    @ExcelField(value = "个人应缴金额")
    private Float gr;

    /**
     * 医疗补缴利息
     */
    @ExcelField(value = "补缴利息")
    private Float lx;

    /**
     * 医疗滞纳金
     */
    @ExcelField(value = "滞纳金/积累额")
    private Float zn;

    private transient String employeecode;

    private transient Integer employeetype;

    private transient String type;

    /**
     * 系统编号
     */
    private transient Integer employeeid;
}
