package cc.mrbird.febs.system.domain;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class KqGs implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    private transient String employeename;

    private Integer employeeid;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date startdate;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date enddate;

    /**
     * 备注
     */
    private String memo;

    private transient String dept;

    private transient String deptname;

    private transient Integer employeetype;

    private transient String employeetypename;

    private transient Integer rszfw;

    private transient String rszfwname;

    private transient Integer status;

    private transient String statusname;

    private transient String employeecode;



}
