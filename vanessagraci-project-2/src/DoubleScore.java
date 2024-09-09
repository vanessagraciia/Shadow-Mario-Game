import bagel.*;
import java.util.Properties;

/**
 * Class for the DoubleScore entity
 */
public class DoubleScore extends Power{
    private int speed_y = 0;
    private boolean isCollided = false;
    private static boolean isActive = false;
    private int remainingFrames = 0;

    /** The constructor
     * @param xCoordinate: x-coordinate of double score entity
     * @param yCoordinate: y-coordinate of double score entity
     * @param image: image of double score entity
     * @param SPEED: speed of double score entity
     * @param RADIUS: radius of double score entity
     * @param props: game property files
     * @param MAX_FRAMES: max frames for double score to be active
     */
    public DoubleScore(int xCoordinate, int yCoordinate, Image image, int SPEED, double RADIUS, Properties props, int MAX_FRAMES) {
        super(xCoordinate, yCoordinate, image, SPEED, RADIUS, props, MAX_FRAMES);
    }

    /** Getters for isActive attributes
     * @return boolean if double score is being activated
     */
    public static boolean isActive() {
        return isActive;
    }

    // move the double score power based on the player's movement
    @Override
    protected void move(Input input) {
        if (input.isDown(Keys.RIGHT)) {
            setX(getX() - getSPEED());
        } else if (input.isDown(Keys.LEFT)) {
            setX(getX() + getSPEED());
        }
    }

    // update the power movement, control for collision, activation, frames, and draw power.
    @Override
    protected void update(Input input, Player target) {
        move(input);
        getImage().draw(getX(), getY());
        moveUpwards();
        if (collision(target) && !isActive()){
            speed_y = verticalSpeed;
            isActive = true;
            this.remainingFrames = getMAX_FRAMES();
        }

        if (this.remainingFrames > 0){
            Coin.setDoubleValue(true);
            this.remainingFrames--;

            if (this.remainingFrames == 0){
                isActive = false;
                Coin.setDoubleValue(false);
            }
        }
    }


    /** Check the collision with player
     * @param player: player as the parameter
     * @return boolean true if its collided, and false if not
     */
    // check collision between double score and player (inspired by Project 1 solution)
    @Override
    public boolean collision(Player player) {
        return Math.sqrt(Math.pow(player.getX() - getX(), 2) +
                Math.pow(player.getY() - getY(), 2)) <= player.getRADIUS() + getRADIUS();
    }

    /**
     * This method is to move the double score power upwards (used when it is collided)
     */
    @Override
    public void moveUpwards() {
        if (getY() > -100) {
            setY(getY() + speed_y);
        }
    }
}
