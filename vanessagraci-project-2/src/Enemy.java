import bagel.*;
import java.util.Properties;

/**
 * The class of enemy entity
 */
public class Enemy extends Entity implements Collideable<Player>, RandomMovable{
    private final double DAMAGE_SIZE;
    private final int RANDOM_DISPLACEMENT;
    private final int RANDOM_SPEED;
    private boolean isCollided = false;
    private int displacement = 0;
    private boolean movingRight;
    private static boolean invincible;

    /** The constructor
     * @param xCoordinate: x-coordinate of enemy
     * @param yCoordinate: y-coordinate of enemy
     * @param image: image of enemy
     * @param SPEED: speed of enemy
     * @param RADIUS: radius of enemy
     * @param props: game property file
     */
    public Enemy(int xCoordinate, int yCoordinate, Image image, int SPEED, double RADIUS, Properties props) {
        super(xCoordinate, yCoordinate, image, SPEED, RADIUS, props);
        this.DAMAGE_SIZE = Double.parseDouble(props.getProperty("gameObjects.enemy.damageSize"));
        this.RANDOM_DISPLACEMENT = Integer.parseInt(props.getProperty("gameObjects.enemy.maxRandomDisplacementX"));
        this.RANDOM_SPEED = Integer.parseInt(props.getProperty("gameObjects.enemy.randomSpeed"));

        // randomly choose the initial direction of enemy
        this.movingRight = Math.random() < 0.5;
    }

    /** Method to check if invincible is collected
     * @param invincible: true if collected
     */
    public static void setInvincible(boolean invincible) {
        Enemy.invincible = invincible;
    }


    // move the enemy based on the player's movement
    @Override
    protected void move(Input input) {
        if (input.isDown(Keys.RIGHT)){
            setX(getX() - getSPEED());
        } else if (input.isDown(Keys.LEFT)) {
            setX(getX() + getSPEED());
        }
    }

    // update the enemy movement, control for collision, and draw enemy (inspired by Project 1 solution).
    @Override
    protected void update(Input input, Player target) {
        move(input);
        randomMove();
        getImage().draw(getX(), getY());
        if (collision(target) && !isCollided && !invincible){
            target.setHealth(target.getHealth() - DAMAGE_SIZE);
            isCollided = true;
        }
    }
    /** Check the collision with player
     * @param player: player as the parameter
     * @return boolean true if its collided, and false if not
     */
    // check collision between enemy and player (inspired by Project 1 solution)
    @Override
    public boolean collision(Player player) {
        return Math.sqrt(Math.pow(player.getX() - getX(), 2) +
                Math.pow(player.getY() - getY(), 2)) <= player.getRADIUS() + getRADIUS();
    }

    /**
     * This method is to move the enemy randomly
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
}