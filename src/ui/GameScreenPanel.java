package ui;

import main.Main;
import model.actions.Action;
import model.calendar.GameCalendar;
import model.calendar.Weather;
import model.entitas.Player;
import model.entitas.NPC;
import model.entitas.Emily;
import model.items.Point;
import model.items.Seed;
import model.items.Fish;
import model.map.FarmMap;
import model.map.NPCMap;
import model.map.StoreMap;
import model.map.ForestRiverMap;
import model.map.MountainLakeMap;
import model.map.OceanMap;
import model.map.PlantData;
import model.actions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameScreenPanel extends JPanel implements KeyListener {
    private Player player;
    private Object currentMap;
    private GameCalendar gameCalendar;
    private Image playerImage;
    private String popupMessage;
    private long messageTime;
    private Main mainFrame;

    private Point lastClickedTile = null;
    private InventoryDialog inventoryDialog;
    private StatisticsDialog statisticsDialog;

    public GameScreenPanel(Player player, GameCalendar gameCalendar, Main mainFrame) {
        this.player = player;
        this.gameCalendar = gameCalendar;
        this.mainFrame = mainFrame;
        this.currentMap = mainFrame.getCurrentMap();

        if (currentMap instanceof FarmMap) {
            setPreferredSize(new Dimension(FarmMap.MAP_SIZE * FarmMap.TILE_SIZE, FarmMap.MAP_SIZE * FarmMap.TILE_SIZE));
        }
        else if (currentMap instanceof NPCMap) {
            setPreferredSize(new Dimension(NPCMap.MAP_SIZE * NPCMap.TILE_SIZE, NPCMap.MAP_SIZE * NPCMap.TILE_SIZE));
        }
        else if (currentMap instanceof StoreMap) {
            setPreferredSize(new Dimension(StoreMap.MAP_SIZE * StoreMap.TILE_SIZE, StoreMap.MAP_SIZE * StoreMap.TILE_SIZE));
        }
        else if (currentMap instanceof ForestRiverMap) {
            setPreferredSize(new Dimension(ForestRiverMap.MAP_SIZE * ForestRiverMap.TILE_SIZE, ForestRiverMap.MAP_SIZE * ForestRiverMap.TILE_SIZE));
        }
        else if (currentMap instanceof MountainLakeMap) {
            setPreferredSize(new Dimension(MountainLakeMap.MAP_SIZE * MountainLakeMap.TILE_SIZE, MountainLakeMap.MAP_SIZE * MountainLakeMap.TILE_SIZE));
        }
        else if (currentMap instanceof OceanMap) {
            setPreferredSize(new Dimension(OceanMap.MAP_SIZE * OceanMap.TILE_SIZE, OceanMap.MAP_SIZE * OceanMap.TILE_SIZE));
        }


        setBackground(new Color(20, 80, 20));
        setFocusable(true);
        addKeyListener(this);

        URL playerImgUrl = getClass().getResource("/player.png");
        if (playerImgUrl != null) {
            playerImage = new ImageIcon(playerImgUrl).getImage();
            if (playerImage.getWidth(null) == -1) playerImage = null;
        }
        else {
            playerImage = null;
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Dialog modalDialog = findModalDialog();
                if (modalDialog != null && modalDialog.isVisible()) {
                    return;
                }

                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (mainFrame.getActionPanel().isVisible()) {
                        mainFrame.getActionPanel().clearActions();
                        popupMessage = null;
                    }
                    handleTileClick(e.getX(), e.getY());
                    repaint();
                }
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    mainFrame.getActionPanel().clearActions();
                    lastClickedTile = null;
                    popupMessage = null;
                    repaint();
                }
            }
        });
        new Timer(100, ev -> repaint()).start();
    }

    public Main getMainFrame() {
        return mainFrame;
    }

    public void setCurrentMap(Object map) {
        this.currentMap = map;
        int mapPixelSize = 0;
        if (map instanceof FarmMap) {
            mapPixelSize = FarmMap.MAP_SIZE * FarmMap.TILE_SIZE;
        }
        else if (map instanceof NPCMap) {
            mapPixelSize = NPCMap.MAP_SIZE * NPCMap.TILE_SIZE;
        }
        else if (map instanceof StoreMap) {
            mapPixelSize = StoreMap.MAP_SIZE * StoreMap.TILE_SIZE;
        }
        else if (map instanceof ForestRiverMap) {
            mapPixelSize = ForestRiverMap.MAP_SIZE * ForestRiverMap.TILE_SIZE;
        }
        else if (map instanceof MountainLakeMap) {
            mapPixelSize = MountainLakeMap.MAP_SIZE * MountainLakeMap.TILE_SIZE;
        }
        else if (map instanceof OceanMap) {
            mapPixelSize = OceanMap.MAP_SIZE * OceanMap.TILE_SIZE;
        }
        if (mapPixelSize > 0) {
            setPreferredSize(new Dimension(mapPixelSize, mapPixelSize));
        }
        revalidate();
        repaint();
    }


    private Dialog findModalDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        if (owner == null) return null;
        for (Window w : owner.getOwnedWindows()) {
            if (w instanceof JDialog && ((JDialog)w).isModal() && w.isVisible()) {
                return (Dialog) w;
            }
        }
        return null;
    }

    public GameCalendar getGameCalendar() { return gameCalendar; }
    public Player getPlayer() { return player; }

    private void handleTileClick(int mouseX, int mouseY) {
        int tileSize = FarmMap.TILE_SIZE;
        int tileX = mouseX / tileSize;
        int tileY = mouseY / tileSize;

        int mapWidth, mapHeight;
        if (currentMap instanceof FarmMap) {
            mapWidth = FarmMap.MAP_SIZE; mapHeight = FarmMap.MAP_SIZE;
        }
        else if (currentMap instanceof NPCMap) {
            mapWidth = NPCMap.MAP_SIZE; mapHeight = NPCMap.MAP_SIZE;
        }
        else if (currentMap instanceof StoreMap) {
            mapWidth = StoreMap.MAP_SIZE; mapHeight = StoreMap.MAP_SIZE;
        }
        else if (currentMap instanceof ForestRiverMap) {
            mapWidth = ForestRiverMap.MAP_SIZE; mapHeight = ForestRiverMap.MAP_SIZE;
        }
        else if (currentMap instanceof MountainLakeMap) {
            mapWidth = MountainLakeMap.MAP_SIZE; mapHeight = MountainLakeMap.MAP_SIZE;
        }
        else if (currentMap instanceof OceanMap) {
            mapWidth = OceanMap.MAP_SIZE; mapHeight = OceanMap.MAP_SIZE;
        }
        else {
            return;
        }

        if (tileX < 0 || tileX >= mapWidth || tileY < 0 || tileY >= mapHeight) {
            mainFrame.getActionPanel().clearActions();
            lastClickedTile = null;
            return;
        }

        Point clickedTile = new Point(tileX, tileY);
        Point playerCoordinate = player.getCoordinate();
        List<model.actions.Action> availableActions = new ArrayList<>();
        lastClickedTile = null;

        if (currentMap instanceof FarmMap farmMapInstance) {
            boolean onTile = playerCoordinate.getX() == tileX && playerCoordinate.getY() == tileY;
            boolean adjacentToClicked = Math.abs(playerCoordinate.getX() - tileX) <= 1 &&
                    Math.abs(playerCoordinate.getY() - tileY) <= 1 &&
                    !onTile;
            char targetTileType = farmMapInstance.getTileType(clickedTile.getY(), clickedTile.getX());

            if (onTile || adjacentToClicked) {
                lastClickedTile = clickedTile;
                if (targetTileType == '.') {
                    if (player.getInventory().getItemCountByName("Hoe") > 0) {
                        availableActions.add(new Tilling(player, gameCalendar, farmMapInstance, clickedTile));
                    }
                }
                else if (targetTileType == 't') {
                    if (!player.getInventory().getItemsByCategory("Seeds", Seed.class).isEmpty()) {
                        availableActions.add(new Planting(player, gameCalendar, farmMapInstance, clickedTile));
                    }
                    if (player.getInventory().getItemCountByName("Pickaxe") > 0) {
                        availableActions.add(new RecoverLand(player, gameCalendar, farmMapInstance, clickedTile));
                    }
                }
                else if (targetTileType == 'l') {
                    PlantData pd = farmMapInstance.getPlantData(clickedTile);
                    if (pd != null) {
                        if (pd.needsWateringToday(gameCalendar) && gameCalendar.getWeather() != Weather.RAINY && player.getInventory().getItemCountByName("Watering Can") > 0) {
                            availableActions.add(new Watering(player, gameCalendar, farmMapInstance, clickedTile));
                        }
                        if (pd.isReadyToHarvest()) {
                            availableActions.add(new Harvesting(player, gameCalendar, farmMapInstance, clickedTile));
                        }
                    }
                }
                else if (targetTileType == 'H') {
                    boolean isPlayerAdjacentToThisHouseTile = Math.abs(playerCoordinate.getX() - tileX) <= 1 && Math.abs(playerCoordinate.getY() - tileY) <= 1;
                    if(isPlayerAdjacentToThisHouseTile) {
                        availableActions.add(new Watching(player, gameCalendar));
                        availableActions.add(new Sleeping(player, gameCalendar));
                        availableActions.add(new CookingAction(player, gameCalendar, mainFrame));
                    }
                }
                else if (targetTileType == 'S') {
                    if (Math.abs(playerCoordinate.getX() - tileX) <= 1 && Math.abs(playerCoordinate.getY() - tileY) <= 1) {
                        availableActions.add(new Selling(player, gameCalendar, mainFrame));
                    }
                }
                else if (targetTileType == 'W') {
                    if (Math.abs(playerCoordinate.getX() - tileX) <= 1 && Math.abs(playerCoordinate.getY() - tileY) <= 1) {
                        if (player.getInventory().getItemCountByName("Fishing Rod") > 0) {
                            availableActions.add(new Fishing(player, gameCalendar, clickedTile));
                        }
                    }
                }
            }
        }
        else if (currentMap instanceof NPCMap npcMapInstance) {
            NPC npcAtClick = npcMapInstance.getNpcAt(clickedTile);
            if (npcAtClick != null && Math.abs(playerCoordinate.getX() - npcAtClick.getCoordinate().getX()) <=1 && Math.abs(playerCoordinate.getY() - npcAtClick.getCoordinate().getY()) <=1) {
                lastClickedTile = npcAtClick.getCoordinate();
                availableActions.add(new Chatting(player, gameCalendar, npcAtClick));
                availableActions.add(new InitiateGiftingUIAction(player, gameCalendar, npcAtClick, mainFrame, this));

                if (!npcAtClick.getRelationshipStatus().equals("fiance") && !npcAtClick.getRelationshipStatus().equals("spouse")) {
                    if (npcAtClick.getHeartPoints() == npcAtClick.getMaxHeartPoints()) {
                        availableActions.add(new Proposing(player, gameCalendar, npcAtClick));
                    }
                }
                else if (npcAtClick.getRelationshipStatus().equals("fiance")) {
                    availableActions.add(new Marrying(player, gameCalendar, npcAtClick, mainFrame));
                }
            }
        }
        else if (currentMap instanceof StoreMap storeMapInstance) {
            Emily emily = storeMapInstance.getEmily();
            if (emily != null && Math.abs(playerCoordinate.getX() - emily.getCoordinate().getX()) <=1 &&
                    Math.abs(playerCoordinate.getY() - emily.getCoordinate().getY()) <=1) {
                lastClickedTile = emily.getCoordinate();
                availableActions.add(new Chatting(player, gameCalendar, emily));
                availableActions.add(new InitiateGiftingUIAction(player, gameCalendar, emily, mainFrame, this));
                availableActions.add(new InitiateBuyingDialogAction(player, gameCalendar, emily, mainFrame, this));
                if (!emily.getRelationshipStatus().equals("fiance") && !emily.getRelationshipStatus().equals("spouse")) {
                    if (emily.getHeartPoints() == emily.getMaxHeartPoints()) {
                        availableActions.add(new Proposing(player, gameCalendar, emily));
                    }
                }
                else if (emily.getRelationshipStatus().equals("fiance")) {
                    availableActions.add(new Marrying(player, gameCalendar, emily, mainFrame));
                }
            }
            else if (storeMapInstance.getTileType(tileY, tileX) == 'C' &&
                    Math.abs(playerCoordinate.getX() - tileX) <=1 &&
                    Math.abs(playerCoordinate.getY() - tileY) <=1 ) {
                lastClickedTile = clickedTile;
                if (emily != null) {
                    availableActions.add(new InitiateBuyingDialogAction(player, gameCalendar, emily, mainFrame, this));
                }
            }
        }
        else if (currentMap instanceof ForestRiverMap forestRiverMapInstance) {
            char targetTileType = forestRiverMapInstance.getTileType(clickedTile.getY(), clickedTile.getX());
            if (targetTileType == 'R') {
                if (Math.abs(playerCoordinate.getX() - tileX) <= 1 && Math.abs(playerCoordinate.getY() - tileY) <= 1) {
                    if (player.getInventory().getItemCountByName("Fishing Rod") > 0) {
                        lastClickedTile = clickedTile;
                        availableActions.add(new Fishing(player, gameCalendar, clickedTile));
                    }
                }
            }
        }
        else if (currentMap instanceof MountainLakeMap mountainLakeMapInstance) {
            char targetTileType = mountainLakeMapInstance.getTileType(clickedTile.getY(), clickedTile.getX());
            if (targetTileType == 'L') {
                if (Math.abs(playerCoordinate.getX() - tileX) <= 1 && Math.abs(playerCoordinate.getY() - tileY) <= 1) {
                    if (player.getInventory().getItemCountByName("Fishing Rod") > 0) {
                        lastClickedTile = clickedTile;
                        availableActions.add(new Fishing(player, gameCalendar, clickedTile));
                    }
                }
            }
        }
        else if (currentMap instanceof OceanMap oceanMapInstance) {
            char targetTileType = oceanMapInstance.getTileType(clickedTile.getY(), clickedTile.getX());
            if (targetTileType == 'O') {
                if (Math.abs(playerCoordinate.getX() - tileX) <= 1 && Math.abs(playerCoordinate.getY() - tileY) <= 1) {
                    if (player.getInventory().getItemCountByName("Fishing Rod") > 0) {
                        lastClickedTile = clickedTile;
                        availableActions.add(new Fishing(player, gameCalendar, clickedTile));
                    }
                }
            }
        }


        if (!availableActions.isEmpty()) {
            mainFrame.getActionPanel().displayActions(availableActions, lastClickedTile != null ? lastClickedTile : clickedTile);
        }
        else {
            mainFrame.getActionPanel().clearActions();
            String msgToShow = "";
            if (currentMap instanceof FarmMap farmMapInstance) {
                char targetTileType = farmMapInstance.getTileType(clickedTile.getY(), clickedTile.getX());
                boolean onTile = playerCoordinate.getX() == tileX && playerCoordinate.getY() == tileY;
                boolean adjacentToClicked = Math.abs(playerCoordinate.getX() - tileX) <= 1 &&
                        Math.abs(playerCoordinate.getY() - tileY) <= 1 &&
                        !onTile;
                if (onTile || adjacentToClicked) {
                    if (targetTileType == 'H' && !(Math.abs(playerCoordinate.getX() - tileX) <= 1 && Math.abs(playerCoordinate.getY() - tileY) <= 1) ){
                        msgToShow = "Move closer to the house to interact.";
                    }
                    else if (targetTileType == 'H') {
                        msgToShow = "Near house. Press 'E' at the door or click the house.";
                    }
                    else {
                        msgToShow = "No actions for tile at (" + tileX + "," + tileY + ").";
                    }
                }
                else {
                    msgToShow = "Too far to interact with (" + tileX + "," + tileY + ").";
                }
            }
            else if (currentMap instanceof NPCMap) {
                msgToShow = "No direct actions for this tile on NPC map. Click on an NPC.";
            }
            else if (currentMap instanceof StoreMap) {
                msgToShow = "No actions here. Try clicking on Emily or the counter.";
            }
            else if (currentMap instanceof ForestRiverMap) {
                char targetTileType = ((ForestRiverMap)currentMap).getTileType(clickedTile.getY(), clickedTile.getX());
                if (targetTileType == 'R') {
                    msgToShow = "Move closer to the river to fish.";
                }
                else {
                    msgToShow = "Nothing to do here in the Forest River.";
                }
            }
            else if (currentMap instanceof MountainLakeMap) {
                char targetTileType = ((MountainLakeMap)currentMap).getTileType(clickedTile.getY(), clickedTile.getX());
                if (targetTileType == 'L') {
                    msgToShow = "Move closer to the lake to fish.";
                } else {
                    msgToShow = "Nothing to do here at the Mountain Lake.";
                }
            }
            else if (currentMap instanceof OceanMap) {
                char targetTileType = ((OceanMap)currentMap).getTileType(clickedTile.getY(), clickedTile.getX());
                if (targetTileType == 'O') {
                    msgToShow = "Move closer to the ocean to fish.";
                } else {
                    msgToShow = "Nothing to do here at the Ocean.";
                }
            }
            if(!msgToShow.isEmpty()) showMessage(msgToShow);
        }
        requestFocusInWindow();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int tileSize = FarmMap.TILE_SIZE;

        if (currentMap instanceof FarmMap farmMapInstance) {
            farmMapInstance.draw(g2d, this);
        }
        else if (currentMap instanceof NPCMap npcMapInstance) {
            npcMapInstance.draw(g2d, this);
        }
        else if (currentMap instanceof StoreMap storeMapInstance) {
            storeMapInstance.draw(g2d, this);
        }
        else if (currentMap instanceof ForestRiverMap forestRiverMapInstance) {
            forestRiverMapInstance.draw(g2d, this);
        }
        else if (currentMap instanceof MountainLakeMap mountainLakeMapInstance) {
            mountainLakeMapInstance.draw(g2d, this);
        }
        else if (currentMap instanceof OceanMap oceanMapInstance) {
            oceanMapInstance.draw(g2d, this);
        }

        Point playerCoord = player.getCoordinate();
        if (playerImage != null) {
            g2d.drawImage(playerImage, playerCoord.getX() * tileSize, playerCoord.getY() * tileSize, tileSize, tileSize, this);
        }
        else {
            g2d.setColor(new Color(0, 0, 200, 180));
            g2d.fillRect(playerCoord.getX() * tileSize, playerCoord.getY() * tileSize, tileSize, tileSize);
            g2d.setColor(Color.WHITE);
            g2d.drawString("P", playerCoord.getX() * tileSize + tileSize/3, playerCoord.getY() * tileSize + tileSize*2/3);
        }

        if (lastClickedTile != null && mainFrame.getActionPanel().isVisible()) {
            g2d.setColor(new Color(255, 255, 0, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.fillRect(lastClickedTile.getX() * tileSize, lastClickedTile.getY() * tileSize, tileSize, tileSize);
        }

        if (popupMessage != null && System.currentTimeMillis() - messageTime < 3000) {
            FontMetrics fm = g2d.getFontMetrics(new Font("Arial", Font.BOLD, 13));
            int msgWidth = fm.stringWidth(popupMessage) + 20;
            int msgHeight = fm.getHeight() + 10;
            int popupX = (getWidth() - msgWidth) / 2;
            int popupY = mainFrame.getActionPanel().isVisible() ?
                    mainFrame.getActionPanel().getY() - msgHeight - 5 :
                    getHeight() - msgHeight - 10;
            if (popupY < 5) popupY = 5;

            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRoundRect(popupX, popupY, msgWidth, msgHeight, 15, 15);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 13));
            g2d.drawString(popupMessage, popupX + 10, popupY + fm.getAscent() + 5);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Dialog modalDialog = findModalDialog();
        if (modalDialog != null && modalDialog.isVisible()) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (modalDialog instanceof InventoryDialog || modalDialog instanceof CookingDialog ||
                        modalDialog instanceof ShippingBinPanel || modalDialog instanceof GiftingDialog ||
                        modalDialog instanceof BuyingDialog || modalDialog instanceof StatisticsDialog ||
                        modalDialog instanceof EndGameStatsDialog ) {
                    modalDialog.dispatchEvent(new WindowEvent(modalDialog, WindowEvent.WINDOW_CLOSING));
                }
            }
            return;
        }

        int key = e.getKeyCode();
        Point currentCoordinate = player.getCoordinate();
        int newX = currentCoordinate.getX();
        int newY = currentCoordinate.getY();
        boolean moved = false;

        switch (key) {
            case KeyEvent.VK_W: case KeyEvent.VK_UP:    newY--; moved = true; break;
            case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  newY++; moved = true; break;
            case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  newX--; moved = true; break;
            case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: newX++; moved = true; break;
            case KeyEvent.VK_E:
                handleInteractionKey();
                repaint();
                return;
            case KeyEvent.VK_I:
                if (inventoryDialog == null || !inventoryDialog.isVisible()) {
                    inventoryDialog = new InventoryDialog(mainFrame, player, player.getInventory(), gameCalendar, this);
                    inventoryDialog.setVisible(true);
                }
                else {
                    inventoryDialog.dispatchEvent(new WindowEvent(inventoryDialog, WindowEvent.WINDOW_CLOSING));
                }
                return;
            case KeyEvent.VK_1:
                if (statisticsDialog == null || !statisticsDialog.isVisible()) {
                    gameCalendar.pauseTime();
                    statisticsDialog = new StatisticsDialog(mainFrame, player, gameCalendar, mainFrame.getNpcList(), mainFrame);
                    statisticsDialog.setVisible(true);
                }
                else {
                    statisticsDialog.dispatchEvent(new WindowEvent(statisticsDialog, WindowEvent.WINDOW_CLOSING));
                }
                return;
            case KeyEvent.VK_ESCAPE:
                mainFrame.getActionPanel().clearActions();
                lastClickedTile = null;
                popupMessage = null;
                repaint();
                return;
            default: return;
        }

        if (moved) {
            if (isValidMove(newX, newY)) {
                player.moveTo(new Point(newX, newY));
                checkAndSetPlayerLocationForMapTransition(newX, newY);
                mainFrame.getActionPanel().clearActions();
                lastClickedTile = null;
                popupMessage = null;
            }
            else {
                showMessage("Cannot move there!");
            }
            repaint();
        }
    }

    private void checkAndSetPlayerLocationForMapTransition(int x, int y) {
        if (currentMap instanceof FarmMap) {
            if (x == 0 || x == FarmMap.MAP_SIZE -1 || y == 0 || y == FarmMap.MAP_SIZE - 1) {
                player.setLocation("EdgeOfFarm");
            }
            else if (x >= ((FarmMap)currentMap).getHouseCol() && x < ((FarmMap)currentMap).getHouseCol() + FarmMap.HOUSE_WIDTH &&
                    y >= ((FarmMap)currentMap).getHouseRow() && y < ((FarmMap)currentMap).getHouseRow() + FarmMap.HOUSE_HEIGHT) {
                player.setLocation("House Interior");
            }
            else if (!player.getLocation().equals("House") && !player.getLocation().equals("House Interior")){
                player.setLocation("Farm");
            }
        }
        else if (currentMap instanceof NPCMap) {
            if (x == 0 || x == NPCMap.MAP_SIZE -1 || y == 0 || y == NPCMap.MAP_SIZE - 1) {
                player.setLocation("EdgeOfNPCMap");
            }
            else {
                player.setLocation("NPCMap");
            }
        } else if (currentMap instanceof StoreMap) {
            if (x == 0 || x == StoreMap.MAP_SIZE -1 || y == 0 || y == StoreMap.MAP_SIZE - 1) {
                player.setLocation("EdgeOfStore");
            }
            else {
                player.setLocation("Store");
            }
        } else if (currentMap instanceof ForestRiverMap) {
            if (x == 0 || x == ForestRiverMap.MAP_SIZE -1 || y == 0 || y == ForestRiverMap.MAP_SIZE - 1) {
                player.setLocation("EdgeOfForestRiver");
            }
            else {
                player.setLocation("Forest River");
            }
        } else if (currentMap instanceof MountainLakeMap) {
            if (x == 0 || x == MountainLakeMap.MAP_SIZE -1 || y == 0 || y == MountainLakeMap.MAP_SIZE - 1) {
                player.setLocation("EdgeOfMountainLake");
            }
            else {
                player.setLocation("Mountain Lake");
            }
        } else if (currentMap instanceof OceanMap) {
            if (x == 0 || x == OceanMap.MAP_SIZE -1 || y == 0 || y == OceanMap.MAP_SIZE - 1) {
                player.setLocation("EdgeOfOcean");
            }
            else {
                player.setLocation("Ocean");
            }
        }
    }


    private void handleInteractionKey() {
        Point playerPos = player.getCoordinate();
        List<Action> availableActions = new ArrayList<>();
        Point interactionTile = null;

        String playerLocation = player.getLocation();

        if (playerLocation.equals("EdgeOfFarm")) {
            availableActions.add(new Visiting(player, gameCalendar, mainFrame));
            availableActions.add(new VisitStoreAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitForestRiverAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitMountainLakeAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitOcean(player, gameCalendar, mainFrame));
            interactionTile = playerPos;
        }
        else if (playerLocation.equals("EdgeOfNPCMap")) {
            availableActions.add(new GoHome(player, gameCalendar, mainFrame));
            availableActions.add(new VisitStoreAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitForestRiverAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitMountainLakeAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitOcean(player, gameCalendar, mainFrame));
            interactionTile = playerPos;
        }
        else if (playerLocation.equals("EdgeOfStore")) {
            availableActions.add(new GoHome(player, gameCalendar, mainFrame));
            availableActions.add(new Visiting(player, gameCalendar, mainFrame));
            availableActions.add(new VisitForestRiverAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitMountainLakeAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitOcean(player, gameCalendar, mainFrame));
            interactionTile = playerPos;
        }
        else if (playerLocation.equals("EdgeOfForestRiver")) {
            availableActions.add(new GoHome(player, gameCalendar, mainFrame));
            availableActions.add(new Visiting(player, gameCalendar, mainFrame));
            availableActions.add(new VisitStoreAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitMountainLakeAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitOcean(player, gameCalendar, mainFrame));
            interactionTile = playerPos;
        }
        else if (playerLocation.equals("EdgeOfMountainLake")) {
            availableActions.add(new GoHome(player, gameCalendar, mainFrame));
            availableActions.add(new Visiting(player, gameCalendar, mainFrame));
            availableActions.add(new VisitStoreAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitForestRiverAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitOcean(player, gameCalendar, mainFrame));
            interactionTile = playerPos;
        }
        else if (playerLocation.equals("EdgeOfOcean")) {
            availableActions.add(new GoHome(player, gameCalendar, mainFrame));
            availableActions.add(new Visiting(player, gameCalendar, mainFrame));
            availableActions.add(new VisitStoreAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitForestRiverAction(player, gameCalendar, mainFrame));
            availableActions.add(new VisitMountainLakeAction(player, gameCalendar, mainFrame));
            interactionTile = playerPos;
        }
        else if (currentMap instanceof FarmMap farmMapInstance) {
            Point[] checkTiles = {
                    new Point(playerPos.getX(), playerPos.getY() - 1), new Point(playerPos.getX(), playerPos.getY() + 1),
                    new Point(playerPos.getX() - 1, playerPos.getY()), new Point(playerPos.getX() + 1, playerPos.getY())
            };
            for (Point adjTilePos : checkTiles) {
                if (adjTilePos.getX() >= 0 && adjTilePos.getX() < FarmMap.MAP_SIZE &&
                        adjTilePos.getY() >= 0 && adjTilePos.getY() < FarmMap.MAP_SIZE) {
                    char tileType = farmMapInstance.getTileType(adjTilePos.getY(), adjTilePos.getX());
                    if (tileType == 'H') {
                        int doorCol = farmMapInstance.getHouseCol() + 2;
                        int doorRow = farmMapInstance.getHouseRow() + FarmMap.HOUSE_HEIGHT -1;
                        if (adjTilePos.getX() == doorCol && adjTilePos.getY() == doorRow) {
                            player.setLocation("House");
                            availableActions.add(new Watching(player, gameCalendar));
                            availableActions.add(new Sleeping(player, gameCalendar));
                            availableActions.add(new CookingAction(player, gameCalendar, mainFrame));
                            interactionTile = adjTilePos; break;
                        }
                        else {
                            showMessage("This is part of your house. Interact with the door.");
                            interactionTile = adjTilePos; break;
                        }
                    }
                    else if (tileType == 'S') {
                        player.setLocation("Shipping Bin");
                        availableActions.add(new Selling(player, gameCalendar, mainFrame));
                        interactionTile = adjTilePos; break;
                    }
                    else if (tileType == 'W') {
                        if (player.getInventory().getItemCountByName("Fishing Rod") > 0) {
                            player.setLocation("Pond");
                            availableActions.add(new Fishing(player, gameCalendar, adjTilePos));
                            interactionTile = adjTilePos;
                        }
                        else {
                            showMessage("Need a Fishing Rod to fish here.");
                        }
                        break;
                    }
                }
            }
        }
        else if (currentMap instanceof NPCMap npcMapInstance) {
            Point[] checkTilesNPC = {
                    new Point(playerPos.getX(), playerPos.getY() - 1), new Point(playerPos.getX(), playerPos.getY() + 1),
                    new Point(playerPos.getX() - 1, playerPos.getY()), new Point(playerPos.getX() + 1, playerPos.getY()),
                    playerPos
            };
            NPC foundNpc = null;
            for(Point p : checkTilesNPC) {
                if (p.getX() >= 0 && p.getX() < NPCMap.MAP_SIZE && p.getY() >= 0 && p.getY() < NPCMap.MAP_SIZE) {
                    NPC npc = npcMapInstance.getNpcAt(p);
                    if (npc != null) {
                        foundNpc = npc;
                        interactionTile = p;
                        break;
                    }
                }
            }
            if (foundNpc != null) {
                availableActions.add(new Chatting(player, gameCalendar, foundNpc));
                availableActions.add(new InitiateGiftingUIAction(player, gameCalendar, foundNpc, mainFrame, this));
                if (!foundNpc.getRelationshipStatus().equals("fiance") && !foundNpc.getRelationshipStatus().equals("spouse")) {
                    if (foundNpc.getHeartPoints() == foundNpc.getMaxHeartPoints()) {
                        availableActions.add(new Proposing(player, gameCalendar, foundNpc));
                    }
                }
                else if (foundNpc.getRelationshipStatus().equals("fiance")) {
                    availableActions.add(new Marrying(player, gameCalendar, foundNpc, mainFrame));
                }
            }
            else {
                if (playerPos.getX() == 0 || playerPos.getX() == NPCMap.MAP_SIZE -1 ||
                        playerPos.getY() == 0 || playerPos.getY() == NPCMap.MAP_SIZE - 1) {
                    player.setLocation("EdgeOfNPCMap");
                    availableActions.add(new GoHome(player, gameCalendar, mainFrame));
                    availableActions.add(new VisitStoreAction(player, gameCalendar, mainFrame));
                    availableActions.add(new VisitForestRiverAction(player, gameCalendar, mainFrame));
                    availableActions.add(new VisitMountainLakeAction(player, gameCalendar, mainFrame));
                    availableActions.add(new VisitOcean(player, gameCalendar, mainFrame));
                    interactionTile = playerPos;
                }
            }
        }
        else if (currentMap instanceof StoreMap storeMapInstance) {
            Emily emily = storeMapInstance.getEmily();
            Point[] checkTilesStore = {
                    new Point(playerPos.getX(), playerPos.getY() - 1), new Point(playerPos.getX(), playerPos.getY() + 1),
                    new Point(playerPos.getX() - 1, playerPos.getY()), new Point(playerPos.getX() + 1, playerPos.getY()),
                    playerPos
            };
            boolean interactedWithEmily = false;
            for (Point p : checkTilesStore) {
                if (p.getX() >= 0 && p.getX() < StoreMap.MAP_SIZE && p.getY() >= 0 && p.getY() < StoreMap.MAP_SIZE) {
                    if (emily != null && emily.getCoordinate().equals(p)) {
                        interactionTile = p;
                        availableActions.add(new Chatting(player, gameCalendar, emily));
                        availableActions.add(new InitiateGiftingUIAction(player, gameCalendar, emily, mainFrame, this));
                        availableActions.add(new InitiateBuyingDialogAction(player, gameCalendar, emily, mainFrame, this));
                        if (!emily.getRelationshipStatus().equals("fiance") && !emily.getRelationshipStatus().equals("spouse")) {
                            if (emily.getHeartPoints() == emily.getMaxHeartPoints()) {
                                availableActions.add(new Proposing(player, gameCalendar, emily));
                            }
                        }
                        else if (emily.getRelationshipStatus().equals("fiance")) {
                            availableActions.add(new Marrying(player, gameCalendar, emily, mainFrame));
                        }
                        interactedWithEmily = true;
                        break;
                    }
                }
            }
            if (!interactedWithEmily) {
                for (Point p : checkTilesStore) {
                    if (p.getX() >= 0 && p.getX() < StoreMap.MAP_SIZE && p.getY() >= 0 && p.getY() < StoreMap.MAP_SIZE) {
                        char tileType = storeMapInstance.getTileType(p.getY(), p.getX());
                        if (tileType == 'C' && emily != null) {
                            interactionTile = p;
                            availableActions.add(new InitiateBuyingDialogAction(player, gameCalendar, emily, mainFrame, this));
                            break;
                        }
                    }
                }
                if (availableActions.isEmpty() && (playerPos.getX() == 0 || playerPos.getX() == StoreMap.MAP_SIZE - 1 ||
                        playerPos.getY() == 0 || playerPos.getY() == StoreMap.MAP_SIZE - 1)) {
                    player.setLocation("EdgeOfStore");
                    availableActions.add(new GoHome(player, gameCalendar, mainFrame));
                    availableActions.add(new Visiting(player, gameCalendar, mainFrame));
                    availableActions.add(new VisitForestRiverAction(player, gameCalendar, mainFrame));
                    availableActions.add(new VisitMountainLakeAction(player, gameCalendar, mainFrame));
                    availableActions.add(new VisitOcean(player, gameCalendar, mainFrame));
                    interactionTile = playerPos;
                }
            }
        }
        else if (currentMap instanceof ForestRiverMap forestRiverMapInstance) {
            Point[] checkTilesRiver = {
                    new Point(playerPos.getX(), playerPos.getY() - 1), new Point(playerPos.getX(), playerPos.getY() + 1),
                    new Point(playerPos.getX() - 1, playerPos.getY()), new Point(playerPos.getX() + 1, playerPos.getY())
            };
            for (Point adjTilePos : checkTilesRiver) {
                if (adjTilePos.getX() >= 0 && adjTilePos.getX() < ForestRiverMap.MAP_SIZE &&
                        adjTilePos.getY() >= 0 && adjTilePos.getY() < ForestRiverMap.MAP_SIZE) {
                    char tileType = forestRiverMapInstance.getTileType(adjTilePos.getY(), adjTilePos.getX());
                    if (tileType == 'R') {
                        if (player.getInventory().getItemCountByName("Fishing Rod") > 0) {
                            player.setLocation("Forest River");
                            availableActions.add(new Fishing(player, gameCalendar, adjTilePos));
                            interactionTile = adjTilePos;
                        } else {
                            showMessage("Need a Fishing Rod to fish here.");
                        }
                        break;
                    }
                }
            }
            if (availableActions.isEmpty() && (playerPos.getX() == 0 || playerPos.getX() == ForestRiverMap.MAP_SIZE - 1 ||
                    playerPos.getY() == 0 || playerPos.getY() == ForestRiverMap.MAP_SIZE - 1)) {
                player.setLocation("EdgeOfForestRiver");
                availableActions.add(new GoHome(player, gameCalendar, mainFrame));
                availableActions.add(new Visiting(player, gameCalendar, mainFrame));
                availableActions.add(new VisitStoreAction(player, gameCalendar, mainFrame));
                availableActions.add(new VisitMountainLakeAction(player, gameCalendar, mainFrame));
                availableActions.add(new VisitOcean(player, gameCalendar, mainFrame));
                interactionTile = playerPos;
            }
        }
        else if (currentMap instanceof MountainLakeMap mountainLakeMapInstance) {
            Point[] checkTilesLake = {
                    new Point(playerPos.getX(), playerPos.getY() - 1), new Point(playerPos.getX(), playerPos.getY() + 1),
                    new Point(playerPos.getX() - 1, playerPos.getY()), new Point(playerPos.getX() + 1, playerPos.getY())
            };
            for (Point adjTilePos : checkTilesLake) {
                if (adjTilePos.getX() >= 0 && adjTilePos.getX() < MountainLakeMap.MAP_SIZE &&
                        adjTilePos.getY() >= 0 && adjTilePos.getY() < MountainLakeMap.MAP_SIZE) {
                    char tileType = mountainLakeMapInstance.getTileType(adjTilePos.getY(), adjTilePos.getX());
                    if (tileType == 'L') {
                        if (player.getInventory().getItemCountByName("Fishing Rod") > 0) {
                            player.setLocation("Mountain Lake");
                            availableActions.add(new Fishing(player, gameCalendar, adjTilePos));
                            interactionTile = adjTilePos;
                        } else {
                            showMessage("Need a Fishing Rod to fish here.");
                        }
                        break;
                    }
                }
            }
            if (availableActions.isEmpty() && (playerPos.getX() == 0 || playerPos.getX() == MountainLakeMap.MAP_SIZE - 1 ||
                    playerPos.getY() == 0 || playerPos.getY() == MountainLakeMap.MAP_SIZE - 1)) {
                player.setLocation("EdgeOfMountainLake");
                availableActions.add(new GoHome(player, gameCalendar, mainFrame));
                availableActions.add(new Visiting(player, gameCalendar, mainFrame));
                availableActions.add(new VisitStoreAction(player, gameCalendar, mainFrame));
                availableActions.add(new VisitForestRiverAction(player, gameCalendar, mainFrame));
                availableActions.add(new VisitOcean(player, gameCalendar, mainFrame));
                interactionTile = playerPos;
            }
        }
        else if (currentMap instanceof OceanMap oceanMapInstance) {
            Point[] checkTilesOcean = {
                    new Point(playerPos.getX(), playerPos.getY() - 1), new Point(playerPos.getX(), playerPos.getY() + 1),
                    new Point(playerPos.getX() - 1, playerPos.getY()), new Point(playerPos.getX() + 1, playerPos.getY())
            };
            for (Point adjTilePos : checkTilesOcean) {
                if (adjTilePos.getX() >= 0 && adjTilePos.getX() < OceanMap.MAP_SIZE &&
                        adjTilePos.getY() >= 0 && adjTilePos.getY() < OceanMap.MAP_SIZE) {
                    char tileType = oceanMapInstance.getTileType(adjTilePos.getY(), adjTilePos.getX());
                    if (tileType == 'O') {
                        if (player.getInventory().getItemCountByName("Fishing Rod") > 0) {
                            player.setLocation("Ocean");
                            availableActions.add(new Fishing(player, gameCalendar, adjTilePos));
                            interactionTile = adjTilePos;
                        } else {
                            showMessage("Need a Fishing Rod to fish here.");
                        }
                        break;
                    }
                }
            }
            if (availableActions.isEmpty() && (playerPos.getX() == 0 || playerPos.getX() == OceanMap.MAP_SIZE - 1 ||
                    playerPos.getY() == 0 || playerPos.getY() == OceanMap.MAP_SIZE - 1)) {
                player.setLocation("EdgeOfOcean");
                availableActions.add(new GoHome(player, gameCalendar, mainFrame));
                availableActions.add(new Visiting(player, gameCalendar, mainFrame));
                availableActions.add(new VisitStoreAction(player, gameCalendar, mainFrame));
                availableActions.add(new VisitForestRiverAction(player, gameCalendar, mainFrame));
                availableActions.add(new VisitMountainLakeAction(player, gameCalendar, mainFrame));
                interactionTile = playerPos;
            }
        }


        if (!availableActions.isEmpty()) {
            lastClickedTile = interactionTile;
            mainFrame.getActionPanel().displayActions(availableActions, interactionTile);
        }
        else if (interactionTile == null) {
            showMessage("Nothing here.");
            mainFrame.getActionPanel().clearActions();
            lastClickedTile = null;
        }
    }


    public void showMessage(String msg) {
        this.popupMessage = msg;
        this.messageTime = System.currentTimeMillis();
        repaint();
    }

    private boolean isValidMove(int x, int y) {
        if (currentMap instanceof FarmMap farmMapInstance) {
            if (x < 0 || x >= FarmMap.MAP_SIZE || y < 0 || y >= FarmMap.MAP_SIZE) return false;
            char tileType = farmMapInstance.getTileType(y, x);
            return tileType == '.' || tileType == 't' || tileType == 'l';
        }
        else if (currentMap instanceof NPCMap npcMapInstance) {
            if (x < 0 || x >= NPCMap.MAP_SIZE || y < 0 || y >= NPCMap.MAP_SIZE) return false;
            char tileType = npcMapInstance.getTileType(y, x);
            return tileType == '.';
        }
        else if (currentMap instanceof StoreMap storeMapInstance) {
            if (x < 0 || x >= StoreMap.MAP_SIZE || y < 0 || y >= StoreMap.MAP_SIZE) return false;
            char tileType = storeMapInstance.getTileType(y,x);
            return tileType == '.' || tileType == 'E';
        }
        else if (currentMap instanceof ForestRiverMap forestRiverMapInstance) {
            if (x < 0 || x >= ForestRiverMap.MAP_SIZE || y < 0 || y >= ForestRiverMap.MAP_SIZE) return false;
            char tileType = forestRiverMapInstance.getTileType(y,x);
            return tileType == '.' || tileType == 'B';
        }
        else if (currentMap instanceof MountainLakeMap mountainLakeMapInstance) {
            if (x < 0 || x >= MountainLakeMap.MAP_SIZE || y < 0 || y >= MountainLakeMap.MAP_SIZE) return false;
            char tileType = mountainLakeMapInstance.getTileType(y,x);
            return tileType == '.';
        }
        else if (currentMap instanceof OceanMap oceanMapInstance) {
            if (x < 0 || x >= OceanMap.MAP_SIZE || y < 0 || y >= OceanMap.MAP_SIZE) return false;
            char tileType = oceanMapInstance.getTileType(y,x);
            return tileType == '.' || tileType == 'B';
        }
        return false;
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}