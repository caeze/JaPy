package view;

import console.Log;
import control.Control;
import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import preferences.Preferences;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ColorStore;
import view.util.ButtonUtil;

/**
 * Help screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Help implements IViewComponent {

    private static Help instance;

    private Help() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static Help getInstance() {
        if (instance == null) {
            instance = new Help();
        }
        return instance;
    }

    @Override
    public List<JButton> getButtonsLeft() {
        List<JButton> retList = new ArrayList<>();
        JButton backButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                View.getInstance().setContent(MainMenu.getInstance());
            }
        }, "back.png", 40, 40, L10n.getString("back"));
        retList.add(backButton);
        return retList;
    }

    @Override
    public List<JComponent> getComponentsCenter() {
        List<JComponent> retList = new ArrayList<>();
        JButton icon = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Desktop.isDesktopSupported()) {
                        String s = Preferences.getInstance().projectLocation;
                        Desktop.getDesktop().browse(new URI(s.substring(0, s.lastIndexOf("/") + 1)));
                    }
                } catch (Exception e) {
                    Log.error(Help.class, e.getMessage());
                }
            }
        }, "logo.png", 48, 48);
        icon.setSelected(true);
        retList.add(icon);
        return retList;
    }

    @Override
    public List<JButton> getButtonsRight() {
        List<JButton> retList = new ArrayList<>();
        JButton exitButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                Control.getInstance().exitProgram();
            }
        }, "clear.png", 40, 40, L10n.getString("exitJaPy"));
        retList.add(exitButton);
        return retList;
    }

    @Override
    public JComponent initializeViewComponent() {
        JComponent retComponent = new JPanel();
        retComponent.setBackground(ColorStore.BACKGROUND);
        JEditorPane helpText = new JEditorPane();
        helpText.setContentType("text/html");
        helpText.setText(L10n.getString("helpText"));
        helpText.setBackground(ColorStore.BACKGROUND);
        retComponent.add(helpText);
        return retComponent;
    }

    @Override
    public void uninitializeViewComponent() {
        instance = null;
    }
}
