package filehandling;

import console.Log;
import view.*;
import japy.JaPy;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * File handling util.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class FileHandlingUtil {

    private static FileHandlingUtil instance;

    private FileHandlingUtil() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static FileHandlingUtil getInstance() {
        if (instance == null) {
            instance = new FileHandlingUtil();
        }
        return instance;
    }

    public String showSaveFileSelector() {
        final JFileChooser fc = new JFileChooser(JaPy.lastOpenedProjectFile);
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("japy Documents", "japy"));
        if (fc.showSaveDialog(View.getInstance()) == JFileChooser.APPROVE_OPTION) {
            JaPy.lastOpenedProjectFile = fc.getSelectedFile().getAbsoluteFile().toString();
            return fc.getSelectedFile().getAbsoluteFile().toString();
        }
        return null;
    }

    public String showOpenFileSelector() {
        final JFileChooser fc = new JFileChooser(JaPy.lastOpenedProjectFile);
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("japy Documents", "japy"));
        if (fc.showOpenDialog(View.getInstance()) == JFileChooser.APPROVE_OPTION) {
            JaPy.lastOpenedProjectFile = fc.getSelectedFile().getAbsoluteFile().toString();
            return fc.getSelectedFile().getAbsoluteFile().toString();
        }
        return null;
    }

    public String readFileAsString(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)), Charset.forName("UTF-8"));
        } catch (Exception e) {
            Log.error(FileHandlingUtil.class, "Error reading file " + filePath + ": " + e.getMessage());
        }
        return null;
    }

    public void writeStringToFile(String filePath, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            Log.error(FileHandlingUtil.class, "Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
