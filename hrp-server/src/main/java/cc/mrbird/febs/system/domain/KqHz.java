package cc.mrbird.febs.system.domain;


import cc.mrbird.febs.common.annotation.KqFiled;
import cc.mrbird.febs.common.converter.TimeConverter;
import cc.mrbird.febs.common.utils.CustomerDoubleSerialize;
import com.baomidou.mybatisplus.annotation.IdType;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 *
 * @author MrBird
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Excel("考勤汇总报表")
public class KqHz implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 员工编号
     */
    @ExcelField(value = "编号")
    private String employeecode;

    /**
     * 员工姓名
     */
    @ExcelField(value = "姓名")
    private String employeename;

    /**
     * 身份证号
     */
    private String idnumber;

    /**
     * 科室
     */
    @ExcelField(value = "科室")
    private String dept;

    /**
     * 人员类型
     */
    @ExcelField(value = "员工类型",writeConverterExp = "1=在编,2=合同,3=代理,4=协和在编,5=协和非编",readConverterExp="在编=1,合同=2,代理=3,协和在编=4,协和非编=5")
    private Integer employeetype;

    /**
     * 考勤年月
     */
    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    @ExcelField(value = "考勤年月",writeConverter = TimeConverter.class)
    private Date kqdate;

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private Date shdate;

    @KqFiled
    @ExcelField(value = "病")
    private Integer bing;

    @KqFiled
    @ExcelField(value = "事")
    private Integer shi;

    @KqFiled
    @ExcelField(value = "产")
    private Integer chan;

    @KqFiled
    @ExcelField(value = "空")
    private Integer kong;

    @KqFiled
    private Integer xx;


    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "扣款1")
    private Double kk1;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "扣款2")
    private Double kk2;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double gwgz;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double xjgz;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double wsf;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double jcxjx;

    private transient String employeeid;

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private transient Date startdate;

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private transient Date enddate;

    private Integer kqstatus;

    private String memo;

    private Integer kktype;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "合计")
    private transient Double sum;



}
