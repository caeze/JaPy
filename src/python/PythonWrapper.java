package python;

import console.Log;
import java.awt.Frame;
import java.io.StringWriter;
import org.python.util.InteractiveConsole;
import view.util.LoadingAnimation;

/**
 * Python interpreter wrapper utility class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class PythonWrapper {

    private static PythonWrapper instance;
    private boolean alreadyRunning = false;
    private boolean scriptResultThreadRunning = false;
    private StringWriter writer;
    private Thread scriptThread;
    private Thread scriptResultThread;
    private InteractiveConsole interactiveConsole;

    private PythonWrapper() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static PythonWrapper getInstance() {
        if (instance == null) {
            instance = new PythonWrapper();
        }
        return instance;
    }

    public void interpret(String pythonString, IPythonCallback callback) {
        if (alreadyRunning) {
            callback.alreadyRunning();
            return;
        }
        alreadyRunning = true;

        scriptThread = new Thread(new Runnable() {
            @Override
            public void run() {
                writer = new StringWriter();
                if (System.getProperty("python.home") == null) {
                    System.setProperty("python.home", "");
                }
                InteractiveConsole.initialize(System.getProperties(), null, new String[0]);
                interactiveConsole = new InteractiveConsole();
                interactiveConsole.setOut(writer);
                LoadingAnimation.killLoadingAnim();
                try {
                    String script = PythonLibConcatter.addLibs(pythonString);
                    interactiveConsole.exec(script);
                } catch (Exception e) {
                    if (e.getMessage().contains("ThreadDeath")) {
                        callback.executionCancelled();
                    } else if (e.getMessage().contains("not defined")) {
                        callback.syntaxError("Syntax error on executing Python script: " + e.getMessage());
                    } else {
                        callback.pythonRuntimeError("Python runtime error on executing Python script: " + e.getMessage());
                    }
                }
            }
        });
        scriptThread.start();

        scriptResultThreadRunning = true;
        scriptResultThread = new Thread(new Runnable() {
            String content = "";

            @Override
            public void run() {
                while (scriptResultThreadRunning) {
                    if (writer != null && !writer.getBuffer().toString().isEmpty() && !content.equals(writer.getBuffer().toString())) {
                        content = writer.getBuffer().toString();
                        callback.interpreterOutput(content);
                        writer.flush();
                    }
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        callback.pythonRuntimeError("Python runtime error on executing Python script: " + e.getMessage());
                    }
                }
            }
        });
        scriptResultThread.start();
    }

    public void cancelInterpreter() {
        if (!alreadyRunning || writer == null) {
            return;
        }
        for (Frame f : Frame.getFrames()) {
            if (!f.getClass().getName().contains("SwingUtilities") && !f.getClass().getName().contains("View")) {
                f.setVisible(false);
            }
        }
        try {
            interactiveConsole.exec("exit()");
        } catch (Exception e) {
            Log.debug(PythonWrapper.class, "Execution cancelled!");
        }
        try {
            interactiveConsole.cleanup();
            interactiveConsole.close();
            interactiveConsole = null;
            alreadyRunning = false;
            scriptResultThreadRunning = false;
            Thread.sleep(10);
        } catch (Exception e) {
            Log.error(PythonWrapper.class, "Error on exiting interpreter: " + e.getMessage());
        }
        scriptThread.stop();
        scriptResultThread.stop();
    }
}
