package cc.mrbird.febs.system.domain;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

/**
 * 
 *
 * @author MrBird
 */
@Data
@Excel("Sheet1")
public class CheckinfoExcel {

    /**
     * 编码
     */
    @ExcelField(value = "人员编号")
    private String code;

    /**
     * 姓名
     */
    @ExcelField(value = "姓名")
    private String employeename;

    /**
     * 性别
     */
    @ExcelField(value = "性别")
    private String gender;

    /**
     * 考核结果
     */
    @ExcelField(value = "考核结果", writeConverterExp = "1=优秀,2=合格,3=不合格,4=不确定考核等次,5=病假超6个月不参加考核,6=长病休不参加考核,7=处分不参加考核,8=其他",
            readConverterExp = "优秀=1,合格=2,不合格=3,不确定考核等次=4,病假超6个月不参加考核=5,长病休不参加考核=6,处分不参加考核=7,其他=8")
    private String result;

    @ExcelField(value = "考核年度")
    private String year;

    /**
     * 备注
     */
    @ExcelField(value = "备注")
    private String memo;


}
