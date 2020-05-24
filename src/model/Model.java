package model;

/**
 * Control of the MVC pattern.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Model {

    private static Model instance;

    private Model() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
}
