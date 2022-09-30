package cc.mrbird.febs.system.domain;

import java.io.Serializable;
import java.util.Date;

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
public class DutyDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String keyy;
    /**
     * 专业技术职务名称
     */
    private String valuee;

    /**
     * 职称编号
     */
    private String titleid;

    private String xtUpdateby;

    private Date xtUpdatedate;


}
