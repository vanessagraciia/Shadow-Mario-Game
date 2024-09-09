import bagel.*;
import java.util.Properties;

/**
 * Power class (inherited by double score and invincible)
 */
public abstract class Power extends Entity implements Collideable<Player>, UpwardMovable{
    private int MAX_FRAMES;

    /** The constructor
     * @param xCoordinate: x-coordinate of power
     * @param yCoordinate: y-coordinate of power
     * @param image: image of power
     * @param SPEED: speed of power
     * @param RADIUS: radius of power
     * @param props: game property file
     * @param MAX_FRAMES: max frames for power to be active
     */
    public Power(int xCoordinate, int yCoordinate, Image image, int SPEED, double RADIUS, Properties props, int MAX_FRAMES) {
        super(xCoordinate, yCoordinate, image, SPEED, RADIUS, props);
        this.MAX_FRAMES = MAX_FRAMES;
    }

    /** Getters for maximum frames for power activation
     * @return maximum frames
     */
    public int getMAX_FRAMES() {
        return MAX_FRAMES;
    }
}
