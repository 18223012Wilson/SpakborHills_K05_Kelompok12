package ui;

import model.calendar.GameCalendar;
import model.entitas.Player;

import javax.swing.*;
import java.awt.*;

public class StatusBarPanel extends JPanel {
    private Player player;
    private GameCalendar gameCalendar;

    private JLabel nameLabel;
    private JLabel energyLabel;
    private JLabel goldLabel;
    private JLabel locationLabel;
    private JLabel timeLabel;
    private JLabel dateLabel;
    private JLabel seasonLabel;
    private JLabel weatherLabel;

    public StatusBarPanel(Player player, GameCalendar gameCalendar) {
        this.player = player;
        this.gameCalendar = gameCalendar;

        setBackground(new Color(50, 50, 50));
        setPreferredSize(new Dimension(0, 60));
        setLayout(new GridLayout(2, 4, 10, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Color textColor = Color.WHITE;

        nameLabel = new JLabel("Player: " + player.getName());
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(textColor);
        add(nameLabel);

        goldLabel = new JLabel("Gold: " + player.getGold() + "g");
        goldLabel.setFont(labelFont);
        goldLabel.setForeground(textColor);
        add(goldLabel);

        energyLabel = new JLabel("Energy: " + player.getEnergy() + "/" + player.getMaxEnergy());
        energyLabel.setFont(labelFont);
        energyLabel.setForeground(textColor);
        add(energyLabel);

        locationLabel = new JLabel("Location: " + player.getLocation());
        locationLabel.setFont(labelFont);
        locationLabel.setForeground(textColor);
        add(locationLabel);


        dateLabel = new JLabel("Day: " + gameCalendar.getDay());
        dateLabel.setFont(labelFont);
        dateLabel.setForeground(textColor);
        add(dateLabel);

        seasonLabel = new JLabel("Season: " + gameCalendar.getSeason().toString());
        seasonLabel.setFont(labelFont);
        seasonLabel.setForeground(textColor);
        add(seasonLabel);

        weatherLabel = new JLabel("Weather: " + gameCalendar.getWeather().toString());
        weatherLabel.setFont(labelFont);
        weatherLabel.setForeground(textColor);
        add(weatherLabel);

        timeLabel = new JLabel("Time: " + gameCalendar.getTime().toString());
        timeLabel.setFont(labelFont);
        timeLabel.setForeground(textColor);
        add(timeLabel);


        Timer updateTimer = new Timer(200, e -> updateStatus());
        updateTimer.start();
    }

    private void updateStatus() {
        if (player != null && gameCalendar != null) {
            energyLabel.setText("Energy: " + player.getEnergy() + "/" + player.getMaxEnergy());
            goldLabel.setText("Gold: " + player.getGold() + "g");
            locationLabel.setText("Location: "+ player.getLocation());
            timeLabel.setText("Time: " + String.format("%02d:%02d", gameCalendar.getTime().getHour(), gameCalendar.getTime().getMinute()));
            dateLabel.setText("Day: " + gameCalendar.getDay());
            seasonLabel.setText("Season: " + gameCalendar.getSeason().toString());
            weatherLabel.setText("Weather: " + gameCalendar.getWeather().toString());
        }
        revalidate();
        repaint();
    }

    public void setPlayer(Player player) {
        this.player = player;
        updateStatus();
    }

    public void setGameCalendar(GameCalendar gameCalendar) {
        this.gameCalendar = gameCalendar;
        updateStatus();
    }
}