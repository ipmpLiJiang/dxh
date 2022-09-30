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
public class CwKj implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 员工编号
     */
    private transient String code;
    
    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private Date period;
    
    /**
     * 期间
     */
    private Integer periodid;

    /**
     * 员工编号
     */
    private Integer employeeid;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private String drugcost;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private String hydropowercost;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private String roomcharge;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private String meetingcost;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private String accumulationcost;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private String incometax;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private String zgongyl;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private String zynj;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private String deylgrbj;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private String hrotherreduce;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private String savedown;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private String actual;
    

    private Long status;
    
}
