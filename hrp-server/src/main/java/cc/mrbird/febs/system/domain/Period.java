package cc.mrbird.febs.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author MrBird
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Period implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.INPUT)
    private Integer id;
    /**
     * 期间
     */
    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private Date period;

    /**
     * 状态(0关闭，1开启)
     */
    private Integer status;

    /**
     * 状态(默认是0,,第一次开启后改为1)暂时停用
     */
    //private Integer isfirststatus;


}
