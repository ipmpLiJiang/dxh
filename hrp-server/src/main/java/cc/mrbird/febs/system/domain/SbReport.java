package cc.mrbird.febs.system.domain;

import cc.mrbird.febs.common.converter.TimeConverter;
import cc.mrbird.febs.common.utils.CustomerDoubleSerialize;
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
@Excel("社保分析表")
public class SbReport implements Serializable {

    private String employeeid;

    @ExcelField(value = "保险类型",writeConverterExp = "1=养老,2=医疗,3=失业,4=工伤,5=生育,6=大额医疗,7=补助医疗,8=在编养老,9=在编年金")
    private Integer bxtype;

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private Date shdate;

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    @ExcelField(value = "缴纳年月", writeConverter = TimeConverter.class)
    private Date paydate;

    @ExcelField(value = "本月应缴人数")
    private int byyjcount;

    @ExcelField(value = "上月应缴纳人数")
    private int syyjcount;

    @ExcelField(value = "本月新增人数")
    private int xzcount;

    @ExcelField(value = "本月减少人数")
    private int jscount;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "个人缴纳金额")
    private Double gryjsum;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "单位缴纳金额")
    private Double dwyjsum;


    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "本月应缴总金额")
    private Double yjsum;
    @ExcelField(value = "补缴总人数")
    private int bjcount;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "个人补缴金额")
    private Double grbjsum;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "单位补缴金额")
    private Double dwbjsum;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "本月补缴总额")
    private Double bjsum;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "本月缴纳总额")
    private Double jnsum;

    private String[] bjnames;

    private String employeename;

    private String idnumber;

    private Integer employeetype;

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private Date startdate;

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private Date enddate;

    private Integer jftype;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double bzgr;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double bzdw;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double bzlx;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double bzzn;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double degr;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double dedw;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double delx;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double dezn;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double gsgr;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double gsdw;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double gslx;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double gszn;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double shengyugr;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double shengyudw;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double shengyulx;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double shengyuzn;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double sygr;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double sydw;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double sylx;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double syzn;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double yanglaogr;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double yanglaodw;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double yanglaolx;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double yanglaozn;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double ylgr;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double yldw;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double yllx;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double ylzn;

    private String code;



}
