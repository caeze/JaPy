package view.l10n;

import console.Log;
import java.io.InputStream;
import java.util.Properties;
import preferences.Preferences;

/**
 * L10n functionality for the application.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class L10n {

    public static String getString(String key) {
        try {
            InputStream input = L10n.class.getResource(Preferences.getInstance().locale.getLanguage() + ".properties").openStream();
            Properties prop = new Properties();
            prop.load(input);
            String value = prop.getProperty(key);
            if (value != null) {
                return value;
            }
            return "!" + key + "!";
        } catch (Exception e) {
            Log.error(L10n.class, e.getMessage());
        }
        return "!" + key + "!";
    }
}
