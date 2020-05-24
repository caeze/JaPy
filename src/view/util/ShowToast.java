package view.util;

import console.Log;
import java.awt.Color;
import java.awt.Dialog;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Little window containing a text, shown for a specified time.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ShowToast {

    private JWindow toastScreen;

    /**
     * Shows a small modal text in a little window on the main screen.
     *
     * @param text the text to be displayed
     * @param timeToShowToastMs time in ms before the window will disappear
     * @param xPos xPos
     * @param yPos yPos
     * @param color background color
     */
    public static void showToastStatic(String text, int timeToShowToastMs, int xPos, int yPos, Color color) {
        new ShowToast().showToast(text, timeToShowToastMs, xPos, yPos, false, color, 25f);
    }

    /**
     * Shows a small modal text in a little window on the main screen.
     *
     * @param text the text to be displayed
     * @param timeToShowToastMs time in ms before the window will disappear
     * @param xPos xPos
     * @param yPos yPos
     * @param color background color
     * @param textSize the font size of the text to display
     */
    public static void showToastStatic(String text, int timeToShowToastMs, int xPos, int yPos, Color color, double textSize) {
        new ShowToast().showToast(text, timeToShowToastMs, xPos, yPos, false, color, (float) textSize);
    }

    /**
     * Shows a small modal text in a little window in the center of the main screen.
     *
     * @param text the text to be displayed
     * @param timeToShowToastMs time in ms before the window will disappear
     * @param color background color
     */
    public static void showToastCenteredStatic(String text, int timeToShowToastMs, Color color) {
        new ShowToast().showToast(text, timeToShowToastMs, 0, 0, true, color, 25f);
    }
    
    private void showToast(String text, int timeToShowToastMs, int xPos, int yPos, boolean isCentered, Color color, float textSize) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    toastScreen = new JWindow();
                    JLabel label = new JLabel(" " + text + " ");
                    Border redBorder = BorderFactory.createMatteBorder(4, 4, 4, 4, color);
                    label.setBorder(redBorder);
                    label.setBackground(color);
                    label.setForeground(Color.black);
                    if (0.2 * color.getRed() + 0.7 * color.getGreen() + 0.1 * color.getBlue() < 140) {
                        label.setForeground(Color.white);
                    }
                    label.setFont(label.getFont().deriveFont(textSize));
                    label.setOpaque(true);
                    label.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    toastScreen.add(label);
                    toastScreen.setBackground(Color.white);
                    toastScreen.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
                    toastScreen.setType(JWindow.Type.POPUP);
                    toastScreen.setVisible(true);
                    toastScreen.pack();
                    if (isCentered) {
                        toastScreen.setLocationRelativeTo(null);
                    } else {
                        toastScreen.setLocation(xPos, yPos);
                    }
                } catch (Exception e) {
                    Log.error(ShowToast.class, e.getMessage());
                    killToast();
                }
            }
        });
        
        if (timeToShowToastMs > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(timeToShowToastMs);
                    } catch (InterruptedException e) {
                        Log.error(ShowToast.class, e.getMessage());
                    }
                    killToast();
                }
            }).start();
        }
    }

    private void killToast() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    if (toastScreen != null && toastScreen.isDisplayable()) {
                        toastScreen.dispose();
                        toastScreen = null;
                        System.gc();
                    }
                } catch (Exception e) {
                    Log.error(ShowToast.class, e.getMessage());
                }
            }
        });
    }
}
