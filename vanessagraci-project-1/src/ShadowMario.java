import bagel.*;
import java.util.Properties;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
/**
 * Skeleton Code for SWEN20003 Project 1, Semester 1, 2024
 *
 * Please enter your name below
 * @author Vanessa Gracia Tan
 */
public class ShadowMario extends AbstractGame {

    private final Image BACKGROUND_IMAGE;
    private final Image PLATFORM_IMAGE;
    private final Image COIN_IMAGE;
    private final Image ENEMY_IMAGE;
    private final Image FLAG_IMAGE;
    private final Font title;
    private final String titleMessage;
    private final double titleX;
    private final double titleY;
    private final int titleSize;
    private final Font instruction;
    private final Font score;
    private final double scoreX;
    private final double scoreY;
    private final int scoreSize;
    private final Font health;
    private final double healthX;
    private final double healthY;
    private final int healthSize;
    private final double instructionY;
    private final double instructionX;
    private final String instructionMessage;
    private final int instructionSize;
    private final String scoreMessage;
    private final String healthMessage;
    private final Font message;
    private final int messageSize;
    private final String winMessage;
    private final String loseMessage;
    private final double messageX;
    private final double messageY;
    private final String filepath;
    private boolean start = false;
    private boolean finish = false;
    private Entity[] entities;
    private Player player;
    protected static double playerX;
    protected static double playerY;
    private Score totalScore;
    private final int enemyCount;
    private final int coinCount;
    protected static int coinValue;
    protected static double healthPoint;
    protected static double damagePoint;

    protected static int enemySpeed;
    protected static int platformSpeed;
    protected static int coinSpeed;
    protected static int flagSpeed;
    protected static double playerRadius;
    protected static double coinRadius;
    protected static double enemyRadius;
    protected static double flagRadius;
    private boolean win = false;

    /**
     * The constructor
     */
    public ShadowMario(Properties game_props, Properties message_props) {
        super(Integer.parseInt(game_props.getProperty("windowWidth")),
                Integer.parseInt(game_props.getProperty("windowHeight")),
                message_props.getProperty("title"));

        // import the images and size needed from the Property files
        BACKGROUND_IMAGE = new Image(game_props.getProperty("backgroundImage"));
        PLATFORM_IMAGE = new Image(game_props.getProperty("gameObjects.platform.image"));
        COIN_IMAGE = new Image(game_props.getProperty("gameObjects.coin.image"));
        ENEMY_IMAGE = new Image(game_props.getProperty("gameObjects.enemy.image"));
        FLAG_IMAGE = new Image(game_props.getProperty("gameObjects.endFlag.image"));

        titleSize = Integer.parseInt(game_props.getProperty("title.fontSize"));
        instructionSize = Integer.parseInt(game_props.getProperty("instruction.fontSize"));
        scoreSize = Integer.parseInt(game_props.getProperty("score.fontSize"));
        healthSize = Integer.parseInt(game_props.getProperty("health.fontSize"));
        messageSize = Integer.parseInt(game_props.getProperty("message.fontSize"));

        // import font directory from Property files and set the font size for each font
        title = new Font(game_props.getProperty("font"), titleSize);
        instruction = new Font(game_props.getProperty("font"), instructionSize);
        score = new Font(game_props.getProperty("font"), scoreSize);
        health = new Font(game_props.getProperty("font"), healthSize);
        message = new Font(game_props.getProperty("font"), messageSize);

        // access the data (message) from Property files
        titleMessage = message_props.getProperty("title");
        titleX = Double.parseDouble(game_props.getProperty("title.x"));
        titleY = Double.parseDouble(game_props.getProperty("title.y"));

        // access the instruction message from Message files and set the x and y coordinates
        instructionMessage = message_props.getProperty("instruction");
        instructionY = Double.parseDouble(game_props.getProperty("instruction.y"));
        instructionX = (Window.getWidth() / 2.0) - ((int) (instruction.getWidth(instructionMessage) / 2.0));

        // access the score and health text from Message files
        scoreMessage = message_props.getProperty("score");
        scoreX = Double.parseDouble(game_props.getProperty("score.x"));
        scoreY = Double.parseDouble(game_props.getProperty("score.y"));
        healthMessage = message_props.getProperty("health");
        healthX = Double.parseDouble(game_props.getProperty("health.x"));
        healthY = Double.parseDouble(game_props.getProperty("health.y"));

        // access the message from property file
        winMessage = message_props.getProperty("gameWon");
        loseMessage = message_props.getProperty("gameOver");
        messageX = (Window.getWidth() / 2.0) - ((int) (instruction.getWidth(instructionMessage) / 2.0));
        messageY = Double.parseDouble(game_props.getProperty("message.y"));

        // access details for the game
        enemyCount = Integer.parseInt(game_props.getProperty("gameObjects.enemy.enemyCount"));
        coinCount = Integer.parseInt(game_props.getProperty("gameObjects.coin.coinCount"));

        healthPoint = Double.parseDouble(game_props.getProperty("gameObjects.player.health"));
        damagePoint = Double.parseDouble(game_props.getProperty("gameObjects.enemy.damageSize"));
        coinValue = Integer.parseInt(game_props.getProperty("gameObjects.coin.value"));

        enemySpeed = Integer.parseInt(game_props.getProperty("gameObjects.enemy.speed"));
        platformSpeed = Integer.parseInt(game_props.getProperty("gameObjects.platform.speed"));
        coinSpeed = Integer.parseInt(game_props.getProperty("gameObjects.coin.speed"));
        flagSpeed = Integer.parseInt(game_props.getProperty("gameObjects.endFlag.speed"));

        playerRadius = Double.parseDouble(game_props.getProperty("gameObjects.player.radius"));
        enemyRadius = Double.parseDouble(game_props.getProperty("gameObjects.enemy.radius"));
        coinRadius = Double.parseDouble(game_props.getProperty("gameObjects.coin.radius"));
        flagRadius = Double.parseDouble(game_props.getProperty("gameObjects.endFlag.radius"));

        // filepath for level1.csv
        filepath = game_props.getProperty("levelFile");
        
        // read level1.csv file
        readCSV();

        // get the x and y coordinates for player from the csv file
        for (Entity entity : entities) {
            if (entity.getType().equals("PLAYER")) {
                playerX = entity.getX();
                playerY = entity.getY();
            }

            // set player and score
            player = new Player(playerX, playerY);
            totalScore = new Score();
        }
    }


