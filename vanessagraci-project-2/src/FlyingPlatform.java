import bagel.*;
import java.util.Properties;

/**
 * The class of flying platform entity
 */
public class FlyingPlatform extends Entity implements RandomMovable {
    private final int RANDOM_DISPLACEMENT;
    private final int HALF_LENGTH;
    private final int HALF_HEIGHT;
    private final int RANDOM_SPEED;
    private int displacement = 0;
    private boolean movingRight;

    /** The constructor
     * @param xCoordinate: x-coordinate of flying platform entity
     * @param yCoordinate: y-coordinate of flying platform entity
     * @param image: image of flying platform entity
     * @param SPEED: speed of flying platform entity
     * @param RADIUS: radius of flying platform entity
     * @param props: game property file
     */
    public FlyingPlatform(int xCoordinate, int yCoordinate, Image image, int SPEED, double RADIUS, Properties props) {
        super(xCoordinate, yCoordinate, image, SPEED, RADIUS, props);
        this.RANDOM_DISPLACEMENT = Integer.parseInt(props.getProperty("gameObjects.flyingPlatform.maxRandomDisplacementX"));
        this.HALF_LENGTH = Integer.parseInt(props.getProperty("gameObjects.flyingPlatform.halfLength"));
        this.HALF_HEIGHT = Integer.parseInt(props.getProperty("gameObjects.flyingPlatform.halfHeight"));
        this.RANDOM_SPEED = Integer.parseInt(props.getProperty("gameObjects.flyingPlatform.randomSpeed"));
        // randomly choose the initial direction of flying platform
        this.movingRight = Math.random() < 0.5;
    }

    // move the flying platform based on the player's movement
    @Override
    protected void move(Input input) {
        if (input.isDown(Keys.RIGHT)) {
            setX(getX() - getSPEED());
        } else if (input.isDown(Keys.LEFT)) {
            setX(getX() + getSPEED());
        }
    }

    // update the flying platform movement, random movement, and draw flying platform (inspired by Project 1 solution).
    @Override
    protected void update(Input input, Player target) {
        move(input);
        getImage().draw(getX(), getY());
        randomMove();
    }

    /**
     * This method is to set the random movement behaviour
     */
    @Override
    public void randomMove() {
        if (movingRight){
            setX(getX() + RANDOM_SPEED);
            displacement++;
        }
        else {
            setX(getX() - RANDOM_SPEED);
            displacement--;
        }

        // reverse the random movement direction if the displacement has reached its maximum displacement
        if (Math.abs(displacement) >= RANDOM_DISPLACEMENT){
            movingRight = !movingRight;
        }
    }

    /** This method is to check whether the player has met the condition to land on flying platform
     * @param player: player as the parameter
     * @return boolean true if the condition is met, false is not
     */
    // check the condition whether the player lands on the flying platform
    public boolean playerLanding(Player player) {
        return (Math.abs(player.getX() - getX()) < HALF_LENGTH &&
                getY() - player.getY() <= HALF_HEIGHT &&
                getY() - player.getY() >= HALF_HEIGHT - 1);
    }
}
