package cc.mrbird.febs.system.domain;


import java.io.Serializable;
import java.util.Date;

import cc.mrbird.febs.common.converter.TimeConverter;
import cc.mrbird.febs.common.utils.CustomerDoubleSerialize;
import com.baomidou.mybatisplus.annotation.IdType;
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
@Excel("特殊工资项目")
public class Tsxmmx implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    private Integer employeeid;

    @ExcelField(value = "工号")
    private transient String employeecode;

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    @ExcelField(value = "期间",dateFormat  = "yyyy-MM")
    private Date period;

    @ExcelField(value = "工资项目")
    private String gzxm;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "金额",  maxLength = 20)
    private Double je;

    @ExcelField(value = "备注")
    private String memo;

    private transient String employeename;



}
