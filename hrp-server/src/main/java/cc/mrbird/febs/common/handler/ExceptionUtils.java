package cc.mrbird.febs.common.handler;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常处理工具类
 *
 * @author MrBird
 */

public interface ExceptionUtils {

    public static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
