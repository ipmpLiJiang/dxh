package cc.mrbird.febs.common.exception;

import org.springframework.http.HttpStatus;


/**
 * FEBS 系统内部异常
 */
public class FebsException extends RuntimeException {

    private static final long serialVersionUID = -994962710559017255L;
    private HttpStatus statusCode;

    public FebsException(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public FebsException(HttpStatus statusCode, String statusText) {
        super(statusText);
        this.statusCode = statusCode;
        fillInStackTrace();
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }
}
