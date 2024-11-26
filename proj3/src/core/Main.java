package core;


import edu.princeton.cs.algs4.StdDraw; //@source Ed #1822ba
import utils.FileUtils;

public class Main {
    private static final int WIDTH = 95;
    private static final int HEIGHT = 50;
    private static String SAVE_FILE = "save.txt";
    private static final MusicPlayer musicPlayer = new MusicPlayer();

    public static void main(String[] args) {

        // build your own world!
        // Set up the canvas
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 100);
        StdDraw.setYscale(0, 100);

        musicPlayer.playMusic("resources/menu_music.wav"); //@source Ed 1825abf

        // Display the main menu
        mainMenu();
        // Main interaction loop
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped()); // Make key press case-insensitive
                if (key == 'n') {
                    long seed = enterSeedScreen();
                    startNewGame(seed);
                } else if (key == 'l') {
                    loadGame();
                } else if (key == 'q') {
                    System.exit(0); // Close the program
                }
            }
        }
    }

    // Method to draw the main menu
    private static void mainMenu() {
        StdDraw.clear(StdDraw.BLACK);
        // Title
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(50, 80, "CS61B: BYOW");
        // Instructions
        StdDraw.text(50, 60, "(N) New Game");
        StdDraw.text(50, 55, "(L) Load Game");
        StdDraw.text(50, 50, "(Q) Quit Game");
    }

    // Method to display the "Enter Seed" screen and collect seed input
    private static long enterSeedScreen() {
        StdDraw.clear(StdDraw.BLACK);
        // Title
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(50, 80, "Enter a seed followed by 'S'");
        StringBuilder seedBuilder = new StringBuilder();
        // Seed entry loop
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();

                if (Character.isDigit(key)) { // Append digit to seed
                    seedBuilder.append(key);
                    drawSeedInput(seedBuilder.toString());
                } else if (key == 's' || key == 'S') { // Finalize input with 'S'
                    break;
                }
            }
        }
        // Convert the seed to a long
        long seed = Long.parseLong(seedBuilder.toString());
        return seed;
    }

    // Helper to draw the current seed input
    private static void drawSeedInput(String seed) {
        StdDraw.clear(StdDraw.BLACK);
        // Title
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(50, 80, "Enter a seed followed by 'S':");
        // Display the current seed
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.text(50, 50, "Seed: " + seed);
    }

    private static void startNewGame(long seed) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.text(50, 50, "Starting new game with seed: " + seed);
        StdDraw.text(50, 45, "Press v/V to turn on/off Line of Sight.");

        StdDraw.pause(3000); // Pause for 1 seconds
        // Initialize and render a world (customize this logic as needed)
        World world = new World(WIDTH, HEIGHT, seed);
        world.placeAvatar();
        gameLoop(world);
    }

    private static void gameLoop(World world) {
        boolean colonPressed = false; // Track ':' key press

        // Stop menu music and play exploration music when entering exploration
        musicPlayer.stopMusic();
        musicPlayer.playMusic("resources/exploration_music.wav");

        // Main game loop for rendering and movement
        while (true) {
            // Render the world
            world.renderWorld();

            // Check for user input
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();

                if (key == 'v' || key == 'V') {
                    world.isLineOfSightEnabled = !world.isLineOfSightEnabled; // Toggle LoS
                }

                if (colonPressed) {
                    // Handle the key after colon
                    if (key == 'q' || key == 'Q') {
                        saveGame(world, world.seed); // Save the game
                        System.exit(0); // Quit
                    } else {
                        colonPressed = false; // Reset colon tracking
                    }
                } else if (key == ':') {
                    colonPressed = true; // Start tracking for ':q'
                }
                // Movement keys
                switch (Character.toLowerCase(key)) {
                    case 'w':
                        world.moveAvatar(0, 1);
                        break;
                    case 'a':
                        world.moveAvatar(-1, 0);
                        break;
                    case 's':
                        world.moveAvatar(0, -1);
                        break;
                    case 'd':
                        world.moveAvatar(1, 0);
                        break;
                    default:
                        break;
                }
            }
            // Pause briefly to allow smooth animation
            StdDraw.pause(10);
        }
    }

    private static void saveGame(World world, long seed) {
        final String SAVE_FILE = "save.txt"; // File name for saving the game

        // Get the avatar's current position
        int avatarX = world.avatar.x;
        int avatarY = world.avatar.y;
        boolean sight = world.isLineOfSightEnabled;

        // Build the save data (seed and avatar position)
        String saveData = seed + "\n" + avatarX + " " + avatarY + "\n" + sight + "\n";

        // Use FileUtils to write the data to save.txt
        FileUtils.writeFile(SAVE_FILE, saveData);

        // Display success message
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.text(50, 25, "Game saved successfully.");
        StdDraw.show();
        StdDraw.pause(2000);
        }

    private static void loadGame() {
        // Check if the save file exists before trying to load it
        if (!FileUtils.fileExists(SAVE_FILE)) {
            final String SAVE_FILE = "save.txt";
        }

        // Read the save data from the file using FileUtils
        String savedData = FileUtils.readFile(SAVE_FILE);
        // Parse the saved data
        String[] lines = savedData.split("\n");
        // Ensure that the data consists of complete lines (seed and avatar position)
        if (lines.length < 3) {
            System.exit(0);
        }

        // Parse the most recent save (last two lines)
        long seed = Long.parseLong(lines[lines.length - 3]); // Seed is second-to-last line
        String[] avatarPos = lines[lines.length - 2].split(" "); // Avatar position is last line
        int avatarX = Integer.parseInt(avatarPos[0]);
        int avatarY = Integer.parseInt(avatarPos[1]);
        boolean sight = Boolean.parseBoolean(lines[lines.length - 1]);

        // Display success message
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.text(50, 50, "Game loaded successfully.");
        StdDraw.text(50, 45, "Press v/V to turn on/off Line of Sight.");
        StdDraw.show();
        StdDraw.pause(2500);

        StdDraw.clear(StdDraw.BLACK);
        // Initialize the world with the saved seed
        World world = new World(WIDTH, HEIGHT, seed);
        // Set the avatar's position
        world.setAvatar(avatarX, avatarY);
        world.isLineOfSightEnabled = sight;
        // Clear the screen and render the world with the avatar at the saved position
        gameLoop(world);
    }
}