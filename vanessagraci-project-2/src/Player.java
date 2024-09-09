import bagel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The class for player
 */
public class Player{
    private Image image;
    private int verticalSpeed = 0;
    private final int JUMPING_MOTION = 1;
    private final int JUMP_SPEED = -20;
    private int score = 0;
    private final double RADIUS;
    private double health;
    private int xCoordinate;
    private int yCoordinate;
    private int initialY;
    private final Properties props;
    private boolean landingOnPlatform;
    private List<Fireball> fireballs;
    private boolean doubleScore = false;
    private boolean invincible = false;

    /** The constructor
     * @param xCoordinate: player's x-coordinate
     * @param yCoordinate: player's y-coordinate
     * @param props: game property file
     */
    public Player(int xCoordinate, int yCoordinate, Properties props) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.props = props;
        this.RADIUS = Double.parseDouble(props.getProperty("gameObjects.player.radius"));
        this.image = new Image(props.getProperty("gameObjects.player.imageRight"));
        this.health = Double.parseDouble(props.getProperty("gameObjects.player.health"));
        this.initialY = yCoordinate;
        fireballs = new ArrayList<>();
    }

    /** Getters to get the list of fireball shot by player
     * @return list of fireball
     */
    public List<Fireball> getFireballs() {
        return fireballs;
    }

    /** Setters to set player is landing on platform
     * @param landingOnPlatform: boolean true if player is landed on platform, false otherwise
     */
    public void setLandingOnPlatform(boolean landingOnPlatform) {
        this.landingOnPlatform = landingOnPlatform;
    }

    /** Getters for player's x-coordinate
     * @return x-coordinate of player
     */
    public int getX() {
        return xCoordinate;
    }

    /** Getters for player's y-coordinate
     * @return y-coordinate of player
     */
    public int getY() {
        return yCoordinate;
    }

    /** Getters for player's radius
     * @return radius of player
     */
    public double getRADIUS() {
        return RADIUS;
    }

    /** Getters for player's score
     * @return score
     */
    public int getScore() {
        return score;
    }

    /** Setters for the player's score
     * @param score: score to be set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /** Getters for player's health
     * @return health of player
     */
    public double getHealth() {
        return health;
    }

    /** Setters for the player's health
     * @param health: health to be set
     */
    public void setHealth(double health) {
        this.health = health;
    }


    public boolean isDoubleScore() {
        return doubleScore;
    }

    public void setDoubleScore(boolean doubleScore) {
        this.doubleScore = doubleScore;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    /** This method is to handle player's jumping behaviour
     * @param input: input keys that determine the jump
     */

    public void jump(Input input) {
        // can jump if on platform or on flying platform
        if (input.wasPressed(Keys.UP) && (yCoordinate == initialY || landingOnPlatform)){
            verticalSpeed = JUMP_SPEED;
            yCoordinate += verticalSpeed;
            landingOnPlatform = false;
        }

        // if lands on flying platform
        if (landingOnPlatform){
            verticalSpeed = 0;
        }

        // mid jump but is not landing on flying platform
        if (yCoordinate < initialY && !landingOnPlatform) {
            verticalSpeed += JUMPING_MOTION;
        }

        // finishing jump
        if (!landingOnPlatform | verticalSpeed > 0 && (yCoordinate >= initialY) && !isDead()){
            verticalSpeed = 0;
            yCoordinate = initialY;
        }

        this.yCoordinate += verticalSpeed;
    }

    /** Method that update player state, jump and movement behaviour and draw it.
     * @param input: input keys that affect some method behaviour
     * @param boss: enemy boss to shot fireball
     * @param platforms: list of flying platform
     */
    // (inspired by Project 1 solution)
    public void update(Input input, EnemyBoss boss, List<FlyingPlatform> platforms) {
        if (input.wasPressed(Keys.LEFT)) {
            image = new Image(this.props.getProperty("gameObjects.player.imageLeft"));
        }

        if (input.wasPressed(Keys.RIGHT)) {
            image = new Image(this.props.getProperty("gameObjects.player.imageRight"));
        }
        image.draw(xCoordinate, yCoordinate);

        // landing on platform checking and behaviour
        boolean landingOnAnyPlatform = false;

        for (FlyingPlatform platform : platforms) {
            if (platform.playerLanding(this)) {
                landingOnAnyPlatform = true;
                break;
            }
        }

        setLandingOnPlatform(landingOnAnyPlatform);

        // jump behaviour
        jump(input);

        // shoot fireball only for level 3 when boss is not null
        if (boss != null) {
            fireballUpdate(input, boss);
        }

        // lose movement (downwards)
        lose();

    }

    /** Method to check if player is dead
     * @return boolean true if health below or equal to 0
     */
    public boolean isDead(){return health <= 0;}

    /**
     * This method is to control player movement if dead
     */
    // method to move the player down when lose
    public void lose() {
        if (isDead()){
            if (yCoordinate < Window.getHeight()) {
                verticalSpeed = 2;
                yCoordinate += verticalSpeed;
                initialY += verticalSpeed;
            }
        }
    }

    /** This method is to check shooting condition and add fireball to fireballs list
     * @param boss: boss to check the distance between player and boss
     */
    public void shootFireball(EnemyBoss boss) {
        int distance = getX() - boss.getX();
        if (Math.abs(distance) < 500 && !boss.isDead()){
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

    /** Check the collision with fireball
     * @param fireball: fireball as the parameter
     * @return boolean true if its collided, and false if not
     */
    // check collision between player and fireball
    public boolean collision(Fireball fireball) {
        return Math.sqrt(Math.pow(fireball.getX() - getX(), 2) +
                Math.pow(fireball.getY() - getY(), 2)) <= fireball.getRADIUS() + getRADIUS();
    }

    /** This method is to update health and fireball activation status if it's collided
     * @param fireballs: list of fireball shot from boss
     */
    public void checkCollision(List<Fireball> fireballs){

        for (Fireball fireball : fireballs){

            // deactivate the fireball once it is collided
            if (fireball.isActive() && collision(fireball)){
                fireball.deactivate();

                if (!invincible) {
                    setHealth(getHealth() - fireball.getDAMAGE_SIZE());
                }
            }
        }
    }

    /** This method is to update the fireball corresponding to the player input or if shot from player
     * @param input: input keys to shoot fireball
     * @param boss: boss as target when fireball is shot
     */
    public void fireballUpdate(Input input, EnemyBoss boss){
        if (input.wasPressed(Keys.S)){
            shootFireball(boss);
        }

        for (Fireball fireball : fireballs){
            fireball.update(input, this);
        }

        checkCollision(boss.getFireballs());
    }
}
