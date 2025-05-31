package main;

import model.calendar.GameCalendar;
import model.entitas.Player;
import model.entitas.NPC;
import model.entitas.Abigail;
import model.entitas.Caroline;
import model.entitas.Dasco;
import model.entitas.Perry;
import model.entitas.Tadi;
import model.entitas.Emily;
import model.items.Point;
import model.map.FarmMap;
import model.map.NPCMap;
import model.map.StoreMap;
import model.map.ForestRiverMap;
import model.map.MountainLakeMap;
import model.map.OceanMap;
import ui.*; // Import all ui package classes

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {

    private GameCalendar gameCalendar;
    private Thread calendarThread;
    private GameScreenPanel gameScreenPanel;
    private StatusBarPanel statusBarPanel;
    private ActionPanel actionPanel;
    private Player player;
    private FarmMap farmMap;
    private NPCMap npcMap;
    private StoreMap storeMap;
    private ForestRiverMap forestRiverMap;
    private MountainLakeMap mountainLakeMap;
    private OceanMap oceanMap;
    private List<NPC> npcList;
    private Emily emily;

    private Object currentMap;
    private ShippingBinPanel shippingBinPanel;

    private MainMenuPanel mainMenuPanel;
    private CreditsPanel creditsPanel;
    private StoryIntroPanel storyIntroPanel;
    private EndGameStatsDialog endGameStatsDialog;


    public Main() {
        setTitle("BorborSpakbor Hills");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        showMainMenu();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (creditsPanel != null && creditsPanel.isVisible()) {
                    creditsPanel.stopCredits();
                    showMainMenu();
                } else if (storyIntroPanel != null && storyIntroPanel.isVisible()) {
                    storyIntroPanel.stopIntro();
                    showMainMenu();
                } else if (endGameStatsDialog != null && endGameStatsDialog.isVisible()) {
                    endGameStatsDialog.dispatchEvent(new WindowEvent(endGameStatsDialog, WindowEvent.WINDOW_CLOSING));
                }
                else if (gameScreenPanel != null && gameScreenPanel.isVisible()) {
                    int confirm = JOptionPane.showConfirmDialog(
                            Main.this,
                            "Are you sure you want to exit Spakbor Hills?",
                            "Exit Confirmation",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        performShutdown();
                    }
                } else {
                    System.exit(0);
                }
            }
        });
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showMainMenu() {
        setTitle("Spakbor Hills");
        getContentPane().removeAll();

        if (mainMenuPanel == null) {
            mainMenuPanel = new MainMenuPanel(this);
        }
        add(mainMenuPanel, BorderLayout.CENTER);
        mainMenuPanel.setVisible(true);

        if (gameCalendar != null) gameCalendar.stop();
        if (calendarThread != null && calendarThread.isAlive()) {
            try {
                calendarThread.join(100);
                if(calendarThread.isAlive()) calendarThread.interrupt();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        gameScreenPanel = null;
        statusBarPanel = null;
        actionPanel = null;


        pack();
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        revalidate();
        repaint();
        mainMenuPanel.requestFocusInWindow();
    }

    public void showCreditsScreen() {
        getContentPane().removeAll();

        if (creditsPanel == null) {
            creditsPanel = new CreditsPanel(this);
        }
        add(creditsPanel, BorderLayout.CENTER);
        creditsPanel.setVisible(true);

        setTitle("Spakbor Hills - Credits");
        setSize(1200, 800);
        setMinimumSize(new Dimension(1200,800));
        setLocationRelativeTo(null);
        revalidate();
        repaint();
        creditsPanel.startCredits();
        creditsPanel.requestFocusInWindow();
    }

    public void startGame(String playerNameInput, String playerGender) {
        getContentPane().removeAll();

        if (storyIntroPanel == null) {
            storyIntroPanel = new StoryIntroPanel(this, playerNameInput, playerGender);
        } else {
            storyIntroPanel.resetAndPrepare(playerNameInput,playerGender);
        }
        add(storyIntroPanel, BorderLayout.CENTER);
        storyIntroPanel.setVisible(true);

        setTitle("Spakbor Hills - Kisah Dimulai...");
        setSize(1200, 800);
        setMinimumSize(new Dimension(1200,800));
        setLocationRelativeTo(null);
        revalidate();
        repaint();
        storyIntroPanel.startIntro();
        storyIntroPanel.requestFocusInWindow();
    }

    public void proceedToGameActual(String playerName, String playerGender) {
        getContentPane().removeAll();

        emily = new Emily();
        npcList = new ArrayList<>();
        npcList.add(new Abigail());
        npcList.add(new Caroline());
        npcList.add(new Dasco());
        npcList.add(new Perry());
        npcList.add(new Tadi());
        npcList.add(emily);

        farmMap = new FarmMap();
        npcMap = new NPCMap(npcList);
        storeMap = new StoreMap(emily);
        forestRiverMap = new ForestRiverMap();
        mountainLakeMap = new MountainLakeMap();
        oceanMap = new OceanMap();
        currentMap = farmMap;

        Point startingPlayerCoordinate = new Point(farmMap.getHouseCol() + FarmMap.HOUSE_WIDTH / 2, farmMap.getHouseRow() + FarmMap.HOUSE_HEIGHT);
        if (!(startingPlayerCoordinate.getY() < FarmMap.MAP_SIZE && startingPlayerCoordinate.getX() < FarmMap.MAP_SIZE &&
                farmMap.getTileType(startingPlayerCoordinate.getY(), startingPlayerCoordinate.getX()) == '.')) {
            startingPlayerCoordinate = new Point(1, 1);
            if (farmMap.getTileType(1,1) != '.') farmMap.setTileType(1,1,'.');
        }

        this.player = new Player(playerName, "Farm", playerGender, playerName + "'s Farm");
        this.player.moveTo(startingPlayerCoordinate);
        this.player.setGold(5000);

        gameCalendar = new GameCalendar(farmMap, this.player);
        gameCalendar.setMainFrame(this);

        statusBarPanel = new StatusBarPanel(this.player, gameCalendar);
        add(statusBarPanel, BorderLayout.NORTH);

        gameScreenPanel = new GameScreenPanel(this.player, gameCalendar, this);
        add(gameScreenPanel, BorderLayout.CENTER);

        actionPanel = new ActionPanel(gameScreenPanel);
        add(actionPanel, BorderLayout.SOUTH);

        setTitle("Spakbor Hills - " + this.player.getFarmName());
        pack();

        int topPanelHeight = statusBarPanel.getPreferredSize().height;
        int bottomPanelHeight = actionPanel.getPreferredSize().height > 0 ? actionPanel.getPreferredSize().height : 80;
        int gameScreenDim = FarmMap.MAP_SIZE * FarmMap.TILE_SIZE;

        if (currentMap instanceof NPCMap) gameScreenDim = Math.max(gameScreenDim, NPCMap.MAP_SIZE * NPCMap.TILE_SIZE);
        else if (currentMap instanceof StoreMap) gameScreenDim = Math.max(gameScreenDim, StoreMap.MAP_SIZE * StoreMap.TILE_SIZE);
        else if (currentMap instanceof ForestRiverMap) gameScreenDim = Math.max(gameScreenDim, ForestRiverMap.MAP_SIZE * ForestRiverMap.TILE_SIZE);
        else if (currentMap instanceof MountainLakeMap) gameScreenDim = Math.max(gameScreenDim, MountainLakeMap.MAP_SIZE * MountainLakeMap.TILE_SIZE);
        else if (currentMap instanceof OceanMap) gameScreenDim = Math.max(gameScreenDim, OceanMap.MAP_SIZE * OceanMap.TILE_SIZE);


        int frameInsetsHeight = getInsets().top + getInsets().bottom;
        int frameInsetsWidth = getInsets().left + getInsets().right;

        int minWidth = Math.max(700, gameScreenDim + frameInsetsWidth + 20);
        int minHeight = Math.max(550, gameScreenDim + topPanelHeight + bottomPanelHeight + frameInsetsHeight + 20);

        setMinimumSize(new Dimension(minWidth, minHeight));
        setSize(minWidth, minHeight);
        setLocationRelativeTo(null);

        revalidate();
        repaint();
        gameScreenPanel.requestFocusInWindow();

        if (calendarThread != null && calendarThread.isAlive()) {
            gameCalendar.stop();
            try {
                calendarThread.join(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        calendarThread = new Thread(gameCalendar);
        calendarThread.start();
    }


    public void checkMilestones() {
        if (player == null || gameCalendar == null) return;

        // Check Gold Milestone
        if (player.getGold() >= 17209 && !player.isGoldMilestoneAchieved()) {
            player.setGoldMilestoneAchieved(true);
            showEndGameStatsDialog("gold");
        }

        // Check Marriage Milestone
        if (player.getPartner() != null && "spouse".equals(player.getPartner().getRelationshipStatus()) && !player.isMarriageMilestoneAchieved()) {
            player.setMarriageMilestoneAchieved(true);
            showEndGameStatsDialog("marriage");
        }
    }

    private void showEndGameStatsDialog(String milestoneType) {
        if (gameCalendar != null) {
            gameCalendar.pauseTime();
        }
        // Ensure only one instance of this dialog is shown at a time, or manage focus properly
        if (endGameStatsDialog != null && endGameStatsDialog.isVisible()) {
            endGameStatsDialog.toFront(); // Bring to front if already visible
            return;
        }
        endGameStatsDialog = new EndGameStatsDialog(this, player, gameCalendar, npcList, milestoneType);
        endGameStatsDialog.setVisible(true);
        // The dialog's window listener will handle resuming game time
    }


    public ActionPanel getActionPanel() { return actionPanel; }
    public GameCalendar getGameCalendar() { return gameCalendar; }
    public Player getPlayer() { return player; }
    public GameScreenPanel getGameScreenPanel() { return gameScreenPanel; }
    public StatusBarPanel getStatusBarPanel() { return statusBarPanel;}

    public Object getCurrentMap() { return currentMap; }
    public FarmMap getFarmMap() { return farmMap; }
    public NPCMap getNpcMap() { return npcMap; }
    public StoreMap getStoreMap() { return storeMap; }
    public ForestRiverMap getForestRiverMap() { return forestRiverMap; }
    public MountainLakeMap getMountainLakeMap() { return mountainLakeMap; }
    public OceanMap getOceanMap() { return oceanMap; }
    public List<NPC> getNpcList() { return npcList; }
    public Emily getEmily() { return emily; }


    public void switchToNPCMap() {
        this.currentMap = npcMap;
        if(gameScreenPanel != null) gameScreenPanel.setCurrentMap(npcMap);
    }

    public void switchToFarmMap() {
        this.currentMap = farmMap;
        if(gameScreenPanel != null) gameScreenPanel.setCurrentMap(farmMap);
    }

    public void switchToStoreMap() {
        this.currentMap = storeMap;
        if(gameScreenPanel != null) gameScreenPanel.setCurrentMap(storeMap);
    }

    public void switchToForestRiverMap() {
        this.currentMap = forestRiverMap;
        if(gameScreenPanel != null) gameScreenPanel.setCurrentMap(forestRiverMap);
    }

    public void switchToMountainLakeMap() {
        this.currentMap = mountainLakeMap;
        if(gameScreenPanel != null) gameScreenPanel.setCurrentMap(mountainLakeMap);
    }

    public void switchToOceanMap() {
        this.currentMap = oceanMap;
        if(gameScreenPanel != null) gameScreenPanel.setCurrentMap(oceanMap);
    }


    public void showShippingBinPanel() {
        if (shippingBinPanel == null || !shippingBinPanel.isDisplayable()) {
            shippingBinPanel = new ShippingBinPanel(this, player, gameCalendar, gameScreenPanel);
        }
        shippingBinPanel.setVisible(true);
    }

    private void performShutdown() {
        if (gameCalendar != null) {
            gameCalendar.stop();
        }
        if (calendarThread != null && calendarThread.isAlive()) {
            try {
                calendarThread.join(500);
                if (calendarThread.isAlive()) {
                    calendarThread.interrupt();
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
