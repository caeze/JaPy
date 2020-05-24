package view.util;

import console.Log;
import java.awt.Color;
import java.awt.Dialog;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import view.img.ImageStore;

/**
 * Modal loading animation.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class LoadingAnimation {

    private static JWindow loadingAnimScreen;

    /**
     * Shows a small modal loading animation in the center of the screen.
     */
    public static void showLoadingAnim() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    loadingAnimScreen = new JWindow();
                    JLabel label = new JLabel(ImageStore.getImageIcon("loading.gif"));
                    label.setBackground(Color.white);
                    label.setOpaque(true);
                    Border border = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black);
                    label.setBorder(border);
                    label.setSize(30, 30);
                    loadingAnimScreen.add(label);
                    loadingAnimScreen.setSize(30, 30);
                    loadingAnimScreen.setBackground(Color.white);
                    loadingAnimScreen.setOpacity(0.85f);
                    loadingAnimScreen.setLocationRelativeTo(null);
                    loadingAnimScreen.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
                    loadingAnimScreen.setVisible(true);
                } catch (Exception e) {
                    Log.error(LoadingAnimation.class, e.getMessage());
                    killLoadingAnim();
                }
            }
        });
    }

    /**
     * Destroys the current small modal loading animation in the center of the
     * screen.
     */
    public static void killLoadingAnim() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    if (loadingAnimScreen != null && loadingAnimScreen.isDisplayable()) {
                        loadingAnimScreen.dispose();
                        loadingAnimScreen = null;
                        System.gc();
                    }
                } catch (Exception e) {
                    Log.error(LoadingAnimation.class, e.getMessage());
                }
            }
        });
    }
}
