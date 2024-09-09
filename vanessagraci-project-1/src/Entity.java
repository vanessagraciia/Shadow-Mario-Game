//Entity class to store the different type of entity from the csv file
import bagel.*;
public class Entity {
    private String type;
    private double xCoordinate;
    private double yCoordinate;
    private boolean collide;
    private final double verticalSpeed = -10;
    private boolean active = true;

    // constructor
    public Entity(String type, double xCoordinate, double yCoordinate) {
        this.type = type;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.collide = false;
        this.active = true;
    }

    public String getType() {
        return type;
    }

    public double getX() {
        return xCoordinate;
    }

    public double getY() {
        return yCoordinate;
    }

    // to check if the enemy or coin has already been collided
    public boolean alreadyCollided() {
        return collide;
    }

    // to check if the status of entity is collided or not
    public void isCollided(boolean collide) {
        this.collide = collide;
    }

    // to check if entity still on the screen
    public boolean isActive() {
        return active;
    }

    // method to check if the entity off screen
    public void isOffScreen(){
        if (yCoordinate <= -100){
            active = false;
        } else if (yCoordinate > -100){
            active = true;
        }
    }


    // move the entity when corresponding arrow is pressed
    public void move(Input input) {
        if (input.isDown(Keys.LEFT)){
            if (getType().equals("ENEMY")) {
                xCoordinate += ShadowMario.enemySpeed;
            } else if (getType().equals("COIN")) {
                xCoordinate += ShadowMario.coinSpeed;
            } else if (getType().equals("END_FLAG")){
                xCoordinate += ShadowMario.flagSpeed;
            } else if (getType().equals("PLATFORM")) {
                if (xCoordinate < 3000) {
                    xCoordinate += ShadowMario.platformSpeed;
                }
            }
        } else if (input.isDown(Keys.RIGHT)){
            if (getType().equals("ENEMY")) {
                xCoordinate -= ShadowMario.enemySpeed;
            } else if (getType().equals("COIN")) {
                xCoordinate -= ShadowMario.coinSpeed;
            } else if (getType().equals("END_FLAG")){
                    xCoordinate -= ShadowMario.flagSpeed;
            } else if (getType().equals("PLATFORM")) {
                xCoordinate -= ShadowMario.platformSpeed;
                }
            }
        }

    // method to move the coin upwards when the player collided with the coin
    public void moveCoin() {
        if(isActive()) {
            yCoordinate += verticalSpeed;
            isOffScreen();
        }
    }
}