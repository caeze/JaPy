package python;

/**
 * Interface for callbacks for Python syntax checks.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public interface IPythonSyntaxCheckerCallback {

    /**
     * Called when the Python interpreter is already running some other script.
     */
    public void alreadyRunning();

    /**
     * Called when the Python script has no syntax errors.
     */
    public void success();

    /**
     * Called when some syntax error is in the script.
     *
     * @param error the error message
     */
    public void syntaxError(String error);

    /**
     * Called when some error occured during checking the syntax.
     * 
     * @param error the error message
     */
    public void otherError(String error);
}
