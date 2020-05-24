package view.itf;

import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * Interface for view components.
 *
 * @author Clemens Strobel
 * @date 04.03.2018
 */
public interface IViewComponent {

    public List<JButton> getButtonsLeft();

    public List<JComponent> getComponentsCenter();

    public List<JButton> getButtonsRight();

    public JComponent initializeViewComponent();

    public void uninitializeViewComponent();
}
