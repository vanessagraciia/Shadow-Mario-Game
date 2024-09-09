import bagel.*;
import java.util.Properties;

/**
 * The class for invincible entity
 */
public class Invincible extends Power{
    private int speed_y = 0;
    private boolean isCollided = false;
    private static boolean isActive = false;
    private int remainingFrames = 0;

    /** The constructor
     * @param xCoordinate: x-coordinate of invincible entity
     * @param yCoordinate: y-coordinate of invincible entity
     * @param image: image of invincible entity
     * @param SPEED: speed of invincible entity
     * @param RADIUS: radius of invincible entity
     * @param props: game property file
     * @param MAX_FRAMES: max frames for invincible to be active
     */
    public Invincible(int xCoordinate, int yCoordinate, Image image, int SPEED, double RADIUS, Properties props, int MAX_FRAMES) {
        super(xCoordinate, yCoordinate, image, SPEED, RADIUS, props, MAX_FRAMES);
    }

    /** Getters for isActive attributes
     * @return boolean if invincible is being activated
     */
    public static boolean isActive() {
        return isActive;
    }


    // move the invincible power based on the player's movement
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
            Enemy.setInvincible(true);
            target.setInvincible(true);
            this.remainingFrames--;

            if (this.remainingFrames == 0){
                isActive = false;
                Enemy.setInvincible(false);
                target.setInvincible(false);
            }
        }
    }

    /** Check the collision with player
     * @param player: player as the parameter
     * @return boolean true if its collided, and false if not
     */
    // check collision between invincible and player (inspired by Project 1 solution)
    @Override
    public boolean collision(Player player) {
        return Math.sqrt(Math.pow(player.getX() - getX(), 2) +
                Math.pow(player.getY() - getY(), 2)) <= player.getRADIUS() + getRADIUS();
    }


    /**
     * This method is to move the invincible power upwards (used when it is collided)
     */
    @Override
    public void moveUpwards() {
        if (getY() > -100) {
            setY(getY() + speed_y);
        }
    }
}
