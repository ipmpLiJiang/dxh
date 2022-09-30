package cc.mrbird.febs.system.domain;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Date;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Excel("考勤申请表")
public class KqSqb implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 排班科室
     */
    @ExcelField(value = "科室编号")
    private String pbdept;

    /**
     * 排班时间
     */
    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    @ExcelField(value = "排班年月",writeConverter = TimeConverter.class)
    private Date pbdate;

    /**
     * 提交时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "考勤年月",writeConverter = TimeConverter.class)
    private Date tjdate;

    @ExcelField(value = "科室人数")
    private Integer kscount;

    private Integer tjcount;

    /**
     * 审核状态
     */
    @ExcelField(value = "提交状态",writeConverterExp = "1=已提交,2=审核通过,3=已驳回")
    private Integer shstatus;

    private String memo;


}
