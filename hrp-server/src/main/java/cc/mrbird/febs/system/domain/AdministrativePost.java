package cc.mrbird.febs.system.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class AdministrativePost implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 员工编号
     */
    private Integer employeeid;

    /**
     * 任命开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date startdate;

    /**
     * 任命结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date enddate;

    /**
     * 科室类型
     */
    private String depttype;

    /**
     * 行政职务科室
     */
    private String dept;

    /**
     * 行政职务
     */
    private String job;

    /**
     * 任命文件
     */
    private String appointmentdocuments;

    /**
     * 任命文件路径
     */
    private String appointmentdocumentspath;

    /**
     * 免职文件
     */
    private String removaldocuments;

    /**
     * 免职文件路径
     */
    private String removaldocumentspath;

    private String xtUpdateby;

    private Date xtUpdatedate;


}
