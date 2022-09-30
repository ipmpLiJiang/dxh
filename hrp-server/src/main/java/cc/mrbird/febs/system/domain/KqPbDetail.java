package cc.mrbird.febs.system.domain;

import java.io.Serializable;

import io.swagger.models.auth.In;
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
public class KqPbDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer keyy;

    /**
     * 班种
     */
    private String valuee;

    /**
     * 所属科室
     */
    private String parentvaluee;

    private Integer flag;


}
