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
public class KqDept implements Serializable {

    private static final long serialVersionUID = 1L;

    private String keyy;

    private String valuee;


}
