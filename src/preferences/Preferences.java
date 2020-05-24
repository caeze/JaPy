package preferences;

import console.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Preferences util.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Preferences {

    private final static String ALGO = "AES";
    private final static String PW = "sadfghjksadfghjksadfghjk";

    // constants
    public static final Map<String, Locale> SUPPORTED_LOCALES = new HashMap<>();
    public static final Map<String, Integer> LOG_LEVELS = new HashMap<>();

    // application specific constants
    public Locale locale = Locale.GERMAN;
    public int logLevel = Log.LOG_LEVEL_INFO;
    public boolean showConsole = false;
    public String projectLocation = "https://drive.google.com/uc?id=1GAaJU2x5mgmQnG4FzofUiiMPbrRWVO9Y";
    public String solutionLocation = "";

    // preferences internals
    private static File preferencesFile;
    private static Preferences instance;

    private Preferences() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();

            SUPPORTED_LOCALES.put("English", Locale.ENGLISH);
            SUPPORTED_LOCALES.put("Deutsch", Locale.GERMAN);

            LOG_LEVELS.put("debug", Log.LOG_LEVEL_DEBUG);
            LOG_LEVELS.put("info", Log.LOG_LEVEL_INFO);
            LOG_LEVELS.put("warning", Log.LOG_LEVEL_WARNING);
            LOG_LEVELS.put("error", Log.LOG_LEVEL_ERROR);

            try {
                File jarFile = new File(Preferences.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                preferencesFile = new File(jarFile.getParentFile().getAbsolutePath() + File.separator + "preferences.properties");
            } catch (Exception e) {
                Log.error(Preferences.class, e.getMessage());
            }
            instance.load();
        }
        return instance;
    }

    public void persist() {
        try {
            Properties props = new Properties();
            props.setProperty("locale", locale.getLanguage());
            props.setProperty("logLevel", String.valueOf(logLevel));
            props.setProperty("showConsole", String.valueOf(showConsole));
            props.setProperty("projectLocation", projectLocation);
            props.setProperty("solutionLocation", solutionLocation);
            OutputStream out = new FileOutputStream(preferencesFile);
            props.store(out, "JaPy preferences. Do not modify!");
        } catch (Exception e) {
            Log.error(Preferences.class, e.getMessage());
        }
    }

    public void load() {
        try {
            if (!preferencesFile.exists()) {
                persist();
            }
            Properties props = new Properties();
            props.load(new FileInputStream(preferencesFile));
            String localeString = props.getProperty("locale", locale.getLanguage());
            for (Locale l : SUPPORTED_LOCALES.values()) {
                if (l.getLanguage().equals(localeString)) {
                    locale = l;
                    break;
                }
            }
            logLevel = Integer.valueOf(props.getProperty("logLevel", String.valueOf(logLevel)));
            showConsole = Boolean.valueOf(props.getProperty("showConsole", String.valueOf(showConsole)));
            projectLocation = props.getProperty("projectLocation", projectLocation);
            solutionLocation = props.getProperty("solutionLocation", solutionLocation);
        } catch (Exception e) {
            Log.error(Preferences.class, e.getMessage());
        }
    }

    public String aesEncryptToBase64(String clearText) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(PW.getBytes(), ALGO);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(clearText.getBytes());
            return byteArrayToBase64(encrypted);
        } catch (Exception e) {
            Log.error(Preferences.class, "AES encryption not working! " + e.getMessage());
        }
        return "";
    }

    public String aesDecryptToClearText(String base64Text) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(PW.getBytes(), ALGO);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] cipherData2 = cipher.doFinal(base64ToByteArray(base64Text));
            return new String(cipherData2);
        } catch (Exception e) {
            Log.error(Preferences.class, "AES encryption not working! " + e.getMessage());
        }
        return "";
    }
    
    private String byteArrayToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    private byte[] base64ToByteArray(String base64String) {
        return Base64.getDecoder().decode(base64String); 
    }
}
