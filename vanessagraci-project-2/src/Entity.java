import bagel.*;
import java.util.Properties;

/**
 * Entity class (parent class)
 */
public abstract class Entity {
    private Image image;
    private int xCoordinate;
    private int yCoordinate;
    private int SPEED;
    private double RADIUS;
    private final Properties props;

    /** The parent constructor
     * @param xCoordinate: x-coordinate of entity
     * @param yCoordinate: y-coordinate of entity
     * @param image: image of entity
     * @param SPEED: speed of entity
     * @param RADIUS: radius of entity
     * @param props: game property file
     */
    public Entity(int xCoordinate, int yCoordinate, Image image, int SPEED, double RADIUS, Properties props) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.SPEED = SPEED;
        this.RADIUS = RADIUS;
        this.image = image;
        this.props = props;
    }

    /** Getters for x-coordinate of entity
     * @return x-coordinate of entity
     */
    public int getX() {
        return xCoordinate;
    }

    /** Setters to set x-coordinate of entity
     * @param xCoordinate: new x-coordinate to be set
     */
    public void setX(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    /** Getters for y-coordinate of entity
     * @return y-coordinate of entity
     */
    public int getY() {
        return yCoordinate;
    }

    /** Setters to set y-coordinate of entity
     * @param yCoordinate: new y-coordinate to be set
     */
    public void setY(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    /** Getters for speed of entity
     * @return speed of entity
     */
    public int getSPEED() {
        return SPEED;
    }

    /** Getters for radius of entity
     * @return radius of entity
     */
    public double getRADIUS() {
        return RADIUS;
    }

    /** Getters for image of entity
     * @return image of entity
     */
    public Image getImage() {
        return image;
    }

    protected abstract void move(Input input);

    protected static void update(Input input, Player target) {

    }
}
