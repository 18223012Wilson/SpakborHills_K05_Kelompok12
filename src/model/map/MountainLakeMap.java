package model.map;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MountainLakeMap {
    public static final int MAP_SIZE = 32;
    public static final int TILE_SIZE = 24;
    public static final int radius = 8;

    private char[][] mapGrid = new char[MAP_SIZE][MAP_SIZE];

    private Image grassImage;
    private Image waterImage;
    private Image rockImage;

    public MountainLakeMap() {
        loadImages();
        initializeMapGrid();
        placeLake();
        placeDecorations();
    }

    private void loadImages() {
        grassImage = loadImage("/tiles/untilledSoilNew.png");
        waterImage = loadImage("/tiles/defaultPond.png");
        rockImage = loadImage("/tiles/farm_misc/placeholder_rock.png");

        if (grassImage == null) System.err.println("Failed to load grassImage for MountainLakeMap");
        if (waterImage == null) System.err.println("Failed to load waterImage for MountainLakeMap");
        if (rockImage == null) System.err.println("Failed to load rockImage for MountainLakeMap");
    }

    private Image loadImage(String path) {
        URL imgUrl = getClass().getResource(path);
        if (imgUrl == null) {
            return null;
        }
        return new ImageIcon(imgUrl).getImage();
    }

    private void initializeMapGrid() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                mapGrid[i][j] = '.'; // Default grass/path
            }
        }
    }

    private void placeLake() {
        int centerX = MAP_SIZE / 2;
        int centerY = MAP_SIZE / 2;
        int radius = 8; // Adjust for desired lake size

        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                // Simple circular lake
                if (Math.sqrt(Math.pow(i - centerY, 2) + Math.pow(j - centerX, 2)) <= radius) {
                    if (isValidTile(i, j)) {
                        mapGrid[i][j] = 'L'; // Lake water
                    }
                }
            }
        }
        // Ensure some walkable edges around the lake if the circle is too perfect
        // For simplicity, this basic circle should allow player to stand adjacent to water.
    }

    private void placeDecorations() {
        if (isValidTile(MAP_SIZE / 2 - radius - 2, MAP_SIZE / 2)) mapGrid[MAP_SIZE / 2 - radius - 2][MAP_SIZE / 2] = 'R';
        if (isValidTile(MAP_SIZE / 2 + radius + 2, MAP_SIZE / 2)) mapGrid[MAP_SIZE / 2 + radius + 2][MAP_SIZE / 2] = 'R';
        if (isValidTile(MAP_SIZE / 2, MAP_SIZE / 2 - radius - 2)) mapGrid[MAP_SIZE / 2][MAP_SIZE / 2 - radius - 2] = 'R';
        if (isValidTile(MAP_SIZE / 2, MAP_SIZE / 2 + radius + 2)) mapGrid[MAP_SIZE / 2][MAP_SIZE / 2 + radius + 2] = 'R';

        if (isValidTile(3, 3)) mapGrid[3][3] = 'R';
        if (isValidTile(MAP_SIZE - 4, MAP_SIZE - 4)) mapGrid[MAP_SIZE - 4][MAP_SIZE - 4] = 'R';
    }

    private boolean isValidTile(int r, int c) {
        return r >= 0 && r < MAP_SIZE && c >= 0 && c < MAP_SIZE;
    }

    public char getTileType(int r, int c) {
        if (isValidTile(r, c)) {
            return mapGrid[r][c];
        }
        return '#'; // Obstacle/Out of bounds
    }

    public void draw(Graphics2D g2d, Component observer) {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                int x = j * TILE_SIZE;
                int y = i * TILE_SIZE;
                char tileChar = getTileType(i, j);
                Image toDraw = null;
                Color fallbackColor = Color.DARK_GRAY;

                switch (tileChar) {
                    case '.': // Grass
                        toDraw = grassImage;
                        fallbackColor = new Color(34, 177, 76);
                        break;
                    case 'L': // Lake Water
                        toDraw = waterImage;
                        fallbackColor = new Color(0, 100, 200);
                        break;
                    case 'R': // Rock
                        toDraw = rockImage;
                        fallbackColor = Color.GRAY;
                        break;
                    default:
                        toDraw = grassImage; // Default to grass if unknown
                        fallbackColor = new Color(50,50,50);
                        break;
                }

                if (toDraw != null) {
                    g2d.drawImage(toDraw, x, y, TILE_SIZE, TILE_SIZE, observer);
                } else {
                    g2d.setColor(fallbackColor);
                    g2d.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }
}