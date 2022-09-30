package cc.mrbird.febs.common.handler;

import cc.mrbird.febs.common.exception.FebsException;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.NoSuchElementException;

/**
 * 自定义异常响应，遵循spring异常格式
 *
 * @author MrBird
 */

@Data
public class FebsResult {
    private Date timestamp = new Date();
    private Integer status;
    private String error;
    private String message;
    private String trace;

    public FebsResult(FebsException e) {
        this.status = e.getStatusCode().value();
        this.error = e.getStatusCode().getReasonPhrase();
        this.message = e.getMessage();
        this.trace = ExceptionUtils.getStackTrace(e);
    }

    public FebsResult(NoSuchElementException e) {
        this.status = HttpStatus.NOT_FOUND.value();
        this.error = HttpStatus.NOT_FOUND.getReasonPhrase();
        this.message = e.getMessage();
        this.trace = ExceptionUtils.getStackTrace(e);
    }
}
