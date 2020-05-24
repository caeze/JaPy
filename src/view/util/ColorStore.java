package view.util;

import java.awt.Color;
import javax.swing.plaf.ColorUIResource;

/**
 * Color store utility class.
 * 
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ColorStore {

    public final static Color GREEN = Color.GREEN;
    public final static Color ORANGE = Color.ORANGE;
    public final static Color YELLOW = Color.YELLOW;
    public final static Color BLUE = Color.BLUE;
    public final static Color RED = Color.RED;
    public final static Color BLACK = Color.BLACK;
    public final static Color GRAY = Color.GRAY;
    public final static Color WHITE = Color.WHITE;

    public static final Color BACKGROUND = new Color(250, 200, 150);
    public static final Color BACKGROUND_DARK = new Color(100, 75, 55);
    public static final Color BACKGROUND_NORMAL = new Color(200, 150, 100);
    public static final Color BACKGROUND_LIGHT = new Color(240, 220, 180);
    public static final Color BACKGROUND_VERY_LIGHT = new Color(250, 240, 230);
    public static final Color BACKGROUND_DIALOG = new Color(100, 100, 65);
    public static final Color BACKGROUND_CONSOLE = new Color(20, 20, 20);
    public static final Color FOREGROUND_CONSOLE = new Color(200, 200, 200);
    public static final Color FOREGROUND_EDITOR = new Color(20, 20, 20);

    public static ColorUIResource convertToColorUIResource(Color c) {
        return new ColorUIResource(c.getRed(), c.getGreen(), c.getBlue());
    }
}
