package test;

import console.Log;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Test suite class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class TestSuite {

    private static final List<String> TESTS_TO_RUN = Arrays.asList(ProjectJsonConverterTest.class.getSimpleName(), ListUtilTest.class.getSimpleName());

    public static boolean startTests() {
        boolean success = true;
        try {
            for (String c : TESTS_TO_RUN) {
                Object o = Class.forName(TestSuite.class.getPackage().getName() + "." + c).getDeclaredConstructor().newInstance();
                Method[] methods = o.getClass().getDeclaredMethods();
                for (Method m : methods) {
                    boolean b = (Boolean) m.invoke(o);
                    success &= b;
                    if (!b) {
                        Log.error(TestSuite.class, "Test " + m + " in " + o + " failed!");
                    }
                }
            }
        } catch (Exception e) {
            success = false;
            Log.error(TestSuite.class, e.toString());
            Log.error(TestSuite.class, e.getCause().toString());
            for (StackTraceElement t : e.getStackTrace()) {
                Log.error(TestSuite.class, t.toString());
            }
        }
        return success;
    }
}
