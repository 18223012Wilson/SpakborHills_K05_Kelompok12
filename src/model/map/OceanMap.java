package model.map;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class OceanMap {
    public static final int MAP_SIZE = 32;
    public static final int TILE_SIZE = 24;

    private char[][] mapGrid = new char[MAP_SIZE][MAP_SIZE];

    private Image beachImage;
    private Image waterImage;
    private Image bridgeImage;
    private Image rockImage;
    private Image palmTreeImage;

    public OceanMap() {
        loadImages();
        initializeMapGrid();
        placeBeachAndWater();
        placeBridge();
        placeDecorations();
    }

    private void loadImages() {
        beachImage = loadImage("/tiles/store/floor.png");
        waterImage = loadImage("/tiles/defaultPond.png");
        bridgeImage = loadImage("/tiles/farm_misc/placeholder_rock.png");
        rockImage = loadImage("/tiles/farm_misc/placeholder_rock.png");
        palmTreeImage = loadImage("/tiles/farm_misc/placeholder_tree.png");

        if (beachImage == null) System.err.println("Failed to load beachImage for OceanMap");
        if (waterImage == null) System.err.println("Failed to load waterImage for OceanMap");
        if (bridgeImage == null) System.err.println("Failed to load bridgeImage for OceanMap");
        if (rockImage == null) System.err.println("Failed to load rockImage for OceanMap");
        if (palmTreeImage == null) System.err.println("Failed to load palmTreeImage for OceanMap");
    }

    private Image loadImage(String path) {
        URL imgUrl = getClass().getResource(path);
        if (imgUrl == null) {
            System.err.println("OceanMap: Could not load image at path: " + path);
            return null;
        }
        return new ImageIcon(imgUrl).getImage();
    }

    private void initializeMapGrid() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                mapGrid[i][j] = 'O';
            }
        }
    }

    private void placeBeachAndWater() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (isValidTile(i, j)) {
                    mapGrid[i][j] = '.';
                }
            }
        }
        for (int j = 0; j < MAP_SIZE; j++) {
            if (isValidTile(0,j)) mapGrid[0][j] = '.';
            if (isValidTile(5,j)) mapGrid[5][j] = '.';
        }
        for (int i = 0; i < 6; i++) {
            if(isValidTile(i,0)) mapGrid[i][0] = '.';
            if(isValidTile(i, MAP_SIZE-1)) mapGrid[i][MAP_SIZE-1] = '.';
        }
    }

    private void placeBridge() {
        int bridgeStartCol = MAP_SIZE / 2 - 1;
        int bridgeEndCol = MAP_SIZE / 2;

        for (int i = 5; i < MAP_SIZE; i++) {
            for (int j = bridgeStartCol; j <= bridgeEndCol; j++) {
                if (isValidTile(i, j)) {
                    mapGrid[i][j] = 'B';
                }
            }
        }
    }

    private void placeDecorations() {
        if (isValidTile(2, 3)) mapGrid[2][3] = 'R';
        if (isValidTile(3, MAP_SIZE - 4)) mapGrid[3][MAP_SIZE - 4] = 'R';
        if (isValidTile(1, MAP_SIZE / 2)) mapGrid[1][MAP_SIZE /2] = 'T';
        if (isValidTile(4, 5)) mapGrid[4][5] = 'T';
        if (isValidTile(4, MAP_SIZE - 6)) mapGrid[4][MAP_SIZE - 6] = 'T';
    }

    private boolean isValidTile(int r, int c) {
        return r >= 0 && r < MAP_SIZE && c >= 0 && c < MAP_SIZE;
    }

    public char getTileType(int r, int c) {
        if (isValidTile(r, c)) {
            return mapGrid[r][c];
        }
        return '#';
    }

    public void draw(Graphics2D g2d, Component observer) {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                int x = j * TILE_SIZE;
                int y = i * TILE_SIZE;
                char tileChar = getTileType(i, j);
                Image toDraw = null;
                Color fallbackColor = Color.BLACK;

                switch (tileChar) {
                    case '.':
                        toDraw = beachImage;
                        fallbackColor = new Color(238, 221, 130);
                        break;
                    case 'O':
                        toDraw = waterImage;
                        fallbackColor = new Color(0, 105, 148);
                        break;
                    case 'B':
                        toDraw = bridgeImage;
                        fallbackColor = new Color(139, 69, 19);
                        break;
                    case 'R':
                        toDraw = rockImage;
                        fallbackColor = Color.GRAY;
                        break;
                    case 'T':
                        toDraw = palmTreeImage;
                        fallbackColor = new Color(34, 139, 34);
                        break;
                    default:
                        fallbackColor = Color.BLACK;
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