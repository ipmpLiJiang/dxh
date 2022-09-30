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
public class RsfwDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private int keyy;
    /**
     * 人事范围名称
     */
    private String valuee;

    private String xtUpdateby;

    private Date xtUpdatedate;


}
