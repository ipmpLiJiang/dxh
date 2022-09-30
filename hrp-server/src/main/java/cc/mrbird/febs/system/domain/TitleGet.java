package cc.mrbird.febs.system.domain;

import java.time.LocalDate;
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
public class TitleGet implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 职工编号
     */
    private Integer employeeid;

    /**
     * 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date startdate;

    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date enddate;

    /**
     * 职称
     */
    private String title;

    /**
     * 职称名称
     */
    private String titlename;

    /**
     * 专业技术职务
     */
    private String duty;

    /**
     * 职务名称
     */
    private String dutyname;

    /**
     * 证书路径
     */
    private String certificatepath;

    /**
     * 证书名称
     */
    private String certificatename;

    /**
     * 证书编号
     */
    private String number;

    private String xtUpdateby;

    @TableField("xt_updatedate")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date xtUpdatedate;

    private String operation;

    private transient String code;

    private String zyname;

    private String pzdw;

    private String pzwh;

    private transient String zsfileid;
}
