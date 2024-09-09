import bagel.*;
import java.util.Properties;
public class Score {

    private static int score;
    private static double health;
    private double entityRadius;
    public Score(){
        score = 0;
        health = ShadowMario.healthPoint;
    }

    public int getScore(){
        return score;
    }
    public double getHealth(){
        return health;
    }

    // check if its collision or not (player with entity)
    public boolean collision(Player player, Entity entity){
        String entityType = entity.getType();
        if (entityType.equals("ENEMY")){
            entityRadius = ShadowMario.enemyRadius;
        } else if (entityType.equals("COIN")){
            entityRadius = ShadowMario.coinRadius;
        } else if (entityType.equals("END_FLAG")){
            entityRadius = ShadowMario.flagRadius;
        }

        double x = player.getX() - entity.getX();
        double y = player.getY() - entity.getY();
        double distance = Math.sqrt(x * x + y * y);

        // calculate the score and health corresponding with the collision
        double collisionRange = ShadowMario.playerRadius + entityRadius;
        if (distance <= collisionRange) {
            if (entity.getType().equals("COIN") && !entity.alreadyCollided()){
                score += ShadowMario.coinValue;
                entity.isCollided(true);
            } else if (entity.getType().equals("ENEMY") && !entity.alreadyCollided()) {
                health -= ShadowMario.damagePoint;
                entity.isCollided(true);
            }
            return true;
        }
        return false;
    }
}
