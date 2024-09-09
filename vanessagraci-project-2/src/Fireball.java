import bagel.*;
import java.util.Properties;

/**
 * The class for fireball entity
 */
public class Fireball extends Entity {
    private final double DAMAGE_SIZE;
    private String direction;
    private int MOVE_SPEED = 5;
    private boolean active;

    /** The constructor
     * @param xCoordinate: x-coordinate of fireball entity
     * @param yCoordinate: y-coordinate of fireball entity
     * @param image: image of fireball entity
     * @param SPEED: speed of fireball entity
     * @param RADIUS: radius of fireball entity
     * @param props: game property file
     */
    public Fireball(int xCoordinate, int yCoordinate, Image image, int SPEED, double RADIUS, Properties props) {
        super(xCoordinate, yCoordinate, new Image(props.getProperty("gameObjects.fireball.image")),
                Integer.parseInt(props.getProperty("gameObjects.fireball.speed")),
                Double.parseDouble(props.getProperty("gameObjects.fireball.radius")), props);

        this.DAMAGE_SIZE = Double.parseDouble(props.getProperty("gameObjects.fireball.damageSize"));
        this.active = true;
    }

    /** Setters to set the direction of fireball's movement when shot
     * @param direction: String "left" or "right"
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /** Getters for fireball's damage_size
     * @return damage size of fireball
     */
    public double getDAMAGE_SIZE() {
        return DAMAGE_SIZE;
    }

    /** Getters to check if the fireball is active
     * @return activation status of fireball
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Method to deactivate the fireball
     */
    public void deactivate(){
        this.active = false;
    }

    // method to move fireball based on player's horizontal movement
    @Override
    protected void move(Input input) {
        if (input.isDown(Keys.RIGHT)) {
            setX(getX() - MOVE_SPEED);
        } else if (input.isDown(Keys.LEFT)) {
            setX(getX() + MOVE_SPEED);
        }
    }

    // method that update the fireball state every frame (move, draw, activation)
    @Override
    protected void update(Input input, Player target) {
        if (active) {

            if (direction.equals("left")){
                setX(getX() - getSPEED());
            }
            else if (direction.equals("right")){
                setX(getX() + getSPEED());
            }

            move(input);
            getImage().draw(getX(), getY());

            if (getX() < 0 || getX() > Window.getWidth()){
                deactivate();
            }
        }
    }
}
