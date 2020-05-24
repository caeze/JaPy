package view;

import console.Log;
import control.Control;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import preferences.Preferences;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ButtonUtil;
import view.util.ColorStore;

/**
 * Main Menu screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ProjectDownloadSelector implements IViewComponent {

    private static ProjectDownloadSelector instance;
    private List<String> projectsNames = new ArrayList<>();
    private List<String> projectsFileIds = new ArrayList<>();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    private ProjectDownloadSelector() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static ProjectDownloadSelector getInstance() {
        if (instance == null) {
            instance = new ProjectDownloadSelector();
        }
        return instance;
    }

    @Override
    public List<JButton> getButtonsRight() {
        List<JButton> retList = new ArrayList<>();
        retList.add(ButtonUtil.createButton("empty.png", 40, 40));
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
                    Log.error(ProjectDownloadSelector.class, e.getMessage());
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

        for (int i = 0; i < projectsNames.size(); i++) {
            String name = projectsNames.get(i);
            String id = projectsFileIds.get(i);
            JButton b = new JButton(name);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    View.getInstance().setContent(Editor.getInstance());
                    String projectLocationDrive = "https://drive.google.com/uc?id=" + id;
                    Control.getInstance().loadProjectFromNetwork(projectLocationDrive);
                }
            });
            retComponent.add(b, constraints);
            constraints.gridy++;
        }
        return retComponent;
    }

    @Override
    public void uninitializeViewComponent() {
        instance = null;
    }

    public void addAllProjectsToDownload(String projects) {
        projects = projects.replace("\r", "");
        projects = projects.replace("\n", "");
        String[] prjs = projects.split(";");
        for (String p : prjs) {
            if (p.contains("#")) {
                continue;
            }
            try {
                String input = p.split(", ")[2];
                Date date = sdf.parse(input);
                if (date.after(today())) {
                    continue;
                }
            } catch (Exception e) {
                Log.error(ProjectDownloadSelector.class, "Could not parse date from projects: " + e.getMessage());
            }
            projectsNames.add(p.split(", ")[0]);
            projectsFileIds.add(p.split(", ")[1]);
        }
    }

    private Date today() throws Exception {
        String todayString = sdf.format(new Date());
        return sdf.parse(todayString);
    }
}
