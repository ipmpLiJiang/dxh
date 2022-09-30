package cc.mrbird.febs.system.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 *
 * @author MrBird
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class KqXj implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    private String employeeid;

    /**
     * 工号
     */
    private String employeecode;

    /**
     * 姓名
     */
    private String employeename;

    /**
     * 休假开始时间
     */
    private Date startdate;

    /**
     * 休假结束时间
     */
    private Date enddate;

    /**
     * 休假类型
     */
    private Integer xjtype;


}
