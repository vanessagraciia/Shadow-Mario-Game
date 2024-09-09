/**
 * This interface is for entity that 'can move' upwards
 */
public interface UpwardMovable {
    /**
     * vertical speed attribute for moving upwards
     */
    int verticalSpeed = -10;

    /**
     * Method to set the upward movement behaviour
     */
    public abstract void moveUpwards();
}
