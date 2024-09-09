import bagel.*;
import java.util.Properties;

/**
 * The class for platform entity
 */
public class Platform extends Entity{
    private final int MAX_X = 3000;

    /**
     * @param xCoordinate: x-coordinate for platform
     * @param yCoordinate: y-coordinate for platform
     * @param image: image for platform
     * @param SPEED: speed for platform
     * @param RADIUS: radius of platform (0)
     * @param props: game property file
     */
    public Platform(int xCoordinate, int yCoordinate, Image image, int SPEED, double RADIUS, Properties props) {
        super(xCoordinate, yCoordinate, image, SPEED, RADIUS, props);
    }

    // move the platform based on the player's movement
    @Override
    protected void move(Input input) {
        if (input.isDown(Keys.RIGHT)){
            setX(getX() - getSPEED());
        }

        else if (input.isDown(Keys.LEFT)){
            if (getX() < MAX_X){
                setX(getX() + getSPEED());
            }
        }
    }

    // update the platform movement and draw platform.
    @Override
    protected void update(Input input, Player target) {
        move(input);
        getImage().draw(getX(), getY());
    }
}
