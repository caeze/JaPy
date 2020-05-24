package python;

/**
 * Interface for callbacks from Python interpreter.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public interface IPythonCallback {

    /**
     * Called when the Python interpreter produces a new output.
     *
     * @param output the complete interpreter output
     */
    public void interpreterOutput(String output);

    /**
     * Called when the Python interpreter is already running some other script.
     */
    public void alreadyRunning();

    /**
     * Called when some syntax error occured.
     * 
     * @param error the error message
     */
    public void syntaxError(String error);

    /**
     * Called when some Python runtime error occured.
     * 
     * @param error the error message
     */
    public void pythonRuntimeError(String error);

    /**
     * Called when the execution thread got cancelled and is shut down.
     */
    public void executionCancelled();
}
