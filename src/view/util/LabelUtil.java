package view.util;

/**
 * Label utility class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class LabelUtil {

    public static String styleLabel(String labelText) {
        return "<html><b style=\"color: black;\">" + labelText + "</b></html>";
    }

    public static String styleHeader(String headerText) {
        return "<html><br><br><br><br><center><h1 style=\"color: black;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + headerText + "</h1></center></html>";
    }
}
