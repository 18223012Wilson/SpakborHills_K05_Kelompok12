package main;

import model.calendar.GameCalendar;
import model.entitas.Player;
import model.items.Point;
import model.map.FarmMap;
import ui.GameScreenPanel;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends JFrame {

    private GameCalendar gameCalendar;
    private Thread calendarThread;

    public Main() {
        setTitle("Spakbor Hills");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gameCalendar = new GameCalendar();
        FarmMap farmMap = new FarmMap();
        Player player = new Player("Dr. Asep", new Point(1, 1), "Male", "Spakbor Farm", farmMap);

        GameScreenPanel gameScreenPanel = new GameScreenPanel(player, farmMap, gameCalendar);
        add(gameScreenPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        gameScreenPanel.requestFocusInWindow();

        calendarThread = new Thread(gameCalendar);
        calendarThread.start();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gameCalendar.stop();
                try {
                    calendarThread.join(1500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                super.windowClosing(e);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}