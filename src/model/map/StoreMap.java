package model.map;

import model.entitas.NPC;
import model.entitas.Emily;
import model.items.Point;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StoreMap {
    public static final int MAP_SIZE = 32;
    public static final int TILE_SIZE = 24;

    private char[][] mapGrid = new char[MAP_SIZE][MAP_SIZE];
    private List<NPC> npcsInStore;
    private Emily emily;

    private Image floorImage;
    private Image wallImage;
    private Image counterImage;
    private Image shelfImage;
    private Image emilyImage;

    public StoreMap(Emily emily) {
        this.emily = emily;
        this.npcsInStore = new ArrayList<>();
        if (this.emily != null) {
            this.npcsInStore.add(this.emily);
        }
        loadImages();
        initializeMapGrid();
        placeStoreElements();
        if (this.emily != null) {
            Point emilyPosition = new Point(MAP_SIZE / 2, MAP_SIZE / 2 - 2);
            this.emily.moveTo(emilyPosition);
            this.emily.setLocation("Store");
            if (isValidTile(emilyPosition.getY(), emilyPosition.getX())) {
                mapGrid[emilyPosition.getY()][emilyPosition.getX()] = 'E';
            }
        }
    }

    private void loadImages() {
        floorImage = loadImage("/tiles/store/floor.png");
        wallImage = loadImage("/tiles/store/wall.png");
        counterImage = loadImage("/tiles/store/counter.png");
        shelfImage = loadImage("/tiles/store/shelf.png");
        if (emily != null && emily.getImagePath() != null) {
            emilyImage = loadImage(emily.getImagePath());
        } else {
            emilyImage = loadImage("/npcs/default_npc.png");
        }

        if (floorImage == null) floorImage = loadImage("/tiles/untilledSoilNew.png");
        if (wallImage == null) wallImage = loadImage("/tiles/house/houseBottom.png");
        if (counterImage == null) counterImage = loadImage("/tiles/shippingBin/shippingBin.png");
        if (shelfImage == null) shelfImage = loadImage("/tiles/farm_misc/placeholder_shelf.png");
    }

    private Image loadImage(String path) {
        URL imgUrl = getClass().getResource(path);
        if (imgUrl == null) {
            System.err.println("Warning: Could not load image at path: " + path);
            return null;
        }
        return new ImageIcon(imgUrl).getImage();
    }

    private void initializeMapGrid() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                mapGrid[i][j] = '.'; // Floor
            }
        }

        for (int i = 0; i < MAP_SIZE; i++) {
            mapGrid[i][0] = 'W';
            mapGrid[i][MAP_SIZE - 1] = 'W';
            mapGrid[0][i] = 'W';
            mapGrid[MAP_SIZE - 1][i] = 'W';
        }

        mapGrid[MAP_SIZE - 1][MAP_SIZE / 2] = '.';
        mapGrid[MAP_SIZE - 1][MAP_SIZE / 2 -1] = '.';
    }

    private void placeStoreElements() {
        int counterY = MAP_SIZE / 2 -1;
        for (int i = MAP_SIZE / 2 - 3; i <= MAP_SIZE / 2 + 3; i++) {
            if(isValidTile(counterY, i)) mapGrid[counterY][i] = 'C';
        }

        for (int i = 2; i < MAP_SIZE - 2; i += 5) {
            if(isValidTile(i, 2)) mapGrid[i][2] = 'L';
            if(isValidTile(i, MAP_SIZE - 3)) mapGrid[i][MAP_SIZE - 3] = 'L';
        }
    }

    private boolean isValidTile(int r, int c) {
        return r >= 0 && r < MAP_SIZE && c >= 0 && c < MAP_SIZE;
    }


    public char getTileType(int r, int c) {
        if (isValidTile(r,c)) {
            return mapGrid[r][c];
        }
        return '#';
    }

    public Emily getEmily() {
        return emily;
    }

    public NPC getNpcAt(Point p) {
        if (emily != null && emily.getCoordinate().equals(p)) {
            return emily;
        }
        return null;
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
                        toDraw = floorImage;
                        fallbackColor = new Color(200, 180, 150);
                        break;
                    case 'W':
                        toDraw = wallImage;
                        fallbackColor = new Color(100, 80, 60);
                        break;
                    case 'C':
                        toDraw = counterImage;
                        fallbackColor = new Color(150, 120, 90);
                        break;
                    case 'L':
                        toDraw = shelfImage;
                        fallbackColor = new Color(130, 100, 70);
                        break;
                    case 'E': // Emily
                        toDraw = floorImage;
                        fallbackColor = new Color(200, 180, 150);
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

        if (emily != null && emilyImage != null) {
            Point emilyCoord = emily.getCoordinate();
            g2d.drawImage(emilyImage,
                    emilyCoord.getX() * TILE_SIZE,
                    emilyCoord.getY() * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE, observer);
        }
    }
}