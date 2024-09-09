import bagel.*;
import java.util.Properties;

/**
 * Class for the End Flag entity
 */
public class EndFlag extends Entity implements Collideable<Player>{
    private boolean isCollided = false;

    /** The constructor
     * @param xCoordinate: x-coordinate of end flag
     * @param yCoordinate: y-coordinate of end flag
     * @param image: image of end flag
     * @param SPEED: speed of end flag
     * @param RADIUS: radius of end flag
     * @param props: game property file
     */
    public EndFlag(int xCoordinate, int yCoordinate, Image image, int SPEED, double RADIUS, Properties props) {
        super(xCoordinate, yCoordinate, image, SPEED, RADIUS, props);
    }

    // move the end flag based on the player's movement
    @Override
    protected void move(Input input) {
        if (input.isDown(Keys.RIGHT)){
            setX(getX() - getSPEED());
        } else if (input.isDown(Keys.LEFT)) {
            setX(getX() + getSPEED());
        }
    }

    // update the end flag movement, control for collision, and draw end flag (inspired by Project 1 solution).
    @Override
    protected void update(Input input, Player target) {
        move(input);
        getImage().draw(getX(), getY());
        if (collision(target) && !isCollided){
            isCollided = true;
        }
    }

    /** Check the collision with player
     * @param player: player as the parameter
     * @return boolean true if its collided, and false if not
     */
    // check collision between end flag and player (inspired by Project 1 solution)
    @Override
    public boolean collision(Player player) {
        return Math.sqrt(Math.pow(player.getX() - getX(), 2) +
                Math.pow(player.getY() - getY(), 2)) <= player.getRADIUS() + getRADIUS();
    }
}
