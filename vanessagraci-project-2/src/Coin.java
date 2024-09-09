import bagel.*;
import java.util.Properties;

/**
 * Class for the coin entity.
 */
public class Coin extends Entity implements Collideable<Player>, UpwardMovable {
    private final int VALUE;
    private boolean isCollided = false;
    private int speed_y = 0;
    private static boolean doubleValue;

    /** The constructor
     * @param xCoordinate: x-coordinate of the coin
     * @param yCoordinate: y-coordinate of the coin
     * @param image: image of coin
     * @param SPEED: speed of coin
     * @param RADIUS: radius of coin
     * @param props: game property file
     */
    public Coin(int xCoordinate, int yCoordinate, Image image, int SPEED, double RADIUS, Properties props) {
        super(xCoordinate, yCoordinate, image, SPEED, RADIUS, props);
        this.VALUE = Integer.parseInt(props.getProperty("gameObjects.coin.value"));
    }

    /** Method to check if double score is collected
     * @param doubleValue
     */
    public static void setDoubleValue(boolean doubleValue) {
        Coin.doubleValue = doubleValue;
    }

    // move the coin based on the player's movement
    @Override
    protected void move(Input input) {
        if (input.isDown(Keys.RIGHT)) {
            setX(getX() - getSPEED());
        } else if (input.isDown(Keys.LEFT)) {
            setX(getX() + getSPEED());
        }
    }

    @Override
    protected void update(Input input, Player target) {
        move(input);
        getImage().draw(getX(), getY());
        moveUpwards();
        if (collision(target) && !isCollided) {
            if (!doubleValue) {
                target.setScore(target.getScore() + VALUE);
            }
            else {
                target.setScore(target.getScore() + (2*VALUE));
            }
            isCollided = true;
            speed_y = verticalSpeed;
        }
    }

    /** Check the collision with player
     * @param player: player as the parameter
     * @return boolean true if its collided, and false if not
     */
    // check collision between coin and player (inspired by Project 1 solution)
    @Override
    public boolean collision(Player player) {
        return Math.sqrt(Math.pow(player.getX() - getX(), 2) +
                Math.pow(player.getY() - getY(), 2)) <= player.getRADIUS() + getRADIUS();
    }


    /**
     * This method is to move the coin upwards (used when it is collided)
     */
    @Override
    public void moveUpwards() {
        if (getY() > -100) {
            setY(getY() + speed_y);
        }
    }
}