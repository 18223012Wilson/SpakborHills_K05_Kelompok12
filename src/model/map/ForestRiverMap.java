package model.map;

import model.entitas.NPC;
import model.items.Point;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class ForestRiverMap {
    public static final int MAP_SIZE = 32;
    public static final int TILE_SIZE = 24;

    private char[][] mapGrid = new char[MAP_SIZE][MAP_SIZE];

    private Image grassImage;
    private Image riverImage;
    private Image bridgeImage;
    private Image treeImage;

    public ForestRiverMap() {
        loadImages();
        initializeMapGrid();
        placeRiverAndBridge();
        placeDecorations();
    }

    private void loadImages() {
        grassImage = loadImage("/tiles/untilledSoilNew.png");
        riverImage = loadImage("/tiles/defaultPond.png");
        bridgeImage = loadImage("/tiles/store/floor.png");
        treeImage = loadImage("/tiles/farm_misc/placeholder_tree.png");

        if (grassImage == null) System.err.println("Failed to load grassImage for ForestRiverMap");
        if (riverImage == null) System.err.println("Failed to load riverImage for ForestRiverMap");
        if (bridgeImage == null) System.err.println("Failed to load bridgeImage for ForestRiverMap");
        if (treeImage == null) System.err.println("Failed to load treeImage for ForestRiverMap");
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
                mapGrid[i][j] = '.';
            }
        }
    }

    private void placeRiverAndBridge() {
        int riverStartCol = MAP_SIZE / 2 - 2;
        int riverEndCol = MAP_SIZE / 2 + 2;

        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = riverStartCol; j < riverEndCol; j++) {
                if (isValidTile(i, j)) {
                    mapGrid[i][j] = 'R';
                }
            }
        }

        int bridgeRow = MAP_SIZE / 2;
        for (int j = riverStartCol -2; j <= riverEndCol +1; j++) {
            if (isValidTile(bridgeRow, j)) {
                mapGrid[bridgeRow][j] = 'B';
            }
            if (isValidTile(bridgeRow - 1, j)) {
                mapGrid[bridgeRow -1][j] = 'B';
            }
        }

        for (int i = bridgeRow -2; i <= bridgeRow + 1; i++ ) {
            for (int j = riverStartCol; j < riverEndCol; j++) {
                if (isValidTile(i, j)) {
                    mapGrid[i] [j] = 'B';
                }

            }
        }

    }

    private void placeDecorations() {
        if (isValidTile(5, 5)) mapGrid[5][5] = 'T'; // Tree
        if (isValidTile(10, MAP_SIZE - 6)) mapGrid[10][MAP_SIZE - 6] = 'T';
        if (isValidTile(MAP_SIZE - 6, 5)) mapGrid[MAP_SIZE - 6][5] = 'T';
        if (isValidTile(MAP_SIZE - 7, MAP_SIZE - 7)) mapGrid[MAP_SIZE - 7][MAP_SIZE - 7] = 'T';
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
                Color fallbackColor = Color.DARK_GRAY;

                switch (tileChar) {
                    case '.':
                        toDraw = grassImage;
                        fallbackColor = new Color(34, 177, 76);
                        break;
                    case 'R':
                        toDraw = riverImage;
                        fallbackColor = Color.BLUE;
                        break;
                    case 'B':
                        toDraw = bridgeImage;
                        fallbackColor = new Color(139, 69, 19);
                        break;
                    case 'T':
                        toDraw = treeImage;
                        fallbackColor = new Color(0,100,0);
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