    /**
     * The entry point for the program.
     */

    // read level1.csv to access entity, convert the data from csv file into array
    private void readCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String text;

            // +3 (platform, end_flag, player)
            entities = new Entity[enemyCount + coinCount + 3];
            int index = 0;
            while ((text = br.readLine()) != null) {
                String[] cells = text.split(",");
                String entity = cells[0];
                double xCoordinate = Double.parseDouble(cells[1]);
                double yCoordinate = Double.parseDouble(cells[2]);

                entities[index] = new Entity(entity, xCoordinate, yCoordinate);
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // run the game
    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");

        ShadowMario game = new ShadowMario(game_props, message_props);
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        // this block of code is for testing purpose (120hz) only
        try {
            Thread.sleep(1000 / 120); // arg is in milliseconds
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // close window
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        if (!start) {
            // insert title and instruction in front window page
            title.drawString(titleMessage, titleX, titleY);
            instruction.drawString(" " + instructionMessage, instructionX, instructionY);

            if (input.wasPressed(Keys.SPACE)) {
                start = true;
            }

        // if the game finished
        } else if (finish) {
            if (win){
                message.drawString(winMessage, messageX, messageY);
            } else {
                message.drawString("  " + loseMessage, messageX, messageY);
            }

            if (input.wasPressed(Keys.SPACE)){
                resetGame();
            }

        // game starts
        } else {

            // player movement
            player.update(input);

            // insert (draw) the entity to its corresponding x and y coordinate
            for (Entity entity : entities) {
                entity.move(input);
                switch (entity.getType()) {
                    case "PLATFORM":
                        PLATFORM_IMAGE.draw(entity.getX(), entity.getY());
                        break;
                    case "COIN":
                        COIN_IMAGE.draw(entity.getX(), entity.getY());
                        break;
                    case "ENEMY":
                        ENEMY_IMAGE.draw(entity.getX(), entity.getY());
                        break;
                    case "END_FLAG":
                        FLAG_IMAGE.draw(entity.getX(), entity.getY());
                        break;
                }
            }

            // entity movement and calculate score and health
            for (Entity entity : entities) {
                if (entity.getType().equals("COIN") && (totalScore.collision(player, entity)) | entity.alreadyCollided()) {
                    entity.moveCoin();
                } else if (entity.getType().equals("ENEMY")){
                    totalScore.collision(player, entity);
                }
            }

            if (totalScore.getHealth() <= 0){
                player.lose();
            }

            finish = gameFinished();

            player.draw(input);
            score.drawString(scoreMessage + totalScore.getScore(), scoreX, scoreY);
            health.drawString(healthMessage + (int) (totalScore.getHealth() * 100), healthX, healthY);
        }
    }

    // method that check if the game finished or not and either the player lose or win
    public boolean gameFinished () {
        for (Entity entity : entities) {
            if (player.getY() >= Window.getHeight()){
                return true;
            } else if (entity.getType().equals("END_FLAG") && totalScore.collision(player, entity)) {
                win = true;
                return true;
            }
        }
        return false;
    }

    // method to reset the game - reset all variables
    private void resetGame(){
        player = new Player(playerX, playerY);
        win = false;
        totalScore = new Score();
        finish = false;
        start = false;
        entities = new Entity[enemyCount + coinCount + 3];
        readCSV();
    }
}
