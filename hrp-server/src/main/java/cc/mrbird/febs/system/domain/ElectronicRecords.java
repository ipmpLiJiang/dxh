package cc.mrbird.febs.system.domain;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Date;

import cc.mrbird.febs.common.authentication.JWTUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class ElectronicRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 职工编号
     */
    private Integer employeeid;

    /**
     * 事件日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date eventdate;

    /**
     * 事件
     */
    private String event;

    /**
     * 事件描述
     */
    private String eventdescription;

    /**
     * 附件路径
     */
    private String path;

    private String xtUpdateby;

    private Date xtUpdatedate;

    public ElectronicRecords(){
        String token = (String) SecurityUtils.getSubject().getPrincipal();
        xtUpdateby= JWTUtil.getUsername(token);
        xtUpdatedate=new Date();
    }

}
