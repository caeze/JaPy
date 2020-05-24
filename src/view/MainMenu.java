package view;

import console.Log;
import control.Control;
import filehandling.HttpFileUtil;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import model.Project;
import model.ProjectJsonConverter;
import preferences.Preferences;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ButtonUtil;
import view.util.ColorStore;
import view.util.LoadingAnimation;

/**
 * Main Menu screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class MainMenu implements IViewComponent {

    private static MainMenu instance;

    private MainMenu() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static MainMenu getInstance() {
        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }

    @Override
    public List<JButton> getButtonsRight() {
        List<JButton> retList = new ArrayList<>();
        JButton helpMenuButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                View.getInstance().setContent(Help.getInstance());
            }
        }, "help.png", 40, 40, L10n.getString("help"));
        retList.add(helpMenuButton);
        JButton settingsMenuButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                View.getInstance().setContent(Settings.getInstance());
            }
        }, "preferences.png", 40, 40, L10n.getString("preferences"));
        retList.add(settingsMenuButton);
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
                    Log.error(MainMenu.class, e.getMessage());
                }
            }
        }, "logo.png", 48, 48);
        icon.setSelected(true);
        retList.add(icon);
        return retList;
    }

    @Override
    public List<JButton> getButtonsLeft() {
        List<JButton> retList = new ArrayList<>();
        retList.add(ButtonUtil.createButton("empty.png", 40, 40));
        retList.add(ButtonUtil.createButton("empty.png", 40, 40));
        retList.add(ButtonUtil.createButton("empty.png", 40, 40));
        return retList;
    }

    @Override
    public JComponent initializeViewComponent() {
        JComponent retComponent = new JPanel();
        retComponent.setBackground(ColorStore.BACKGROUND);
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        retComponent.setLayout(gridBagLayout);

        constraints.weightx = 0;
        constraints.weighty = 0.3333;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.CENTER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.ipadx = 0;
        constraints.ipady = 0;

        JButton emptyProjectButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                View.getInstance().setContent(Editor.getInstance());
                Control.getInstance().loadEmptyProject();
            }
        }, "emptyProject.png", 256, 64);
        retComponent.add(emptyProjectButton, constraints);

        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        JButton loadProjectButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                View.getInstance().setContent(Editor.getInstance());
                Control.getInstance().loadProjectFromDisk();
            }
        }, "loadProject.png", 256, 64);
        retComponent.add(loadProjectButton, constraints);

        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        JButton downloadProjectButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                LoadingAnimation.showLoadingAnim();
                HttpFileUtil.getInstance().getFileViaHttp(Preferences.getInstance().projectLocation, new HttpFileUtil.IHttpCallback() {
                    @Override
                    public void success(String data) {
                        LoadingAnimation.killLoadingAnim();
                        ProjectDownloadSelector.getInstance().addAllProjectsToDownload(data);
                        View.getInstance().setContent(ProjectDownloadSelector.getInstance());
                    }

                    @Override
                    public void failure(String data) {
                        Log.error(Control.class, "Could not load project from network: " + data);
                        LoadingAnimation.killLoadingAnim();
                    }
                });
            }
        }, "downloadProject.png", 256, 64);
        retComponent.add(downloadProjectButton, constraints);

        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        JButton downloadSolutionButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                LoadingAnimation.showLoadingAnim();
                HttpFileUtil.getInstance().getFileViaHttp(Preferences.getInstance().aesDecryptToClearText(Preferences.getInstance().solutionLocation), new HttpFileUtil.IHttpCallback() {
                    @Override
                    public void success(String data) {
                        LoadingAnimation.killLoadingAnim();
                        ProjectDownloadSelector.getInstance().addAllProjectsToDownload(data);
                        View.getInstance().setContent(ProjectDownloadSelector.getInstance());
                    }

                    @Override
                    public void failure(String data) {
                        Log.error(Control.class, "Could not load project from network: " + data);
                        LoadingAnimation.killLoadingAnim();
                    }
                });
            }
        }, "downloadSolution.png", 256, 64);
        retComponent.add(downloadSolutionButton, constraints);
        return retComponent;
    }

    @Override
    public void uninitializeViewComponent() {
        instance = null;
    }
}
