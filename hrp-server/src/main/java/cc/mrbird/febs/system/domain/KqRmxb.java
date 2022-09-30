package cc.mrbird.febs.system.domain;

import java.io.Serializable;
import java.util.Date;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@Excel("周排班")
public class KqRmxb implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    @ExcelField(value = "系统ID")
    private String id;

    /**
     * 员工id
     */
    private Integer employeeid;

    /**
     * 工号
     */
    @ExcelField(value = "工号")
    private String employeecode;


    /**
     * 姓名
     */
    @ExcelField(value = "姓名")
    private String employeename;


    @ExcelField(value = "人事子范围",writeConverterExp = "2001=医疗,2002=医技,2003=医药,2004=护理,2005=档案,2006=工程,2007=编辑,2008=统计,2009=会计,2010=管理,2011=工勤",readConverterExp="医疗=2001,医技=2002,医药=2003,护理=2004,档案=2005,工程=2006,编辑=2007,统计=2008,会计=2009,管理=2010,工勤=2011")
    private Integer rszfw;

    /**
     * 排班日期
     */
    @DateTimeFormat(pattern="yyyy/MM/dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy/MM/dd")
    @ExcelField(value = "日期",writeConverter = TimeConverter.class)
    private Date pbdate;

    /**
     * 班次
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ExcelField(value = "班种")
    private String scheduling;

    private Integer employeetype;

    /**
     * 修改人
     */
    private String updateby;

    /**
     * 排班科室
     */
    private String pbdept;

    private Integer rsfw;

    private transient String rszfws;



    private String idnumber;

    /**
     * 审核状态
     */
    private Integer shstatus;

    /**
     * 休假状态
     */
    private int vacationflag;

    /**
     * 核假状态
     */
    private int hjflag;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String sqbid;

    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private transient Date startdate;

    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private transient Date enddate;

    private transient Boolean editflag;

    private transient String employeeids;

    private transient boolean flag;

    private transient int total;


}
