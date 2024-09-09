import bagel.*;
import bagel.util.Colour;
import java.util.*;

/**
 * The class for level in the game
 */
public class Level {
    private final String SCORE_TITLE;
    private final int SCORE_X;
    private final int SCORE_Y;
    private final Font SCORE_FONT;
    private final String HEALTH_TITLE;
    private final int PLAYER_HEALTH_X;
    private final int PLAYER_HEALTH_Y;
    private final Font PLAYER_HEALTH_FONT;
    private final int ENEMY_HEALTH_X;
    private final int ENEMY_HEALTH_Y;
    private final Font ENEMY_HEALTH_FONT;
    private final Properties message_props;
    private int levelNumber;
    private final Properties game_props;
    private Player player;
    private List<Entity> entities = new ArrayList<>();
    private boolean finished = false;
    private DrawOptions drawOptions;
    private final String FONT_FILE;
    private String filepath;
    private boolean win = false;


    /** The constructor
     * @param levelNumber: level number selected by player
     * @param game_props: game property file
     * @param message_props: message property file
     */
    public Level(int levelNumber, Properties game_props, Properties message_props) {
        this.levelNumber = levelNumber;
        this.game_props = game_props;
        this.message_props = message_props;
        drawOptions = new DrawOptions().setBlendColour(Colour.RED);

        FONT_FILE = game_props.getProperty("font");

        SCORE_TITLE = message_props.getProperty("score");
        SCORE_X = Integer.parseInt(game_props.getProperty("score.x"));
        SCORE_Y = Integer.parseInt(game_props.getProperty("score.y"));
        SCORE_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("score.fontSize")));

        HEALTH_TITLE = message_props.getProperty("health");
        PLAYER_HEALTH_X = Integer.parseInt(game_props.getProperty("playerHealth.x"));
        PLAYER_HEALTH_Y = Integer.parseInt(game_props.getProperty("playerHealth.y"));
        PLAYER_HEALTH_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("playerHealth.fontSize")));
        ENEMY_HEALTH_X = Integer.parseInt(game_props.getProperty("enemyBossHealth.x"));
        ENEMY_HEALTH_Y = Integer.parseInt(game_props.getProperty("enemyBossHealth.y"));
        ENEMY_HEALTH_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("enemyBossHealth.fontSize")));

        loadLevel();
    }

    /**
     * This method is to load the entity based on level
     */
    public void loadLevel() {
        switch (levelNumber) {
            case 1:
                filepath = game_props.getProperty("level1File");
                break;
            case 2:
                filepath = game_props.getProperty("level2File");
                break;
            case 3:
                filepath = game_props.getProperty("level3File");
                break;
        }
        List<String[]> data = IOUtils.readCsv(filepath);
        for (String[] entity : data) {
            int x = Integer.parseInt(entity[1]);
            int y = Integer.parseInt(entity[2]);

            if (entity[0].equals("PLAYER")) {
                player = new Player(x, y, this.game_props);
            } else if (entity[0].equals("PLATFORM")) {
                Platform platform = new Platform(x, y, new Image(this.game_props.getProperty("gameObjects.platform.image")),
                        Integer.parseInt(this.game_props.getProperty("gameObjects.platform.speed")), 0, this.game_props);
                entities.add(platform);
            } else if (entity[0].equals("ENEMY")) {
                Enemy enemy = new Enemy(x, y, new Image(this.game_props.getProperty("gameObjects.enemy.image")),
                        Integer.parseInt(this.game_props.getProperty("gameObjects.enemy.speed")),
                        Double.parseDouble(this.game_props.getProperty("gameObjects.enemy.radius")), this.game_props);
                entities.add(enemy);
            } else if (entity[0].equals("COIN")) {
                Coin coin = new Coin(x, y, new Image(this.game_props.getProperty("gameObjects.coin.image")),
                        Integer.parseInt(this.game_props.getProperty("gameObjects.coin.speed")),
                        Double.parseDouble(this.game_props.getProperty("gameObjects.coin.radius")), this.game_props);
                entities.add(coin);
            } else if (entity[0].equals("END_FLAG")) {
                EndFlag endFlag = new EndFlag(x, y, new Image(this.game_props.getProperty("gameObjects.endFlag.image")),
                        Integer.parseInt(this.game_props.getProperty("gameObjects.endFlag.speed")),
                        Double.parseDouble(this.game_props.getProperty("gameObjects.endFlag.radius")), this.game_props);
                entities.add(endFlag);
            } else if (entity[0].equals("FLYING_PLATFORM")) {
                FlyingPlatform flyingPlatform = new FlyingPlatform(x, y,
                        new Image(this.game_props.getProperty("gameObjects.flyingPlatform.image")),
                        Integer.parseInt(this.game_props.getProperty("gameObjects.flyingPlatform.speed")), 0,
                        this.game_props);
                entities.add(flyingPlatform);
            } else if (entity[0].equals("DOUBLE_SCORE")) {
                DoubleScore doubleScore = new DoubleScore(x, y,
                        new Image(this.game_props.getProperty("gameObjects.doubleScore.image")),
                        Integer.parseInt(this.game_props.getProperty("gameObjects.doubleScore.speed")),
                        Double.parseDouble(this.game_props.getProperty("gameObjects.doubleScore.radius")),
                        this.game_props, Integer.parseInt(game_props.getProperty("gameObjects.doubleScore.maxFrames")));
                entities.add(doubleScore);
            } else if (entity[0].equals("INVINCIBLE_POWER")) {
                Invincible invincible = new Invincible(x, y,
                        new Image(this.game_props.getProperty("gameObjects.invinciblePower.image")),
                        Integer.parseInt(this.game_props.getProperty("gameObjects.invinciblePower.speed")),
                        Double.parseDouble(this.game_props.getProperty("gameObjects.invinciblePower.radius")),
                        this.game_props, Integer.parseInt(game_props.getProperty("gameObjects.invinciblePower.maxFrames")));
                entities.add(invincible);
            } else if (entity[0].equals("ENEMY_BOSS")) {
                EnemyBoss enemyBoss = new EnemyBoss(x, y,
                        new Image(this.game_props.getProperty("gameObjects.enemyBoss.image")),
                        Integer.parseInt(this.game_props.getProperty("gameObjects.enemyBoss.speed")),
                        Double.parseDouble(this.game_props.getProperty("gameObjects.enemyBoss.radius")),
                        this.game_props);
                entities.add(enemyBoss);
            }
        }
    }

    /** Method that updates the game objects each frame, when the game is running.
     * @param input: input keys that affect some of the method
     */
    public void updateGameObjects(Input input) {

        EnemyBoss boss = null;
        List<FlyingPlatform> platform = new ArrayList<>();

        for (Entity entity : entities) {

            entity.update(input, player);

            if (entity instanceof EnemyBoss) {
                boss = (EnemyBoss) entity;
            }

            if (entity instanceof FlyingPlatform){
                platform.add((FlyingPlatform) entity);
            }

            if (entity instanceof EndFlag) {
                EndFlag endFlag = (EndFlag) entity;

                if (levelNumber != 3){
                    if (endFlag.collision(player)){
                        win = true;
                        finished = true;
                    }
                }

                else {
                    if (boss.getY() >= Window.getHeight() && endFlag.collision(player)) {
                        win = true;
                        finished = true;
                    }
                }
            }
        }

        player.update(input, boss, platform);

        if (player.getY() >= Window.getHeight()){
            finished = true;
        }
    }

    /** This method is used when the game is running
     * @param input: input keys that affect the update method
     */
    public void run(Input input) {
        SCORE_FONT.drawString(SCORE_TITLE + player.getScore(), SCORE_X, SCORE_Y);
        PLAYER_HEALTH_FONT.drawString(HEALTH_TITLE + Math.round(player.getHealth() * 100),
                PLAYER_HEALTH_X, PLAYER_HEALTH_Y);

        updateGameObjects(input);

        if (levelNumber == 3) {
            for (Entity entity : entities) {
                if (entity instanceof EnemyBoss) {
                    EnemyBoss boss = (EnemyBoss) entity;
                    ENEMY_HEALTH_FONT.drawString(HEALTH_TITLE + Math.round(boss.getHealth() * 100),
                            ENEMY_HEALTH_X, ENEMY_HEALTH_Y, drawOptions);
                }
            }
        }
    }

    /** This method is to check if the player has win the game level
     * @return boolean true if won, false if otherwise
     */
    public boolean isWin() {
        return win;
    }

    /** This method is to check if the game level has finished
     * @return boolean true if the game level is finished, false otherwise
     */
    public boolean isFinished(){
        return finished;
    }

    /**
     * This method is to reset the game whenever the game is finished (restart the level)
     */
    public void reset(){
        entities.clear();
        finished = false;
        loadLevel();
    }
}
