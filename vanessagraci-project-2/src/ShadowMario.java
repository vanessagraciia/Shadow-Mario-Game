import bagel.*;
import java.util.Properties;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 1, 2024
 * Shadow Mario class
 * Please enter your name below
 * @author Vanessa Gracia Tan
 */
public class ShadowMario extends AbstractGame {

    private final Image BACKGROUND_IMAGE;
    private final String TITLE_MESSAGE;
    private final int TITLE_X;
    private final int TITLE_Y;
    private final Font TITLE_FONT;
    private final String INSTRUCTION_MESSAGE;
    private final int INSTRUCTION_Y;
    private final Font INSTRUCTION_FONT;
    private final Font MESSAGE_FONT;
    private final int MESSAGE_Y;
    private final String GAME_OVER_MESSAGE;
    private final String GAME_WON_MESSAGE;
    private final String FONT_FILE;
    private boolean start = false;
    private final Properties game_props;
    private final Properties message_props;
    private int selectedLevel = 0;
    private Level level;


    /**
     * The constructor
     */
    public ShadowMario(Properties game_props, Properties message_props) {
        super(Integer.parseInt(game_props.getProperty("windowWidth")),
                Integer.parseInt(game_props.getProperty("windowHeight")),
                message_props.getProperty("title"));

        BACKGROUND_IMAGE = new Image(game_props.getProperty("backgroundImage"));
        FONT_FILE = game_props.getProperty("font");

        TITLE_MESSAGE = message_props.getProperty("title");
        TITLE_X = Integer.parseInt(game_props.getProperty("title.x"));
        TITLE_Y = Integer.parseInt(game_props.getProperty("title.y"));
        TITLE_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("title.fontSize")));

        INSTRUCTION_MESSAGE = message_props.getProperty("instruction");
        INSTRUCTION_Y = Integer.parseInt(game_props.getProperty("instruction.y"));
        INSTRUCTION_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("instruction.fontSize")));

        MESSAGE_Y = Integer.parseInt(game_props.getProperty("message.y"));
        MESSAGE_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("message.fontSize")));

        GAME_OVER_MESSAGE = message_props.getProperty("gameOver");
        GAME_WON_MESSAGE = message_props.getProperty("gameWon");

        this.game_props = game_props;
        this.message_props = message_props;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowMario game = new ShadowMario(game_props, message_props);
        game.run();
    }

    /**
     * Performs a state update of the selected level.
     * Allows the game to exit when the escape key is pressed.
     * Handle screen navigation between levels and instruction pages here.
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
            // starting screen
            TITLE_FONT.drawString(TITLE_MESSAGE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString("     " + INSTRUCTION_MESSAGE,
                    Window.getWidth() / 2 - INSTRUCTION_FONT.getWidth(INSTRUCTION_MESSAGE) / 2, INSTRUCTION_Y);

            // Check if the player selects a level
            if (input.wasPressed(Keys.NUM_1) || input.wasPressed(Keys.NUM_2) || input.wasPressed(Keys.NUM_3)) {
                selectedLevel = input.wasPressed(Keys.NUM_1) ? 1 : (input.wasPressed(Keys.NUM_2) ? 2 : 3);

                level = new Level(selectedLevel, game_props, message_props);
                start = true;
            }
        }
        // if level is finished
        else if (level.isFinished()) {

            if (level.isWin()) {
                MESSAGE_FONT.drawString(GAME_WON_MESSAGE,
                        Window.getWidth() / 2 - MESSAGE_FONT.getWidth(GAME_WON_MESSAGE) / 2,
                        MESSAGE_Y);
            }

            else {
                MESSAGE_FONT.drawString(GAME_OVER_MESSAGE,
                        Window.getWidth() / 2 - MESSAGE_FONT.getWidth(GAME_OVER_MESSAGE) / 2,
                        MESSAGE_Y);
            }

            if (input.wasPressed(Keys.SPACE)) {
                level.reset();
                resetGame();
            }
        }

        else {
            // game is running
            level.run(input);
        }
    }

    // method to reset the game - reset start and level
    private void resetGame(){
        start = false;
        level = null;
    }
}

