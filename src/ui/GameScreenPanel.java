package ui;

import model.calendar.GameCalendar;
import model.entitas.Player;
import model.items.Point;
import model.map.FarmMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameScreenPanel extends JPanel implements KeyListener {
    private Player player;
    private FarmMap farmMap;
    private GameCalendar gameCalendar;
    private Image playerImage;
    private String popupMessage;
    private long messageTime;


    public GameScreenPanel(Player player, FarmMap farmMap, GameCalendar gameCalendar) {
        this.player = player;
        this.farmMap = farmMap;
        this.gameCalendar = gameCalendar;

        setPreferredSize(new Dimension(FarmMap.MAP_SIZE * FarmMap.TILE_SIZE, FarmMap.MAP_SIZE * FarmMap.TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        try {
            playerImage = new ImageIcon("src/player.png").getImage();
        } catch (Exception e) {
            System.err.println("Player image not found. Ensure 'src/player.png' exists.");
            playerImage = null;
        }

        Timer repaintTimer = new Timer(100, e -> repaint());
        repaintTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        farmMap.draw(g, this);

        if (playerImage != null) {
            g.drawImage(playerImage, player.getLocation().getX() * FarmMap.TILE_SIZE, player.getLocation().getY() * FarmMap.TILE_SIZE, FarmMap.TILE_SIZE, FarmMap.TILE_SIZE, this);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(player.getLocation().getX() * FarmMap.TILE_SIZE, player.getLocation().getY() * FarmMap.TILE_SIZE, FarmMap.TILE_SIZE, FarmMap.TILE_SIZE);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String calendarInfo = gameCalendar.getFormattedTimeInfo();
        FontMetrics fm = g.getFontMetrics();
        int stringWidth = fm.stringWidth(calendarInfo);
        g.drawString(calendarInfo, getWidth() - stringWidth - 10, 20);

        if (popupMessage != null && System.currentTimeMillis() - messageTime < 2000) {
            int px = player.getLocation().getX() * FarmMap.TILE_SIZE;
            int py = player.getLocation().getY() * FarmMap.TILE_SIZE - 10;
            int msgWidth = fm.stringWidth(popupMessage) + 20;
            int msgHeight = fm.getHeight() + 10;
            g.setColor(new Color(0, 0, 0, 170));
            g.fillRoundRect(px - msgWidth / 2 + FarmMap.TILE_SIZE / 2, py - msgHeight, msgWidth, msgHeight, 15, 15);
            g.setColor(Color.WHITE);
            g.drawString(popupMessage, px - msgWidth / 2 + FarmMap.TILE_SIZE / 2 + 10, py - msgHeight + fm.getAscent() + 5);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        Point currentLocation = player.getLocation();
        int newX = currentLocation.getX();
        int newY = currentLocation.getY();

        switch (key) {
            case KeyEvent.VK_W: newY--; break;
            case KeyEvent.VK_S: newY++; break;
            case KeyEvent.VK_A: newX--; break;
            case KeyEvent.VK_D: newX++; break;
            case KeyEvent.VK_E:
                if (isAdjacentToHouse()) {
                    showMessage("Kamu berhasil masuk ke rumah!");
                } else {
                    showMessage("Kamu jauh dari rumah");
                }
                return;
            default: return;
        }

        if (isValidMove(newX, newY)) {
            player.moveTo(new Point(newX, newY));
        }
        repaint();
    }

    private boolean isAdjacentToHouse() {
        Point pLoc = player.getLocation();
        int houseDoorCol = farmMap.getHouseCol() + 2;
        int houseDoorRow = farmMap.getHouseRow() + 5;

        if (pLoc.getX() == houseDoorCol && pLoc.getY() == houseDoorRow + 1) return true;
        if (pLoc.getX() == houseDoorCol && pLoc.getY() == houseDoorRow -1 ) return true;
        if (pLoc.getY() == houseDoorRow && pLoc.getX() == houseDoorCol + 1) return true;
        if (pLoc.getY() == houseDoorRow && pLoc.getX() == houseDoorCol - 1) return true;

        return false;
    }

    private void showMessage(String msg) {
        popupMessage = msg;
        messageTime = System.currentTimeMillis();
        repaint();
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || x >= FarmMap.MAP_SIZE || y < 0 || y >= FarmMap.MAP_SIZE) {
            return false;
        }
        char tileType = farmMap.getTileType(y, x);
        return tileType == '.';
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}