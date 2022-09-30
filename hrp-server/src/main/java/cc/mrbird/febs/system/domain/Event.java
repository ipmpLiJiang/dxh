package cc.mrbird.febs.system.domain;

import java.io.Serializable;
import java.util.Date;

import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
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
@Excel("异动报表")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    /*****编号信息******/
    @ExcelField(value = "编号")
    private transient String code;

    @ExcelField(value = "员工姓名", required = true, maxLength = 5,
            comment = "提示：必填，长度不能超过5个字符")
    private transient String employeename;

    @ExcelField(value = "事件类型",writeConverterExp = "1=入职,2=轮转,3=调动,4=退休,5=返聘,6=外调,7=离职,8=死亡")
    private Integer event;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "事件时间",writeConverter = TimeConverter.class)
    private Date eventdate;

    @ExcelField(value = "性别")
    private transient String gender;

    @ExcelField(value = "员工状态",writeConverterExp = "1=在职,2=退休,3=返聘,4=外调,5=离职,6=死亡,7=离休")
    private transient String statusname;

    @ExcelField(value = "科室")
    private transient String deptname;

    @ExcelField(value = "病区")
    private transient String wardname;

    @ExcelField(value = "员工类型",writeConverterExp = "1=在编,2=合同")
    private transient String employeetype;

    @ExcelField(value = "人事范围")
    private transient String  rsfwname;

    @ExcelField(value = "人事子范围")
    private transient String rszfwname;

    @ExcelField(value = "身份证号")
    private transient String idnumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "参加工作时间",writeConverter = TimeConverter.class)
    private transient Date workdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "来院时间",writeConverter = TimeConverter.class)
    private transient Date comedate;

    @ExcelField(value = "联系方式")
    private transient String phonenumber;

    @ExcelField(value = "民族")
    private transient String nation;

    @ExcelField(value = "籍贯")
    private transient String homeaddress;

    private Integer employeeid;

    private String coreid;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date startdate;


    private String eventname;

    private String xtUpdateby;

    private Date xtUpdatedate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private transient Date eventstartdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private transient Date eventenddate;

    private transient String eventtypes;//事件类型

    private transient Integer eventtype;

    public Event(){
        xtUpdatedate=new Date();
    }

}
