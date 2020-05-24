package view.util;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * Dialog utility class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class GenericDialog {

    public static int SELECTION_OK = 0;
    public static int SELECTION_CANCEL = 0;

    private String title;
    private List<JComponent> components;
    private boolean suppressCancelButton;
    private List<String> customOptions;

    public GenericDialog(String title, List<JComponent> components) {
        this(title, components, false, new ArrayList<>());
    }

    public GenericDialog(String title, List<JComponent> components, boolean suppressCancelButton) {
        this(title, components, suppressCancelButton, new ArrayList<>());
    }

    public GenericDialog(String title, List<JComponent> components, List<String> customOptions) {
        this(title, components, false, customOptions);
    }

    public GenericDialog(String title, List<JComponent> components, boolean suppressCancelButton, List<String> customOptions) {
        this.title = title;
        this.components = components;
        this.suppressCancelButton = suppressCancelButton;
        this.customOptions = customOptions;
    }

    public int show() {
        if (!customOptions.isEmpty()) {
            String[] options = new String[customOptions.size()];
            for (int i = 0; i < customOptions.size(); i++) {
                options[i] = customOptions.get(i);
            }
            return JOptionPane.showOptionDialog(null, components.toArray(), title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[customOptions.size() - 1]);
        } else {
            if (!suppressCancelButton) {
                return JOptionPane.showOptionDialog(null, components.toArray(), title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            }
            return JOptionPane.showOptionDialog(null, components.toArray(), title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        }
    }
}
