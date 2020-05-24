package view.util;

import console.Log;
import java.awt.Color;
import java.awt.Dialog;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import view.img.ImageStore;

/**
 * Modal splash screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class SplashScreenUtil {

    private static JWindow splashScreen;

    /**
     * Shows a small modal splash screen in the center of the screen.
     * @param image the image to show
     */
    public static void showSplashScreen(String image) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    splashScreen = new JWindow();
                    ImageIcon ii = ImageStore.getImageIcon(image);
                    JLabel label = new JLabel(ii);
                    label.setBackground(Color.white);
                    label.setOpaque(true);
                    label.setSize(ii.getIconWidth(), ii.getIconHeight());
                    splashScreen.add(label);
                    splashScreen.setSize(ii.getIconWidth(), ii.getIconHeight());
                    splashScreen.setBackground(Color.white);
                    splashScreen.setLocationRelativeTo(null);
                    splashScreen.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
                    splashScreen.setVisible(true);
                } catch (Exception e) {
                    Log.error(SplashScreenUtil.class, e.getMessage());
                    killSplashScreen();
                }
            }
        });
    }

    /**
     * Destroys the current splash screen in the center of the screen.
     */
    public static void killSplashScreen() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    if (splashScreen != null && splashScreen.isDisplayable()) {
                        splashScreen.dispose();
                        splashScreen = null;
                        System.gc();
                    }
                } catch (Exception e) {
                    Log.error(SplashScreenUtil.class, e.getMessage());
                }
            }
        });
    }
}
