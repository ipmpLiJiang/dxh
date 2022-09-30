package cc.mrbird.febs.system.domain;

import lombok.Data;

@Data
public class InUploadFile {
    private String id;//外键Id
    private String refTab;//外键表
    private String serName;
    private String suffix;
    private String fileName;
}
