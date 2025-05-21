package model.map;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class FarmMap {
    public static final int MAP_SIZE = 32;
    public static final int TILE_SIZE = 24;
    private char[][] mapGrid = new char[MAP_SIZE][MAP_SIZE];

    private Image floorImage;
    private Image tilledSoilImage;
    private Image[][] houseTiles = new Image[6][6];
    private Image[][] shippingBinTiles = new Image[2][3];
    private Image pondImage;

    private int houseRow, houseCol;
    private int shippingRow, shippingCol;
    private int pondRow, pondCol;

    private static final int HOUSE_SIZE = 6;
    private static final int SHIPPING_BIN_HEIGHT = 2;
    private static final int SHIPPING_BIN_WIDTH = 3;
    private static final int POND_HEIGHT = 3;
    private static final int POND_WIDTH = 4;


    public FarmMap() {
        String floorPath = "src/untilledSoilNew.png";
        String tilledPath = "src/tilledSoil.png";
        String pondTilePath = "src/defaultPond.png";
        String[][] housePaths = {
                {"src/houseTop.png","src/houseTop.png","src/houseTop.png","src/houseTop.png","src/houseTop.png","src/houseTop.png"},
                {"src/houseTop.png","src/houseTop.png","src/houseTop.png","src/houseTop.png","src/houseTop.png","src/houseChimney.png"},
                {"src/houseTop.png","src/houseTop.png","src/houseTop.png","src/houseTop.png","src/houseTop.png","src/houseRoof.png"},
                {"src/houseTopMid.png","src/houseTopMid.png","src/houseTopMid.png","src/houseTopMid.png","src/houseTopMid.png","src/houseTopMid.png"},
                {"src/houseBottomLeft.png","src/houseBottom.png","src/houseBottom.png","src/houseBottom.png","src/houseBottom.png","src/houseBottomRight.png"},
                {"src/houseBottomLeft.png","src/houseBottom.png","src/houseDoor.png","src/houseBottom.png","src/houseBottom.png","src/houseBottomRight.png"}
        };
        String[][] shippingPaths = {
                {"src/shippingBin.png","src/shippingBin.png","src/shippingBin.png"},
                {"src/shippingBin.png","src/shippingBin.png","src/shippingBin.png"}
        };

        try {
            floorImage = new ImageIcon(floorPath).getImage();
            tilledSoilImage = new ImageIcon(tilledPath).getImage();
            pondImage = new ImageIcon(pondTilePath).getImage();

            for (int i = 0; i < HOUSE_SIZE; i++) {
                for (int j = 0; j < HOUSE_SIZE; j++) {
                    houseTiles[i][j] = new ImageIcon(housePaths[i][j]).getImage();
                }
            }
            for (int i = 0; i < SHIPPING_BIN_HEIGHT; i++) {
                for (int j = 0; j < SHIPPING_BIN_WIDTH; j++) {
                    shippingBinTiles[i][j] = new ImageIcon(shippingPaths[i][j]).getImage();
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading map images. Ensure 'src' folder with images is present. Affected images: " + e.getMessage());
            floorImage = null;
            tilledSoilImage = null;
            pondImage = null;
        }
        initializeMapGrid();
    }

    private void initializeMapGrid() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                mapGrid[i][j] = '.';
            }
        }
        placeHouseRandom();
        placePondRandom();
        placeShippingBinWithGap();
        placeRandomObstacles();
    }

    private void placeHouseRandom() {
        Random rand = new Random();
        do {
            houseRow = rand.nextInt(MAP_SIZE - HOUSE_SIZE + 1);
            houseCol = rand.nextInt(MAP_SIZE - HOUSE_SIZE + 1);
        } while (!isRegionFree(houseRow, houseCol, HOUSE_SIZE, HOUSE_SIZE));
        for (int i = houseRow; i < houseRow + HOUSE_SIZE; i++) {
            for (int j = houseCol; j < houseCol + HOUSE_SIZE; j++) {
                mapGrid[i][j] = 'H';
            }
        }
    }

    private void placePondRandom() {
        Random rand = new Random();
        do {
            pondRow = rand.nextInt(MAP_SIZE - POND_HEIGHT + 1);
            pondCol = rand.nextInt(MAP_SIZE - POND_WIDTH + 1);
        } while (!isRegionFree(pondRow, pondCol, POND_HEIGHT, POND_WIDTH));
        for (int i = pondRow; i < pondRow + POND_HEIGHT; i++) {
            for (int j = pondCol; j < pondCol + POND_WIDTH; j++) {
                mapGrid[i][j] = 'W';
            }
        }
    }

    private void placeShippingBinWithGap() {
        Random rand = new Random();
        List<Supplier<Boolean>> areaCheckers = new ArrayList<>(Arrays.asList(
                () -> { // Top
                    int rBin = houseRow - 1 - SHIPPING_BIN_HEIGHT;
                    for (int cBin = houseCol; cBin <= houseCol + HOUSE_SIZE - SHIPPING_BIN_WIDTH; cBin++) {
                        if (isRegionFree(rBin, cBin, SHIPPING_BIN_HEIGHT, SHIPPING_BIN_WIDTH)) {
                            shippingRow = rBin;
                            shippingCol = cBin;
                            return true;
                        }
                    }
                    return false;
                },
                () -> { // Bottom
                    int rBin = houseRow + HOUSE_SIZE + 1;
                    for (int cBin = houseCol; cBin <= houseCol + HOUSE_SIZE - SHIPPING_BIN_WIDTH; cBin++) {
                        if (isRegionFree(rBin, cBin, SHIPPING_BIN_HEIGHT, SHIPPING_BIN_WIDTH)) {
                            shippingRow = rBin;
                            shippingCol = cBin;
                            return true;
                        }
                    }
                    return false;
                },
                () -> { // Left
                    int cBin = houseCol - 1 - SHIPPING_BIN_WIDTH;
                    for (int rBin = houseRow; rBin <= houseRow + HOUSE_SIZE - SHIPPING_BIN_HEIGHT; rBin++) {
                        if (isRegionFree(rBin, cBin, SHIPPING_BIN_HEIGHT, SHIPPING_BIN_WIDTH)) {
                            shippingRow = rBin;
                            shippingCol = cBin;
                            return true;
                        }
                    }
                    return false;
                },
                () -> { // Right
                    int cBin = houseCol + HOUSE_SIZE + 1;
                    for (int rBin = houseRow; rBin <= houseRow + HOUSE_SIZE - SHIPPING_BIN_HEIGHT; rBin++) {
                        if (isRegionFree(rBin, cBin, SHIPPING_BIN_HEIGHT, SHIPPING_BIN_WIDTH)) {
                            shippingRow = rBin;
                            shippingCol = cBin;
                            return true;
                        }
                    }
                    return false;
                }
        ));

        Collections.shuffle(areaCheckers, rand);

        boolean placed = false;
        for (Supplier<Boolean> checker : areaCheckers) {
            if (checker.get()) {
                placed = true;
                break;
            }
        }

        if (!placed) {
            throw new IllegalStateException("Could not initiate a valid farm map, please retry.");
        }

        for (int i = shippingRow; i < shippingRow + SHIPPING_BIN_HEIGHT; i++) {
            for (int j = shippingCol; j < shippingCol + SHIPPING_BIN_WIDTH; j++) {
                mapGrid[i][j] = 'S';
            }
        }
    }


    private boolean isRegionFree(int r, int c, int height, int width) {
        if (r < 0 || c < 0 || r + height > MAP_SIZE || c + width > MAP_SIZE) {
            return false;
        }
        for (int i = r; i < r + height; i++) {
            for (int j = c; j < c + width; j++) {
                if (mapGrid[i][j] != '.') return false;
            }
        }
        return true;
    }

    private void placeRandomObstacles() {
        Random rand = new Random();
        int obstacles = 30;
        for (int k = 0; k < obstacles; k++) {
            int r = rand.nextInt(MAP_SIZE);
            int c = rand.nextInt(MAP_SIZE);
            if (mapGrid[r][c] == '.') {
                mapGrid[r][c] = 'O';
            }
        }
    }

    public char getTileType(int r, int c) {
        if (r >= 0 && r < MAP_SIZE && c >= 0 && c < MAP_SIZE) {
            return mapGrid[r][c];
        }
        return '#';
    }

    public void draw(Graphics g, Component observer) {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                int x = j * TILE_SIZE;
                int y = i * TILE_SIZE;
                char tile = getTileType(i,j);

                switch (tile) {
                    case 'H':
                        if (houseTiles != null) {
                            int hr = i - houseRow;
                            int hc = j - houseCol;
                            if (hr >= 0 && hr < HOUSE_SIZE && hc >= 0 && hc < HOUSE_SIZE && houseTiles[hr][hc] != null) {
                                g.drawImage(houseTiles[hr][hc], x, y, TILE_SIZE, TILE_SIZE, observer);
                            } else {
                                g.setColor(Color.DARK_GRAY);
                                g.fillRect(x,y,TILE_SIZE,TILE_SIZE);
                            }
                        } else {
                            g.setColor(Color.DARK_GRAY);
                            g.fillRect(x,y,TILE_SIZE,TILE_SIZE);
                        }
                        break;
                    case 'S':
                        if (shippingBinTiles != null) {
                            int sr = i - shippingRow;
                            int sc = j - shippingCol;
                            if (sr >= 0 && sr < SHIPPING_BIN_HEIGHT && sc >=0 && sc < SHIPPING_BIN_WIDTH && shippingBinTiles[sr][sc] != null) {
                                g.drawImage(shippingBinTiles[sr][sc], x, y, TILE_SIZE, TILE_SIZE, observer);
                            } else {
                                g.setColor(Color.ORANGE);
                                g.fillRect(x,y,TILE_SIZE,TILE_SIZE);
                            }
                        } else {
                            g.setColor(Color.ORANGE);
                            g.fillRect(x,y,TILE_SIZE,TILE_SIZE);
                        }
                        break;
                    case 'W':
                        if (pondImage != null) {
                            g.drawImage(pondImage, x, y, TILE_SIZE, TILE_SIZE, observer);
                        } else {
                            g.setColor(Color.CYAN);
                            g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                        }
                        break;
                    case 'O':
                        if (tilledSoilImage != null) {
                            g.drawImage(tilledSoilImage, x, y, TILE_SIZE, TILE_SIZE, observer);
                        } else {
                            g.setColor(Color.GRAY);
                            g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                        }
                        break;
                    case '.':
                        if (floorImage != null) {
                            g.drawImage(floorImage, x, y, TILE_SIZE, TILE_SIZE, observer);
                        } else {
                            g.setColor(Color.GREEN.darker());
                            g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                        }
                        break;
                    default:
                        g.setColor(Color.BLACK);
                        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                        break;
                }
            }
        }
    }
    public int getHouseRow() { return houseRow; }
    public int getHouseCol() { return houseCol; }

}