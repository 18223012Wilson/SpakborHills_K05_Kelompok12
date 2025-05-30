package model.map;

import model.entitas.NPC;
import model.items.Point;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPCMap {
    public static final int MAP_SIZE = 32;
    public static final int TILE_SIZE = 24;

    private char[][] mapGrid = new char[MAP_SIZE][MAP_SIZE];
    private List<NPC> npcs;
    private Map<String, Point> npcHouseTopLeftLocations;
    private Map<String, Point> npcStandingLocations;
    private Map<String, Image> npcImages;

    private Image grassTileImage;
    private Image[][] houseTiles = new Image[FarmMap.HOUSE_HEIGHT][FarmMap.HOUSE_WIDTH];

    public NPCMap(List<NPC> allNpcs) {
        this.npcs = new ArrayList<>(allNpcs);
        this.npcHouseTopLeftLocations = new HashMap<>();
        this.npcStandingLocations = new HashMap<>();
        this.npcImages = new HashMap<>();
        loadImages();
        initializeMapGrid();
        placeNPCHousesAndNPCs();
    }

    private void loadImages() {
        URL grassUrl = getClass().getResource("/tiles/untilledSoilNew.png");
        if (grassUrl != null) {
            grassTileImage = new ImageIcon(grassUrl).getImage();
        }

        String[][] housePaths = {
                {"/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png"},
                {"/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseChimney.png"},
                {"/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseTop.png","/tiles/house/houseRoof.png"},
                {"/tiles/house/houseTopMid.png","/tiles/house/houseTopMid.png","/tiles/house/houseTopMid.png","/tiles/house/houseTopMid.png","/tiles/house/houseTopMid.png","/tiles/house/houseTopMid.png"},
                {"/tiles/house/houseBottomLeft.png","/tiles/house/houseBottom.png","/tiles/house/houseBottom.png","/tiles/house/houseBottom.png","/tiles/house/houseBottom.png","/tiles/house/houseBottomRight.png"},
                {"/tiles/house/houseBottomLeft.png","/tiles/house/houseBottom.png","/tiles/house/houseDoor.png","/tiles/house/houseBottom.png","/tiles/house/houseBottom.png","/tiles/house/houseBottomRight.png"}
        };

        for (int i = 0; i < FarmMap.HOUSE_HEIGHT; i++) {
            for (int j = 0; j < FarmMap.HOUSE_WIDTH; j++) {
                URL imgUrl = getClass().getResource(housePaths[i][j]);
                if (imgUrl != null) {
                    houseTiles[i][j] = new ImageIcon(imgUrl).getImage();
                }
            }
        }

        // Load NPC images
        for (NPC npc : this.npcs) {
            if (npc.getImagePath() != null && !npc.getImagePath().isEmpty()) {
                URL npcImgUrl = getClass().getResource(npc.getImagePath());
                if (npcImgUrl != null) {
                    npcImages.put(npc.getName(), new ImageIcon(npcImgUrl).getImage());
                } else {
                    System.err.println("NPCMap: Could not load image for " + npc.getName() + " at path " + npc.getImagePath());
                    // Anda bisa me-load gambar default di sini jika gambar spesifik tidak ditemukan
                    URL defaultNpcImgUrl = getClass().getResource("/npcs/default_npc.png");
                    if (defaultNpcImgUrl != null) {
                        npcImages.put(npc.getName(), new ImageIcon(defaultNpcImgUrl).getImage());
                    }
                }
            }
        }
    }

    private void initializeMapGrid() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                mapGrid[i][j] = '.';
            }
        }
    }

    private void placeNPCHousesAndNPCs() {
        int housesPerRow = 3;
        int housePaddingX = 2;
        int housePaddingY = 3;
        int initialStartX = 2;
        int initialStartY = 3;

        int currentX = initialStartX;
        int currentY = initialStartY;
        int housesInCurrentRow = 0;

        for (NPC npc : npcs) {
            if (housesInCurrentRow >= housesPerRow || currentX + FarmMap.HOUSE_WIDTH > MAP_SIZE - 1) {
                currentX = initialStartX;
                currentY += FarmMap.HOUSE_HEIGHT + housePaddingY;
                housesInCurrentRow = 0;
            }

            if (currentY + FarmMap.HOUSE_HEIGHT > MAP_SIZE - 1) {
                System.err.println("NPCMap: Not enough space to place all NPC houses.");
                break;
            }

            Point houseTopLeft = new Point(currentX, currentY);
            npcHouseTopLeftLocations.put(npc.getName(), houseTopLeft);

            for (int r = 0; r < FarmMap.HOUSE_HEIGHT; r++) {
                for (int c = 0; c < FarmMap.HOUSE_WIDTH; c++) {
                    mapGrid[currentY + r][currentX + c] = 'N';
                }
            }

            int doorRelativeCol = 2;
            Point npcPosition = new Point(currentX + doorRelativeCol, currentY + FarmMap.HOUSE_HEIGHT);

            if (npcPosition.getY() < MAP_SIZE && mapGrid[npcPosition.getY()][npcPosition.getX()] == '.') {
                npcStandingLocations.put(npc.getName(), npcPosition);
                npc.moveTo(npcPosition);
                npc.setLocation("NPCMap");
            } else {
                npcPosition = new Point(currentX + doorRelativeCol, currentY + FarmMap.HOUSE_HEIGHT -1 );
                if (npcPosition.getY() < MAP_SIZE && npcPosition.getY() >=0 && mapGrid[npcPosition.getY()][npcPosition.getX()] == '.') {
                    npcStandingLocations.put(npc.getName(), npcPosition);
                    npc.moveTo(npcPosition);
                    npc.setLocation("NPCMap");
                } else {
                    System.err.println("NPCMap: Could not place NPC " + npc.getName() + " in front of house at " + houseTopLeft);
                }
            }
            currentX += FarmMap.HOUSE_WIDTH + housePaddingX;
            housesInCurrentRow++;
        }
    }


    public char getTileType(int r, int c) {
        if (r >= 0 && r < MAP_SIZE && c >= 0 && c < MAP_SIZE) {
            return mapGrid[r][c];
        }
        return '#';
    }

    public List<NPC> getNpcs() {
        return npcs;
    }

    public NPC getNpcAt(Point p) {
        for (NPC npc : npcs) {
            Point npcP = npcStandingLocations.get(npc.getName());
            if (npcP!= null && npcP.equals(p)) {
                return npc;
            }
        }
        return null;
    }

    public Point getNpcStandingLocation(String npcName) {
        return npcStandingLocations.get(npcName);
    }


    public void draw(Graphics2D g2d, Component observer) {
        // Draw tiles (grass and houses)
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                int x = j * TILE_SIZE;
                int y = i * TILE_SIZE;
                char tileChar = getTileType(i, j);
                Image toDraw = null;
                Color fallbackColor = null;

                if (tileChar == 'N') {
                    boolean houseDrawn = false;
                    for (Map.Entry<String, Point> entry : npcHouseTopLeftLocations.entrySet()) {
                        Point houseTopLeft = entry.getValue();
                        if (i >= houseTopLeft.getY() && i < houseTopLeft.getY() + FarmMap.HOUSE_HEIGHT &&
                                j >= houseTopLeft.getX() && j < houseTopLeft.getX() + FarmMap.HOUSE_WIDTH) {

                            int houseRelativeRow = i - houseTopLeft.getY();
                            int houseRelativeCol = j - houseTopLeft.getX();
                            toDraw = houseTiles[houseRelativeRow][houseRelativeCol];
                            if (toDraw == null) fallbackColor = Color.DARK_GRAY;
                            houseDrawn = true;
                            break;
                        }
                    }
                    if (!houseDrawn) {
                        toDraw = grassTileImage;
                        fallbackColor = new Color(34, 177, 76);
                    }
                }
                else if (tileChar == '.') {
                    toDraw = grassTileImage;
                    fallbackColor = new Color(34, 177, 76);
                }
                else {
                    fallbackColor = Color.BLACK;
                }

                if (toDraw != null) {
                    g2d.drawImage(toDraw, x, y, TILE_SIZE, TILE_SIZE, observer);
                }
                else if (fallbackColor != null) {
                    g2d.setColor(fallbackColor);
                    g2d.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        for (NPC npc : npcs) {
            Point npcCoord = npcStandingLocations.get(npc.getName());
            Image npcImage = npcImages.get(npc.getName());

            if (npcCoord != null && npcImage != null) {
                g2d.drawImage(npcImage,
                        npcCoord.getX() * TILE_SIZE,
                        npcCoord.getY() * TILE_SIZE,
                        TILE_SIZE, TILE_SIZE, observer);
            } else if (npcCoord != null) {
                g2d.setColor(new Color(128, 0, 128, 200));
                g2d.fillOval(npcCoord.getX() * TILE_SIZE + TILE_SIZE / 4,
                        npcCoord.getY() * TILE_SIZE + TILE_SIZE / 4,
                        TILE_SIZE / 2, TILE_SIZE / 2);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                String initial = npc.getName().length() > 0 ? npc.getName().substring(0, 1) : "?";
                FontMetrics fm = g2d.getFontMetrics();
                int stringX = npcCoord.getX() * TILE_SIZE + (TILE_SIZE - fm.stringWidth(initial)) / 2;
                int stringY = npcCoord.getY() * TILE_SIZE + (TILE_SIZE - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(initial, stringX, stringY);
            }
        }
    }
}