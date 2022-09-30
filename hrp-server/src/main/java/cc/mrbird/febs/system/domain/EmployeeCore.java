package cc.mrbird.febs.system.domain;


import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.shiro.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 *
 * @author MrBird
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EmployeeCore implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.UUID)
    private String id;

    private Integer employeeid;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date startdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date enddate;

    private Integer status;

    private String statusname;

    private Long hospital;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String ward;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String wardname;

    private String warddescription;

    private String depttype;

    private String dept;

    private String deptname;

    private String deptdescription;

    private String job;

    private String jobname;

    private String position;

    private String positionname;

    private Integer employeetype;

    private String employeetypename;

    private Integer rsfw;

    private String rsfwname;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer rszfw;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String rszfwname;

    private Integer eventtype;

    private String eventtypename;

    private String xtupdateby;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date xtupdatedate;

    private String memo;

    private transient String code;

    public EmployeeCore(){
        xtupdatedate=new Date();
    }
}
