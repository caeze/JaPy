package console;

import java.text.SimpleDateFormat;
import java.util.Date;
import preferences.Preferences;

/**
 * Log utility class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Log {

    public static final int LOG_LEVEL_DEBUG = 1;
    public static final int LOG_LEVEL_INFO = 2;
    public static final int LOG_LEVEL_WARNING = 3;
    public static final int LOG_LEVEL_ERROR = 4;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static void debug(Class c, String s) {
        if (Preferences.getInstance().logLevel < LOG_LEVEL_INFO) {
            printStdOut(sdf.format(new Date()) + " DEBUG: " + c.getName() + ": " + s);
        }
    }

    public static void info(Class c, String s) {
        if (Preferences.getInstance().logLevel < LOG_LEVEL_WARNING) {
            printStdOut(sdf.format(new Date()) + " INFO: " + c.getName() + ": " + s);
        }
    }

    public static void warning(Class c, String s) {
        if (Preferences.getInstance().logLevel < LOG_LEVEL_ERROR) {
            printStdOut(sdf.format(new Date()) + " WARNING: " + c.getName() + ": " + s);
        }
    }

    public static void error(Class c, String s) {
        printStdErr(sdf.format(new Date()) + " ERROR: " + c.getName() + ": " + s);
    }

    private static void printStdOut(String s) {
        System.out.println(s);
    }

    private static void printStdErr(String s) {
        System.err.println(s);
    }
}
