package cc.mrbird.febs.system.domain;

import java.time.LocalDate;
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
public class JobLevelDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private int keyy;
    /**
     * 岗位等级名称
     */
    private String valuee;

    /**
     * 岗位类型编号
     */
    private int jobtypeid;

    private int titleid;

    private String xtUpdateby;

    private Date xtUpdatedate;


}
