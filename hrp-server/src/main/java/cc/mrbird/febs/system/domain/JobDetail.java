package cc.mrbird.febs.system.domain;

import java.io.Serializable;
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
public class JobDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位编号
     */
    private String keyy;

    /**
     * 岗位名称
     */
    private String valuee;


}
