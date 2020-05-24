package view;

import console.Log;
import control.Control;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import preferences.Preferences;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ColorStore;
import view.util.LabelUtil;
import view.util.ButtonUtil;
import view.util.GenericDialog;

/**
 * Main Menu screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Settings implements IViewComponent {

    private static Settings instance;

    private Settings() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
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
                    Log.error(Settings.class, e.getMessage());
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
        retComponent.setLayout(new GridLayout(0, 1));

        JComponent settingsComponent = new JPanel();
        settingsComponent.setBackground(ColorStore.BACKGROUND);
        settingsComponent.setLayout(new GridLayout(0, 2));

        JLabel languageSettingsLabel = new JLabel(LabelUtil.styleLabel(L10n.getString("languageSettings")));
        settingsComponent.add(languageSettingsLabel);
        JComboBox languageSettings = new JComboBox();
        String selectedItem = "";
        for (String s : Preferences.SUPPORTED_LOCALES.keySet()) {
            languageSettings.addItem(s);
            if (Preferences.SUPPORTED_LOCALES.get(s).equals(Preferences.getInstance().locale)) {
                selectedItem = s;
            }
        }
        languageSettings.setSelectedItem(selectedItem);
        settingsComponent.add(languageSettings);

        settingsComponent.add(new JLabel());
        settingsComponent.add(new JLabel());
        JLabel debugLevelSettingsLabel = new JLabel(LabelUtil.styleLabel(L10n.getString("debugLevel")));
        settingsComponent.add(debugLevelSettingsLabel);
        JComboBox debugLevelSettings = new JComboBox();
        String selectedDebugLevel = "";
        for (String s : Preferences.LOG_LEVELS.keySet()) {
            debugLevelSettings.addItem(s);
            if (Preferences.LOG_LEVELS.get(s).equals(Preferences.getInstance().logLevel)) {
                selectedDebugLevel = s;
            }
        }
        debugLevelSettings.setSelectedItem(selectedDebugLevel);
        settingsComponent.add(debugLevelSettings);

        settingsComponent.add(new JLabel());
        settingsComponent.add(new JLabel());
        JLabel showConsoleSettingsLabel = new JLabel(LabelUtil.styleLabel(L10n.getString("showConsoleButRestartProgramToTakeEffect")));
        settingsComponent.add(showConsoleSettingsLabel);
        JComboBox showConsoleSettings = new JComboBox();
        showConsoleSettings.addItem("true");
        showConsoleSettings.addItem("false");
        showConsoleSettings.setSelectedItem(Preferences.getInstance().showConsole + "");
        settingsComponent.add(showConsoleSettings);

        settingsComponent.add(new JLabel());
        settingsComponent.add(new JLabel());
        JLabel projectLocationLabel = new JLabel(LabelUtil.styleLabel(L10n.getString("projectLocation")));
        settingsComponent.add(projectLocationLabel);
        JTextField projectLocation = new JTextField(Preferences.getInstance().projectLocation);
        settingsComponent.add(projectLocation);

        settingsComponent.add(new JLabel());
        settingsComponent.add(new JLabel());
        JLabel solutionLocationLabel = new JLabel(LabelUtil.styleLabel(L10n.getString("solutionLocation")));
        settingsComponent.add(solutionLocationLabel);
        JButton solutionLocation = new JButton(L10n.getString("edit"));
        settingsComponent.add(solutionLocation);
        solutionLocation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField solutionLocationTextField = new JTextField();
                GenericDialog dialog = new GenericDialog(L10n.getString("enterLocation"), Arrays.asList(solutionLocationTextField), true);
                int selection = dialog.show();
                if (selection == GenericDialog.SELECTION_OK) {
                    Preferences.getInstance().solutionLocation = Preferences.getInstance().aesEncryptToBase64(solutionLocationTextField.getText());
                }
            }
        });

        retComponent.add(settingsComponent);

        JButton okButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                for (String s : Preferences.SUPPORTED_LOCALES.keySet()) {
                    if (s.equals((String) languageSettings.getSelectedItem())) {
                        Preferences.getInstance().locale = Preferences.SUPPORTED_LOCALES.get(s);
                        break;
                    }
                }
                for (String s : Preferences.LOG_LEVELS.keySet()) {
                    if (s.equals((String) debugLevelSettings.getSelectedItem())) {
                        Preferences.getInstance().logLevel = Preferences.LOG_LEVELS.get(s);
                        break;
                    }
                }
                Preferences.getInstance().showConsole = Boolean.valueOf((String) showConsoleSettings.getSelectedItem());
                Preferences.getInstance().projectLocation = projectLocation.getText();
                Preferences.getInstance().persist();
                View.getInstance().setContent(MainMenu.getInstance());
            }
        }, "save.png", 40, 40);
        retComponent.add(okButton);

        return retComponent;
    }

    @Override
    public void uninitializeViewComponent() {
        instance = null;
    }
}
