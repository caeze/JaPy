package view;

import console.Log;
import control.Control;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import preferences.Preferences;
import python.IPythonCallback;
import python.PythonLibConcatter;
import python.PythonWrapper;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ButtonTabComponent;
import view.util.ButtonUtil;
import view.util.ColorStore;
import view.util.GenericDialog;
import view.util.LabelUtil;
import view.util.LoadingAnimation;

/**
 * Main Menu screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Editor implements IViewComponent {

    private JTabbedPane tabbedPane;

    private static Editor instance;

    private List<JTextArea> editors = new ArrayList<>();
    private JScrollPane consoleScrollPane;
    private JTextArea consoleTextArea;
    private PrintOutErrStream printOutErrStream;

    private Pattern p = Pattern.compile("\\d+");
    private boolean errorIsShowing = false;

    private Editor() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static Editor getInstance() {
        if (instance == null) {
            instance = new Editor();
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
        JButton openButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                Control.getInstance().loadProjectFromDisk();
            }
        }, "directory.png", 40, 40, L10n.getString("open"));
        retList.add(openButton);
        JButton saveButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                Control.getInstance().saveCurrentProject(false);
            }
        }, "save.png", 40, 40, L10n.getString("save"));
        retList.add(saveButton);
        JButton addNewButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                JTextField nameTextField = new JTextField();
                GenericDialog dialog = new GenericDialog(L10n.getString("enterName"), Arrays.asList(nameTextField), true);
                int selection = dialog.show();
                if (selection == GenericDialog.SELECTION_OK) {
                    String fileName = nameTextField.getText();
                    addTab(fileName, "");
                }
            }
        }, "add_new.png", 40, 40, L10n.getString("addNewFile"));
        retList.add(addNewButton);
        JButton biggerButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                for (JTextArea a : editors) {
                    Font font = a.getFont();
                    float size = font.getSize() + 1.0f;
                    a.setFont(font.deriveFont(checkBounds(size)));
                }
                Font font = consoleTextArea.getFont();
                float size = font.getSize() + 1.0f;
                consoleTextArea.setFont(font.deriveFont(checkBounds(size)));
            }
        }, "zoom_in.png", 40, 40, L10n.getString("bigger"));
        retList.add(biggerButton);
        JButton smallerButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                for (JTextArea a : editors) {
                    Font font = a.getFont();
                    float size = font.getSize() - 1.0f;
                    a.setFont(font.deriveFont(checkBounds(size)));
                }
                Font font = consoleTextArea.getFont();
                float size = font.getSize() - 1.0f;
                consoleTextArea.setFont(font.deriveFont(checkBounds(size)));
            }
        }, "zoom_out.png", 40, 40, L10n.getString("smaller"));
        retList.add(smallerButton);
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
                    Log.error(Editor.class, e.getMessage());
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
        retList.add(ButtonUtil.createButton("empty.png", 40, 40));
        retList.add(ButtonUtil.createButton("empty.png", 40, 40));
        retList.add(ButtonUtil.createButton("empty.png", 40, 40));
        JButton runButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                PythonWrapper.getInstance().cancelInterpreter();
                consoleTextArea.setBackground(ColorStore.BACKGROUND_CONSOLE);
                consoleTextArea.setText("");
                errorIsShowing = false;
                Control.getInstance().saveCurrentProject(false);
                int i = tabbedPane.getSelectedIndex();
                String pythonString = editors.get(i).getText();
                LoadingAnimation.showLoadingAnim();
                PythonWrapper.getInstance().interpret(pythonString, new IPythonCallback() {
                    @Override
                    public void interpreterOutput(String output) {
                        if (errorIsShowing) {
                            return;
                        }
                        consoleTextArea.setBackground(ColorStore.BACKGROUND_CONSOLE);
                        consoleTextArea.setText(output);
                        JScrollBar vertical = consoleScrollPane.getVerticalScrollBar();
                        vertical.setValue(vertical.getMaximum() + 500);
                    }

                    @Override
                    public void alreadyRunning() {
                        errorIsShowing = true;
                        consoleTextArea.setBackground(ColorStore.RED);
                        consoleTextArea.setText("Already running!");
                        JScrollBar vertical = consoleScrollPane.getVerticalScrollBar();
                        vertical.setValue(vertical.getMaximum() + 500);
                    }

                    @Override
                    public void syntaxError(String error) {
                        errorIsShowing = true;
                        consoleTextArea.setBackground(ColorStore.RED);
                        consoleTextArea.setText(correctLineNumbers(error));
                        JScrollBar vertical = consoleScrollPane.getVerticalScrollBar();
                        vertical.setValue(vertical.getMaximum() + 500);
                        PythonWrapper.getInstance().cancelInterpreter();
                    }

                    @Override
                    public void pythonRuntimeError(String error) {
                        errorIsShowing = true;
                        consoleTextArea.setBackground(ColorStore.RED);
                        consoleTextArea.setText(correctLineNumbers(error));
                        JScrollBar vertical = consoleScrollPane.getVerticalScrollBar();
                        vertical.setValue(vertical.getMaximum() + 500);
                        PythonWrapper.getInstance().cancelInterpreter();
                    }

                    @Override
                    public void executionCancelled() {
                        errorIsShowing = true;
                        consoleTextArea.setBackground(ColorStore.RED);
                        consoleTextArea.setText("Execution cancelled!");
                        JScrollBar vertical = consoleScrollPane.getVerticalScrollBar();
                        vertical.setValue(vertical.getMaximum() + 500);
                    }
                });
            }
        }, "play.png", 40, 40, L10n.getString("run"));
        retList.add(runButton);
        JButton cancelButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                PythonWrapper.getInstance().cancelInterpreter();
            }
        }, "block.png", 40, 40, L10n.getString("cancelRun"));
        retList.add(cancelButton);
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
        // add tabs area for text editor
        tabbedPane = getEditorArea();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        View.getInstance().setExtendedState(View.MAXIMIZED_BOTH);

        // add console area
        JComponent consoleArea = getConsoleArea();

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel retComponent = new JPanel(gridBagLayout);
        retComponent.setBackground(ColorStore.GREEN);
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, consoleArea);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation((int) (View.getInstance().getContentViewSize().getHeight() * 0.75));
        View.getInstance().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                splitPane.setDividerLocation((int) (View.getInstance().getContentViewSize().getHeight() * 0.75));
            }
        });
        retComponent.add(splitPane, constraints);

        return retComponent;
    }

    @Override
    public void uninitializeViewComponent() {
        instance = null;
    }

    public void addTab(String title, String fileContent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorStore.BACKGROUND_VERY_LIGHT);

        GridBagConstraints constraints = new GridBagConstraints();
        JPanel content = new JPanel(new GridBagLayout());

        RSyntaxTextArea editorTextArea = new RSyntaxTextArea(20, 60);
        Font font = consoleTextArea.getFont();
        float size = font.getSize() - 1.0f;
        editorTextArea.setFont(font.deriveFont(checkBounds(size)));
        editors.add(editorTextArea);
        editorTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
        editorTextArea.setTabsEmulated(true);
        editorTextArea.setTabSize(4);
        editorTextArea.setCodeFoldingEnabled(true);
        editorTextArea.setAntiAliasingEnabled(true);
        editorTextArea.setText(fileContent);
        RTextScrollPane sp = new RTextScrollPane(editorTextArea);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        content.add(sp, constraints);
        panel.add(content);
        tabbedPane.addTab(title, null, panel);
        tabbedPane.setTabComponentAt(editors.size() - 1, new ButtonTabComponent(tabbedPane));
        tabbedPane.setSelectedIndex(editors.size() - 1);
        tabbedPane.invalidate();
        tabbedPane.repaint();
    }

    private void removeAllTabs() {
        if (tabbedPane != null) {
            tabbedPane.removeAll();
        }
    }

    private JTabbedPane getEditorArea() {
        JTabbedPane t = new JTabbedPane();
        t.setBackground(ColorStore.RED);
        return t;
    }

    private JComponent getConsoleArea() {
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel content = new JPanel(new GridBagLayout());

        consoleTextArea = new JTextArea();
        consoleTextArea.setColumns(20);
        consoleTextArea.setRows(5);
        consoleTextArea.setEditable(false);
        consoleTextArea.setBackground(ColorStore.BACKGROUND_CONSOLE);
        consoleTextArea.setText("Console ready\n-------------");
        consoleTextArea.setCaretColor(ColorStore.FOREGROUND_CONSOLE);
        consoleTextArea.setForeground(ColorStore.FOREGROUND_CONSOLE);
        printOutErrStream = new Editor.PrintOutErrStream();
        System.setErr(new PrintStream(printOutErrStream, true));

        consoleScrollPane = new JScrollPane();
        consoleScrollPane.setViewportView(consoleTextArea);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        content.add(consoleScrollPane, constraints);

        return content;
    }

    public List<String> getEditorTitles() {
        List<String> retList = new ArrayList<>();
        for (int i = 0; i < editors.size(); i++) {
            retList.add(tabbedPane.getTitleAt(i));
        }
        return retList;
    }

    public List<String> getEditorContents() {
        List<String> retList = new ArrayList<>();
        for (JTextArea editor : editors) {
            retList.add(editor.getText());
        }
        return retList;
    }

    public void setEditorContents(List<String> titles, List<String> contents) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                removeAllTabs();
                for (int i = 0; i < titles.size(); i++) {
                    addTab(titles.get(i), contents.get(i));
                }
                if (tabbedPane.getTabCount() > 0) {
                    tabbedPane.setSelectedIndex(0);
                }
            }
        });
    }

    public boolean editorHasChanges() {
        //TODO fix
        return !editors.isEmpty();
    }

    public void clearEditors() {
        editors.clear();
    }

    public void closeTab(int index) {
        GenericDialog dialog = new GenericDialog(L10n.getString("reallyClose"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("reallyClose")))));
        int selection = dialog.show();
        if (selection == GenericDialog.SELECTION_OK) {
            tabbedPane.remove(index);
            editors.remove(index);
        }
    }

    private class PrintOutErrStream extends ByteArrayOutputStream {

        public void clear() {
            consoleTextArea.setText("");
        }

        public void flush() throws IOException {
            synchronized (this) {
                super.flush();
                String outputStr = this.toString();
                outputStr = outputStr.replace("Exception in thread \"AWT-EventQueue-0\" ", "");
                outputStr = outputStr.replace("Traceback (most recent call last):\r\n", "");
                if (outputStr.contains("java")) {
                    outputStr = "";
                }
                super.reset();
                if (!outputStr.isEmpty()) {
                    String str = "";
                    String[] lines = null;
                    if (outputStr.contains("\r\n")) {
                        lines = outputStr.split("\r\n");
                    } else {
                        lines = new String[1];
                        lines[0] = outputStr;
                    }
                    for (String l : lines) {
                        str += correctLineNumbers(l);
                    }
                    consoleTextArea.append(str);
                    consoleTextArea.setBackground(ColorStore.RED);
                    JScrollBar vertical = consoleScrollPane.getVerticalScrollBar();
                    vertical.setValue(vertical.getMaximum() + 500);
                }
            }
        }
    }

    private String correctLineNumbers(String l) {
        if (l.contains(", line ")) {
            Matcher m = p.matcher(l);
            while (m.find()) {
                String numberString = m.group();
                int number = Integer.parseInt(numberString);
                number -= PythonLibConcatter.getNumberOfRowsAddedLast();
                if (number >= 0) {
                    String strToAdd = l.replace(numberString, number + "");
                    return strToAdd + "\r\n";
                } else {
                    return "";
                }
            }
        }
        return l + "\r\n";
    }

    private float checkBounds(float input) {
        if (input < 5.0f) {
            return 5.0f;
        }
        if (input > 50.0f) {
            return 50.0f;
        }
        return input;
    }
}
