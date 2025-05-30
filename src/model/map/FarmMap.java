package model.map;

import model.calendar.GameCalendar;
import model.calendar.Season;
import model.calendar.Weather;
import model.items.Point;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class FarmMap {
    public static final int MAP_SIZE = 32;
    public static final int TILE_SIZE = 24;
    private char[][] mapGrid = new char[MAP_SIZE][MAP_SIZE];
    private Map<Point, PlantData> plantedCrops;

    private Image floorImage;
    private Image tilledSoilImage;
    private Image plantedSoilImage;
    private Image wateredPlantedSoilImage;
    private Image[][] houseTiles = new Image[6][6];
    private Image[][] shippingBinTiles = new Image[2][3];
    private Image pondImage;


    private int houseRow, houseCol;
    private int shippingRow, shippingCol;
    private int pondRow, pondCol;

    public static final int HOUSE_WIDTH = 6;
    public static final int HOUSE_HEIGHT = 6;
    private static final int SHIPPING_BIN_HEIGHT = 2;
    private static final int SHIPPING_BIN_WIDTH = 3;
    private static final int POND_HEIGHT = 3;
    private static final int POND_WIDTH = 4;

    private Image loadImage(String path) {
        URL imgUrl = getClass().getResource(path);
        if (imgUrl == null) {
            return null;
        }
        return new ImageIcon(imgUrl).getImage();
    }


    public FarmMap() {
        this.plantedCrops = new HashMap<>();

        floorImage = loadImage("/tiles/untilledSoilNew.png");
        tilledSoilImage = loadImage("/tiles/tilledSoil.png");
        plantedSoilImage = loadImage("/tiles/plantedSoil.png");
        wateredPlantedSoilImage = loadImage("/tiles/wateredPlantedSoil.png");
        pondImage = loadImage("/tiles/defaultPond.png");

        String[][] housePaths = {
                {"/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png"},
                {"/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseChimney.png"},
                {"/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseRoof.png"},
                {"/tiles/house/houseTopMid.png","/tiles/house/houseTopMid.png","/tiles/house/houseTopMid.png","/tiles/house/houseTopMid.png","/tiles/house/houseTopMid.png","/tiles/house/houseTopMid.png"},
                {"/tiles/house/houseBottomLeft.png","/tiles/house/houseBottom.png","/tiles/house/houseBottom.png","/tiles/house/houseBottom.png","/tiles/house/houseBottom.png","/tiles/house/houseBottomRight.png"},
                {"/tiles/house/houseBottomLeft.png","/tiles/house/houseBottom.png","/tiles/house/houseDoor.png","/tiles/house/houseBottom.png","/tiles/house/houseBottom.png","/tiles/house/houseBottomRight.png"}
        };

        for (int i = 0; i < HOUSE_HEIGHT; i++) {
            for (int j = 0; j < HOUSE_WIDTH; j++) {
                houseTiles[i][j] = loadImage(housePaths[i][j]);
            }
        }

        String[][] shippingPaths = {
                {"/tiles/shippingBin/shippingBin.png","/tiles/shippingBin/shippingBin.png","/tiles/shippingBin/shippingBin.png"},
                {"/tiles/shippingBin/shippingBin.png","/tiles/shippingBin/shippingBin.png","/tiles/shippingBin/shippingBin.png"}
        };
        for (int i = 0; i < SHIPPING_BIN_HEIGHT; i++) {
            for (int j = 0; j < SHIPPING_BIN_WIDTH; j++) {
                shippingBinTiles[i][j] = loadImage(shippingPaths[i][j]);
            }
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
    }

    private void placeHouseRandom() {
        Random rand = new Random();
        do {
            houseRow = rand.nextInt(MAP_SIZE - HOUSE_HEIGHT + 1);
            houseCol = rand.nextInt(MAP_SIZE - HOUSE_WIDTH + 1);
        }
        while (!isRegionFree(houseRow, houseCol, HOUSE_HEIGHT, HOUSE_WIDTH));
        for (int i = houseRow; i < houseRow + HOUSE_HEIGHT; i++) {
            for (int j = houseCol; j < houseCol + HOUSE_WIDTH; j++) {
                mapGrid[i][j] = 'H';
            }
        }
    }

    private void placePondRandom() {
        Random rand = new Random();
        do {
            pondRow = rand.nextInt(MAP_SIZE - POND_HEIGHT + 1);
            pondCol = rand.nextInt(MAP_SIZE - POND_WIDTH + 1);
        }
        while (!isRegionFree(pondRow, pondCol, POND_HEIGHT, POND_WIDTH));
        for (int i = pondRow; i < pondRow + POND_HEIGHT; i++) {
            for (int j = pondCol; j < pondCol + POND_WIDTH; j++) {
                mapGrid[i][j] = 'W';
            }
        }
    }

    private void placeShippingBinWithGap() {
        Random rand = new Random();
        List<Supplier<Boolean>> areaCheckers = new ArrayList<>(Arrays.asList(
                () -> {
                    int rBin = houseRow - 1 - SHIPPING_BIN_HEIGHT;
                    for (int cBin = houseCol; cBin <= houseCol + HOUSE_WIDTH - SHIPPING_BIN_WIDTH; cBin++) {
                        if (isRegionFree(rBin, cBin, SHIPPING_BIN_HEIGHT, SHIPPING_BIN_WIDTH)) {
                            shippingRow = rBin; shippingCol = cBin; return true;
                        }
                    } return false;
                },
                () -> {
                    int rBin = houseRow + HOUSE_HEIGHT + 1;
                    for (int cBin = houseCol; cBin <= houseCol + HOUSE_WIDTH - SHIPPING_BIN_WIDTH; cBin++) {
                        if (isRegionFree(rBin, cBin, SHIPPING_BIN_HEIGHT, SHIPPING_BIN_WIDTH)) {
                            shippingRow = rBin; shippingCol = cBin; return true;
                        }
                    } return false;
                },
                () -> {
                    int cBin = houseCol - 1 - SHIPPING_BIN_WIDTH;
                    for (int rBin = houseRow; rBin <= houseRow + HOUSE_HEIGHT - SHIPPING_BIN_HEIGHT; rBin++) {
                        if (isRegionFree(rBin, cBin, SHIPPING_BIN_HEIGHT, SHIPPING_BIN_WIDTH)) {
                            shippingRow = rBin; shippingCol = cBin; return true;
                        }
                    } return false;
                },
                () -> {
                    int cBin = houseCol + HOUSE_WIDTH + 1;
                    for (int rBin = houseRow; rBin <= houseRow + HOUSE_HEIGHT - SHIPPING_BIN_HEIGHT; rBin++) {
                        if (isRegionFree(rBin, cBin, SHIPPING_BIN_HEIGHT, SHIPPING_BIN_WIDTH)) {
                            shippingRow = rBin; shippingCol = cBin; return true;
                        }
                    } return false;
                }
        ));

        Collections.shuffle(areaCheckers, rand);
        boolean placed = areaCheckers.stream().anyMatch(Supplier::get);

        if (!placed) {
            do {
                shippingRow = rand.nextInt(MAP_SIZE - SHIPPING_BIN_HEIGHT + 1);
                shippingCol = rand.nextInt(MAP_SIZE - SHIPPING_BIN_WIDTH + 1);
            } while (!isRegionFree(shippingRow, shippingCol, SHIPPING_BIN_HEIGHT, SHIPPING_BIN_WIDTH));
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

    public char getTileType(int r, int c) {
        if (r >= 0 && r < MAP_SIZE && c >= 0 && c < MAP_SIZE) {
            return mapGrid[r][c];
        }
        return '#';
    }

    public void setTileType(int r, int c, char type) {
        if (r >= 0 && r < MAP_SIZE && c >= 0 && c < MAP_SIZE) {
            mapGrid[r][c] = type;
        }
    }

    public PlantData getPlantData(Point p) {
        return plantedCrops.get(p);
    }

    public void addPlantData(Point p, PlantData data) {
        plantedCrops.put(new Point(p.getX(), p.getY()), data);
    }

    public void removePlantData(Point p) {
        plantedCrops.remove(p);
    }

    public void draw(Graphics g, Component observer) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                int x = j * TILE_SIZE;
                int y = i * TILE_SIZE;
                char tile = getTileType(i, j);
                Point currentPoint = new Point(j,i);
                Image toDraw = null;
                Color fallbackColor = null;

                switch (tile) {
                    case 'H':
                        int hr = i - houseRow;
                        int hc = j - houseCol;
                        if (hr >= 0 && hr < HOUSE_HEIGHT && hc >= 0 && hc < HOUSE_WIDTH && houseTiles[hr][hc] != null) {
                            toDraw = houseTiles[hr][hc];
                        }
                        else {
                            fallbackColor = new Color(100,100,100);
                        }
                        break;
                    case 'S':
                        int sr = i - shippingRow;
                        int sc = j - shippingCol;
                        if (sr >= 0 && sr < SHIPPING_BIN_HEIGHT && sc >=0 && sc < SHIPPING_BIN_WIDTH && shippingBinTiles[sr][sc] != null) {
                            toDraw = shippingBinTiles[sr][sc];
                        }
                        else {
                            fallbackColor = new Color(139,69,19);
                        }
                        break;
                    case 'W':
                        toDraw = pondImage;
                        fallbackColor = Color.BLUE;
                        break;
                    case 't':
                        toDraw = tilledSoilImage;
                        fallbackColor = new Color(160, 82, 45);
                        break;
                    case 'l':
                        PlantData pd = getPlantData(currentPoint);
                        if (pd != null && pd.isWateredThisDayCycle() && wateredPlantedSoilImage != null) {
                            toDraw = wateredPlantedSoilImage;
                        }
                        else if (plantedSoilImage != null) {
                            toDraw = plantedSoilImage;
                        }
                        else {
                            fallbackColor = (pd != null && pd.isWateredThisDayCycle()) ? new Color(0, 100, 0) : new Color(34,139,34);
                        }
                        break;
                    case '.':
                        toDraw = floorImage;
                        fallbackColor = new Color(107, 142, 35);
                        break;
                    default:
                        fallbackColor = Color.BLACK;
                        break;
                }

                if (toDraw != null) {
                    g2d.drawImage(toDraw, x, y, TILE_SIZE, TILE_SIZE, observer);
                }
                else if (fallbackColor != null) {
                    g2d.setColor(fallbackColor);
                    g2d.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                }

                if (tile == 'l') {
                    PlantData pd = getPlantData(currentPoint);
                    if (pd != null && !pd.isReadyToHarvest()) {
                        g2d.setColor(new Color(255,255,255,180));
                        g2d.setFont(new Font("Arial", Font.BOLD, 9));
                        String growthText = pd.getGrowthDays() + "/" + pd.getSeed().getDaysToHarvest();
                        FontMetrics fm = g2d.getFontMetrics();
                        int textWidth = fm.stringWidth(growthText);
                        g2d.drawString(growthText, x + (TILE_SIZE - textWidth) / 2, y + TILE_SIZE - fm.getDescent() - 2);
                    } else if (pd != null && pd.isReadyToHarvest()) {
                        g2d.setColor(new Color(255, 215, 0, 200));
                        g2d.fillOval(x + TILE_SIZE/2 - 3, y + TILE_SIZE/2 -3, 6,6);
                    }
                }
            }
        }
    }

    public void dailyPlantUpdate(GameCalendar calendar) {
        List<Point> cropsToRemove = new ArrayList<>();
        List<Point> keysToUpdate = new ArrayList<>(plantedCrops.keySet());

        for (Point p : keysToUpdate) {
            PlantData plant = getPlantData(p);
            if (plant == null) continue;

            boolean survivesSeason = false;
            for (Season validSeason : plant.getSeed().getPlantingSeasons()) {
                if (validSeason == calendar.getSeason()) {
                    survivesSeason = true;
                    break;
                }
            }
            if (!survivesSeason) {
                cropsToRemove.add(p);
                continue;
            }
            plant.dailyResetAndGrowth(calendar);
        }

        for (Point p : cropsToRemove) {
            removePlantData(p);
            setTileType(p.getY(), p.getX(), 't');
        }
    }

    public int getHouseRow() {
    return houseRow;
    }
    public int getHouseCol() {
        return houseCol;
    }
    public int getShippingRow() {
        return shippingRow;
    }
    public int getShippingCol() {
        return shippingCol;
    }
    public int getPondRow() {
        return pondRow;
    }
    public int getPondCol() {
        return pondCol;
    }

    public static int getShippingBinHeight() {
        return SHIPPING_BIN_HEIGHT;
    }
    public static int getShippingBinWidth() {
        return SHIPPING_BIN_WIDTH;
    }

}