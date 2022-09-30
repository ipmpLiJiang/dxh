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
@Excel("sheet1")
public class SbYanglao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 社保号
     */
    @ExcelField(value = "个人编号")
    private Long socialnum;

    private transient String employeecode;

    /**
     * 职工姓名
     */
    @ExcelField(value = "姓名")
    private String employeename;

    @ExcelField(value = "公民身份号码")
    private String idnumber;

    @ExcelField(value = "险种类型", required = true, readConverterExp = "基本养老=1,基本医疗=2,失业=3,工伤=4,生育=5,大额医疗=6,公务员补助医疗=7",writeConverterExp ="1=基本养老,2=基本医疗,3=失业,4=工伤,5=生育,6=大额医疗,7=公务员补助医疗" )
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

    @ExcelField(value = "应缴类型",readConverterExp = "月核定=1,补缴=2,一次性补足医疗=3",writeConverterExp = "1=月核定,2=补缴,3=一次性补足医疗")
    private Integer jftype;

    @ExcelField(value = "应缴金额")
    private Float yj;

    /**
     * 养老单位缴纳
     */
    @ExcelField(value = "单位应缴金额")
    private Float dw;

    /**
     * 养老个人缴纳
     */
    @ExcelField(value = "个人应缴金额")
    private Float gr;

    /**
     * 养老补缴利息
     */
    @ExcelField(value = "补缴利息")
    private Float lx;

    /**
     * 养老滞纳金
     */
    @ExcelField(value = "滞纳金/积累额")
    private Float zn;


    private transient Integer employeetype;

    private transient String type;

    /**
     * 系统编号
     */
    private transient Integer employeeid;
}
