package ui;

import main.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuPanel extends JPanel {
    private Main mainFrame;

    public MainMenuPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setBackground(new Color(40, 20, 0));
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0);

        JLabel titleLabel = new JLabel("Spakbor Hills", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 60));
        titleLabel.setForeground(new Color(230, 210, 190));
        gbc.insets = new Insets(10, 0, 40, 0);
        add(titleLabel, gbc);

        gbc.insets = new Insets(15, 0, 15, 0);

        JButton newGameButton = createMenuButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());
        add(newGameButton, gbc);

        JButton helpButton = createMenuButton("Help");
        helpButton.addActionListener(e -> showHelp());
        add(helpButton, gbc);

        JButton creditsButton = createMenuButton("Credits");
        creditsButton.addActionListener(e -> showCredits());
        add(creditsButton, gbc);

        JButton exitButton = createMenuButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        gbc.insets = new Insets(30, 0, 10, 0);
        add(exitButton, gbc);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 22));
        button.setBackground(new Color(120, 80, 60));
        button.setForeground(new Color(245, 245, 220));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 50, 40), 3),
                BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        Color baseBgColor = new Color(120, 80, 60);
        Color hoverBgColor = new Color(140, 100, 80);
        Color pressedBgColor = new Color(100, 60, 40);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(hoverBgColor);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(baseBgColor);
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                button.setBackground(pressedBgColor);
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                if (button.contains(evt.getPoint())) {
                    button.setBackground(hoverBgColor);
                } else {
                    button.setBackground(baseBgColor);
                }
            }
        });
        return button;
    }

    private void startNewGame() {
        JTextField nameField = new JTextField(20);
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Enter your character's name:"));
        panel.add(nameField);

        int result = JOptionPane.showConfirmDialog(mainFrame, panel, "New Game - Name",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String playerName = nameField.getText();
        if (playerName == null || playerName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Player name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] genders = {"Male", "Female"};
        String playerGender = (String) JOptionPane.showInputDialog(
                mainFrame,
                "Choose your character's gender:",
                "New Game - Gender",
                JOptionPane.QUESTION_MESSAGE,
                null,
                genders,
                genders[0]
        );

        if (playerGender == null) {
            return;
        }

        mainFrame.startGame(playerName, playerGender);
    }

    private void showHelp() {
        HelpDialog helpDialog = new HelpDialog(mainFrame);
        helpDialog.setVisible(true);
    }

    private void showCredits() {
        mainFrame.showCreditsScreen();
    }
}