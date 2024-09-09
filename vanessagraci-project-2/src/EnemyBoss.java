import bagel.*;
import java.util.*;

/**
 * The class for enemy boss entity
 */
public class EnemyBoss extends Entity implements Collideable<Fireball>{
    private double health = 1;
    private int frameCount = 0;
    private Properties props;
    private int loseSpeed = 2;
    private List<Fireball> fireballs;


    /** The constructor
     * @param xCoordinate: x-coordinate of enemy boss
     * @param yCoordinate: y-coordinate of enemy boss
     * @param image: image of enemy boss
     * @param SPEED: speed of enemy boss
     * @param RADIUS: radius of enemy boss
     * @param props: game property file
     */
    public EnemyBoss(int xCoordinate, int yCoordinate, Image image, int SPEED, double RADIUS, Properties props) {
        super(xCoordinate, yCoordinate, image, SPEED, RADIUS, props);
        this.props = props;

        // list to store the fireball shot
        fireballs = new ArrayList<>();
    }

    /** Getters for fireball list
     * @return list of fireball shot by boss
     */
    public List<Fireball> getFireballs() {
        return fireballs;
    }

    /** Getters for the boss' health
     * @return health of the boss
     */
    public double getHealth() {
        return health;
    }


    // move the enemy boss based on the player's movement
    @Override
    protected void move(Input input) {
        if (input.isDown(Keys.RIGHT)) {
            setX(getX() - getSPEED());
        } else if (input.isDown(Keys.LEFT)) {
            setX(getX() + getSPEED());
        }
    }

    // update the enemy boss movement, control for shooting fireball and collision, and draw enemy boss.
    @Override
    protected void update(Input input, Player target) {
        frameCount++;
        move(input);
        shootFireball(target);

        for (Fireball fireball: fireballs){
            fireball.update(input, target);
        }

        checkCollision(target.getFireballs());

        getImage().draw(getX(), getY());

        lose();

    }

    /** Check the collision with fireball
     * @param fireball: fireball as the parameter
     * @return boolean true if its collided, and false if not
     */
    // check collision between enemy boss and fireball
    public boolean collision(Fireball fireball) {
        return Math.sqrt(Math.pow(fireball.getX() - getX(), 2) +
                Math.pow(fireball.getY() - getY(), 2)) <= fireball.getRADIUS() + getRADIUS();
    }


    /** This method is to update health and fireball activation status if it's collided
     * @param fireballs: list of fireball shot from player
     */
    public void checkCollision(List<Fireball> fireballs){
        for (Fireball fireball : fireballs){
            if (fireball.isActive() && collision(fireball)){
                health -= fireball.getDAMAGE_SIZE();
                fireball.deactivate();
            }
        }
    }

    /** This method is to check shooting condition and add fireball to fireballs list
     * @param player: player to check the distance between player and boss
     */
    public void shootFireball(Player player){
        int distance = getX() - player.getX();
        if (frameCount % 100 == 0 && !isDead() &&
                Math.random() < 0.5 &&
                (Math.abs(distance) < 500)){
            Fireball newFireball = new Fireball(this.getX(), this.getY(), new Image(props.getProperty("gameObjects.fireball.image")),
                    Integer.parseInt(props.getProperty("gameObjects.fireball.speed")),
                    Double.parseDouble(props.getProperty("gameObjects.fireball.radius")), props);
            fireballs.add(newFireball);

            // if player (target) is on the left of boss, fireball should move left when it is shot
            if (distance > 0){
                newFireball.setDirection("left");
            }

            // if target is on the right, fireball should move right
            else {
                newFireball.setDirection("right");
            }
        }
    }

    /** Getters to check if enemy boss is dead
     * @return boolean true if enemy boss is dead and false otherwise
     */
    public boolean isDead(){
        return health <= 0;
    }

    /**
     * This method is to control enemy boss movement if dead
     */
    public void lose() {
        if (isDead()){
            setY(getY() + loseSpeed);
        }
    }
}
