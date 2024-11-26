package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.awt.Point;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class World{

    // build your own world!
    // Define constants
    private static final int MIN_ROOMS = 190;
    private static final int MAX_ROOMS = 250;
    private static final int MIN_WIDTH = 5;
    private static final int MAX_WIDTH = 11;
    private static final int MIN_HEIGHT = 5;
    private static final int MAX_HEIGHT = 11;

    private final int width;
    private final int height;
    public final long seed;
    private final TETile[][] world;
    private static Random random; // Random object for pseudorandom generation
    public Avatar avatar;
    private static final int LINE_OF_SIGHT_RADIUS = 5;
    public static boolean isLineOfSightEnabled;
    private final TERenderer ter;

    public World(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.avatar = new Avatar(0, 0);
        this.isLineOfSightEnabled = false;
        random = new Random(seed);
        this.world = new TETile[width][height];
        this.ter = new TERenderer(); // Initialize the renderer once
        ter.initialize(width, height);

        initializeWorld(); // Fill the world with default "nothing" tiles
        generateWorld();
    }

    private void initializeWorld() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void generateWorld() {
        // Randomly select room locations, ensuring no overlap
        int numRooms = RandomUtils.uniform(random, MIN_ROOMS, MAX_ROOMS);
        List<Point> entrances = new ArrayList<>(); // List to store all entrance points

        for (int i = 0; i < numRooms; i++) {
            int x = RandomUtils.uniform(random, this.width);  // Random x for room bottom-left
            int y = RandomUtils.uniform(random, this.height); // Random y for room bottom-left
            int roomWidth = RandomUtils.uniform(random, MIN_WIDTH, MAX_WIDTH);
            int roomHeight = RandomUtils.uniform(random, MIN_HEIGHT, MAX_HEIGHT);
            generateRoom(world, new Point(x, y), roomWidth, roomHeight, entrances); // Pass entrances list
        }
        // Generate hallways between all entrances
        generateHallway(world, entrances);  // Pass the list of entrance points
        // Add walls around rooms and hallways
        addWalls(world);
    }

    // Modify generateRoom to add entrance points to a list
    private void generateRoom(TETile[][] world, Point bottomLeft, int width, int height, List<Point> entrances) {
        int xStart = bottomLeft.x;
        int yStart = bottomLeft.y;

        List<Point> roomTiles = new ArrayList<>();

        // Check if the room fits within the world bounds
        if (xStart <= 1 || yStart <= 1 || xStart + width >= world.length - 3 || yStart + height >= world[0].length - 3) {
            return; // Room doesn't fit, return
        }

        // Check for overlap with existing rooms
        if (isRoomOverlapping(world, xStart, yStart, width, height)) {
            return; // Room overlaps, return
        }

        // Ensure there are at least 5 empty tiles around the room (no adjacent rooms)
        if (!hasEnoughSpacing(world, xStart, yStart, width, height)) {
            return; // If there's not enough space, return
        }

        // Fill the room with floor tiles (without walls yet)
        for (int x = xStart; x < xStart + width; x++) {
            for (int y = yStart; y < yStart + height; y++) {
                world[x][y] = Tileset.FLOOR;// Set the floor tiles
                if (x == xStart || y == yStart) {
                    roomTiles.add(new Point(x, y)); // Collect room tiles for possible entrances
                }
            }
        }
        // Shuffle room tiles and pick a random tile as the entrance
        Collections.shuffle(roomTiles, random);//@source I asked chatgpt how I can pick a random element from a List
        Point entrance = roomTiles.get(0); // Pick the first tile as entrance
        entrances.add(entrance); // Add to the entrances list
    }

    // Helper method to check if the room overlaps with any existing room
    private boolean isRoomOverlapping(TETile[][] world, int xStart, int yStart, int width, int height) {
        for (int x = xStart; x < xStart + width; x++) {
            for (int y = yStart; y < yStart + height; y++) {
                if (world[x][y] != Tileset.NOTHING) {  // Check if the tile is not "nothing" (already filled)
                    return true;  // Overlap detected
                }
            }
        }
        return false;  // No overlap
    }

    // Helper method to check if there is enough empty space (3 tiles of NOTHING) around the room
    private boolean hasEnoughSpacing(TETile[][] world, int xStart, int yStart, int width, int height) {
        int padding = 3; // Minimum padding around the room

        // Check the surrounding area around the room for 5 empty tiles
        for (int x = xStart - padding; x < xStart + width + padding; x++) {
            for (int y = yStart - padding; y < yStart + height + padding; y++) {
                // Ensure the tile is within bounds and is empty (NOTHING)
                if (x >= 0 && y >= 0 && x < world.length && y < world[0].length) {
                    if (world[x][y] != Tileset.NOTHING) {
                        return false; // There is an obstacle (wall or floor)
                    }
                }
            }
        }
        return true; // No obstacle found, spacing is sufficient
    }

    // Modified method to connect multiple points in sequence
    private void generateHallway(TETile[][] world, List<Point> points) {
        // Check if the list has at least two points to connect
        if (points.size() < 2) {
            return; // If there are fewer than two points, no hallway is needed
        }

        // Iterate over consecutive pairs of points in the list
        for (int i = 0; i < points.size() - 1; i++) {
            Point start = points.get(i);
            Point end = points.get(i + 1);
            connectPoints(world, start, end); // Connect each pair of points
        }
    }

    // Helper method to connect two points (same as before)
    private void connectPoints(TETile[][] world, Point start, Point end) {
        int xStart = start.x;
        int yStart = start.y;
        int xEnd = end.x;
        int yEnd = end.y;

        while (xStart != xEnd || yStart != yEnd) {
            boolean shouldSwitchDirection = random.nextInt(100) < 20; // 20% chance
            if (xStart != xEnd && !shouldSwitchDirection) {
                // Continue moving horizontally if not switching
                if (xStart < xEnd) {
                    xStart++; // Move right
                } else {
                    xStart--; // Move left
                }
            } else if (yStart != yEnd && (shouldSwitchDirection || xStart == xEnd)) {
                // Switch to moving vertically or continue moving vertically if on same x
                if (yStart < yEnd) {
                    yStart++; // Move down
                } else {
                    yStart--; // Move up
                }
            }
            // Mark the tile as a floor tile for the hallway
            world[xStart][yStart] = Tileset.FLOOR;
        }
    }

    private void addWalls(TETile[][] world) {
        // Iterate through the world and add walls around rooms and hallways
        for (int x = 1; x < world.length - 1; x++) {
            for (int y = 1; y < world[0].length - 1; y++) {
                // Check if the tile is a floor tile (part of a room or hallway)
                if (world[x][y] == Tileset.FLOOR) {
                    // Check adjacent tiles: if any of them are nothing, mark this tile as a wall
                    if (world[x - 1][y] == Tileset.NOTHING) {
                        world[x - 1][y] = Tileset.WALL; // Left
                    }
                    if (world[x + 1][y] == Tileset.NOTHING) {
                        world[x + 1][y] = Tileset.WALL; // Right
                    }
                    if (world[x][y - 1] == Tileset.NOTHING) {
                        world[x][y - 1] = Tileset.WALL; // Below
                    }
                    if (world[x][y + 1] == Tileset.NOTHING) {
                        world[x][y + 1] = Tileset.WALL; // Above
                    }
                }
            }
        }
    }

    // Place the avatar on the first floor tile
    public void placeAvatar() {
        for (int x = 0; x < width; x = x + 3) {
            for (int y = 0; y < height; y = y + 3) {
                if (world[x][y] == Tileset.FLOOR) { // Check for a floor tile
                    this.avatar.x = x;
                    this.avatar.y = y;
                    world[x][y] = Tileset.AVATAR; // Place avatar
                    return;
                }
            }
        }
    }

    // Move the avatar if the next position is valid
    public void moveAvatar(int dx, int dy) {
        int newX = this.avatar.x + dx;
        int newY = this.avatar.y + dy;

        // Check bounds and ensure the new position is not a wall
        if (newX >= 0 && newX < width && newY >= 0 && newY < height && world[newX][newY] == Tileset.FLOOR) {
            world[this.avatar.x][this.avatar.y] = Tileset.FLOOR; // Reset current position to floor
            this.avatar.x = newX;
            this.avatar.y = newY;
            world[this.avatar.x][this.avatar.y] = Tileset.AVATAR; // Move avatar
        }
    }

    public void setAvatar(int x, int y) {
        this.avatar.setPosition(x, y);
        world[x][y] = Tileset.AVATAR;
    }

    public void renderWorld() {
        ter.renderFrame(world, this.avatar.x , this.avatar.y, this.isLineOfSightEnabled, LINE_OF_SIGHT_RADIUS);
    }
}
