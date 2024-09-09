import bagel.*;

public class Player {
    private Image PLAYER_LEFT;
    private Image PLAYER_RIGHT;
    private double xCoordinate;
    private double yCoordinate;
    private final double JUMP_SPEED = -20.0;
    private final double JUMPING_MOTION = 1.0;
    private final double lost_speed = 2.0;
    private double verticalSpeed;
    private boolean isJumping;
    private double playerY;

    // constructor
    public Player(double xCoordinate, double yCoordinate){
        this.PLAYER_LEFT = new Image("res/player_left.png");
        this.PLAYER_RIGHT = new Image("res/player_right.png");
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.verticalSpeed = 0;
        this.isJumping = false;
        this.playerY = ShadowMario.playerY;
    }

    public double getX(){
        return xCoordinate;
    }

    public double getY(){
        return yCoordinate;
    }

    // draw the player's image (if left keys is down, the player will face left, vice versa)
    public void draw(Input input) {
        if (input.isDown(Keys.LEFT)){
            PLAYER_LEFT.draw(xCoordinate, yCoordinate);
        } else {
            PLAYER_RIGHT.draw(xCoordinate, yCoordinate);
        }
    }

    // handle the player jump behaviour
    public void update(Input input){
        if (input.isDown(Keys.UP) && !isJumping){
            verticalSpeed = JUMP_SPEED;
            isJumping = true;
        }

        if (isJumping){
            verticalSpeed += JUMPING_MOTION;
        }

        yCoordinate += verticalSpeed;

        if (yCoordinate >= playerY){
            yCoordinate = playerY;
            verticalSpeed = 0;
            isJumping = false;
        }
    }

    // the method for player down movement if he loses
    public void lose(){
        if (yCoordinate < Window.getHeight()) {
            yCoordinate += lost_speed;
            playerY += lost_speed;
        }
    }
}
