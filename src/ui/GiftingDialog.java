package ui;

import model.entitas.NPC;
import model.entitas.Player;
import model.calendar.GameCalendar;
import model.items.Item;
import model.items.Inventory;
import model.items.Seed;
import model.items.Fish;
import model.actions.ProcessGiftingAction;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.net.URL;

public class GiftingDialog extends JDialog {
    private Player player;
    private GameCalendar calendar;
    private NPC npcTarget;
    private GameScreenPanel gameScreenPanel;
    private Inventory inventory;

    private JPanel itemsPanel;
    private JLabel currentHeartPointsLabel;
    private JLabel potentialHeartChangeLabel;
    private JLabel resultingHeartPointsLabel;
    private JLabel npcMessageLabel;

    private final int SLOT_WIDTH = 110;
    private final int SLOT_HEIGHT = 130;
    private final int ITEMS_PER_ROW = 4;

    private static final Color BACKGROUND_COLOR = new Color(240, 220, 190);
    private static final Color PANEL_BACKGROUND_COLOR = new Color(255, 239, 213);
    private static final Color SLOT_BACKGROUND_COLOR = new Color(222, 184, 135);
    private static final Color SLOT_BORDER_COLOR = new Color(139, 69, 19);
    private static final Color SLOT_HOVER_BACKGROUND_COLOR = new Color(245, 222, 179);
    private static final Color BUTTON_COLOR = new Color(101, 67, 33);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(85, 52, 20);
    private static final Color HEART_COLOR_POSITIVE = new Color(220, 20, 60);
    private static final Color HEART_COLOR_NEGATIVE = new Color(70, 130, 180);
    private static final Color HEART_COLOR_NEUTRAL = new Color(105, 105, 105);

    public GiftingDialog(Frame owner, Player player, GameCalendar calendar, NPC npcTarget, GameScreenPanel gameScreenPanel) {
        super(owner, "💌 Beri Gift untuk " + npcTarget.getName(), true);
        this.player = player;
        this.calendar = calendar;
        this.npcTarget = npcTarget;
        this.gameScreenPanel = gameScreenPanel;
        this.inventory = player.getInventory();

        initComponents();
        refreshGiftableItemsDisplay();
        pack();
        setMinimumSize(new Dimension(ITEMS_PER_ROW * (SLOT_WIDTH + 15) + 60, 480));
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });
    }

    private void initComponents() {
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout(10,5));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Pilih item dari inventory untuk di-gift kepada " + npcTarget.getName() + ":", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(0,0,10,0));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel npcInfoPanel = new JPanel(new GridLayout(1,2,10,5));
        npcInfoPanel.setOpaque(false);
        npcInfoPanel.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder(new LineBorder(SLOT_BORDER_COLOR, 1), "Info " + npcTarget.getName(), TitledBorder.CENTER, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 12), TEXT_COLOR),
                new EmptyBorder(5,10,10,10)
        ));

        currentHeartPointsLabel = new JLabel("❤️ Heart Points: " + npcTarget.getHeartPoints() + "/" + npcTarget.getMaxHeartPoints());
        currentHeartPointsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        currentHeartPointsLabel.setForeground(TEXT_COLOR);
        npcInfoPanel.add(currentHeartPointsLabel);

        JPanel potentialPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5,0));
        potentialPanel.setOpaque(false);
        potentialHeartChangeLabel = new JLabel("Efek Gift: -");
        potentialHeartChangeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        potentialHeartChangeLabel.setForeground(HEART_COLOR_NEUTRAL);
        potentialPanel.add(potentialHeartChangeLabel);

        resultingHeartPointsLabel = new JLabel("");
        resultingHeartPointsLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        resultingHeartPointsLabel.setForeground(TEXT_COLOR);
        potentialPanel.add(resultingHeartPointsLabel);

        npcInfoPanel.add(potentialPanel);
        headerPanel.add(npcInfoPanel, BorderLayout.CENTER);

        npcMessageLabel = new JLabel("Pilih item yang mau di-gift...", SwingConstants.CENTER);
        npcMessageLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        npcMessageLabel.setForeground(TEXT_COLOR);
        npcMessageLabel.setBorder(new EmptyBorder(5,0,5,0));
        headerPanel.add(npcMessageLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);


        itemsPanel = new JPanel();
        itemsPanel.setBackground(PANEL_BACKGROUND_COLOR);
        itemsPanel.setBorder(new CompoundBorder(
                new LineBorder(SLOT_BORDER_COLOR, 2),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.getViewport().setBackground(PANEL_BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10,0,0,0));

        JButton cancelButton = new JButton("Batal");
        styleButton(cancelButton, new Color(160, 80, 80));
        cancelButton.addActionListener(e -> closeDialog());
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(cancelButton);
        bottomPanel.add(buttonWrapper, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshGiftableItemsDisplay() {
        itemsPanel.removeAll();
        currentHeartPointsLabel.setText("❤️ Heart Points: " + npcTarget.getHeartPoints() + "/" + npcTarget.getMaxHeartPoints());
        potentialHeartChangeLabel.setText("Efek Gift: -");
        potentialHeartChangeLabel.setForeground(HEART_COLOR_NEUTRAL);
        resultingHeartPointsLabel.setText("");
        npcMessageLabel.setText("Pilih item yang mau di-gift...");


        Map<Item, Integer> currentItems = inventory.getAllItemsWithQuantities();
        List<Map.Entry<Item, Integer>> giftableItems = new ArrayList<>();

        for (Map.Entry<Item, Integer> entry : currentItems.entrySet()) {
            if (entry.getKey().isGiftable() && entry.getValue() > 0) {
                giftableItems.add(entry);
            }
        }
        giftableItems.sort(Comparator.comparing(entry -> entry.getKey().getName()));

        if (giftableItems.isEmpty()) {
            itemsPanel.setLayout(new BorderLayout());
            JLabel emptyLabel = new JLabel("<html><div style='text-align: center;'>Yah, di inventory mu belum ada yang bisa di-gift nih.<br/>Cari atau beli dulu deh</div></html>", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            emptyLabel.setForeground(TEXT_COLOR);
            itemsPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            int numRows = Math.max(1, (int) Math.ceil((double) giftableItems.size() / ITEMS_PER_ROW));
            itemsPanel.setLayout(new GridLayout(numRows, ITEMS_PER_ROW, 8, 8));
            for (Map.Entry<Item, Integer> entry : giftableItems) {
                itemsPanel.add(createItemSlot(entry.getKey(), entry.getValue()));
            }
            int totalSlots = numRows * ITEMS_PER_ROW;
            for (int i = giftableItems.size(); i < totalSlots; i++) {
                JPanel emptySlot = new JPanel();
                emptySlot.setPreferredSize(new Dimension(SLOT_WIDTH, SLOT_HEIGHT));
                emptySlot.setBackground(PANEL_BACKGROUND_COLOR);
                itemsPanel.add(emptySlot);
            }
        }
        itemsPanel.revalidate();
        itemsPanel.repaint();
        if (isVisible()) {
            SwingUtilities.invokeLater(this::pack);
        }
    }

    private int calculatePotentialHeartPoints(Item item) {
        if (item == null || npcTarget == null || !item.isGiftable()) return 0;
        int change = 0;

        if (npcTarget.getName().equalsIgnoreCase("Emily")) {
            if (item instanceof Seed) change = 25;
            else if (checkItemInList(npcTarget.getLikedItems(), item.getName())) change = 20;
            else if (checkItemInList(npcTarget.getHatedItems(), item.getName())) change = -25;
        }
        else if (npcTarget.getName().equalsIgnoreCase("Perry")) {
            if (item instanceof Fish) change = -25;
            else if (checkItemInList(npcTarget.getLovedItems(), item.getName())) change = 25;
            else if (checkItemInList(npcTarget.getLikedItems(), item.getName())) change = 20;
        }
        else if (npcTarget.getName().equalsIgnoreCase("Mayor Tadi")) {
            if (checkItemInList(npcTarget.getLovedItems(), item.getName())) change = 25;
            else if (checkItemInList(npcTarget.getLikedItems(), item.getName())) change = 20;
            else change = -25;
        }
        else {
            if (checkItemInList(npcTarget.getLovedItems(), item.getName())) change = 25;
            else if (checkItemInList(npcTarget.getLikedItems(), item.getName())) change = 20;
            else if (checkItemInList(npcTarget.getHatedItems(), item.getName())) change = -25;
        }
        return change;
    }

    private boolean checkItemInList(String[] list, String itemName) {
        if (list == null) return false;
        for (String s : list) {
            if (s != null && s.equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }


    private JPanel createItemSlot(Item item, int quantity) {
        JPanel slotPanel = new JPanel(new BorderLayout(5, 5));
        slotPanel.setPreferredSize(new Dimension(SLOT_WIDTH, SLOT_HEIGHT));
        slotPanel.setBackground(SLOT_BACKGROUND_COLOR);
        slotPanel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(SLOT_BORDER_COLOR, 1, true),
                new EmptyBorder(5,5,5,5))
        );
        String tooltipText = String.format("<html><b>%s</b> (x%d)<br/>Kategori: %s<br/><i>Klik 'Kasih Gift' untuk memberikan item ini.</i></html>",
                item.getName(), quantity, item.getCategory());
        slotPanel.setToolTipText(tooltipText);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(SLOT_WIDTH - 20, SLOT_HEIGHT - 65));
        try {
            URL imgUrl = getClass().getResource(item.getImagePath());
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image image = icon.getImage().getScaledInstance(SLOT_WIDTH - 40, SLOT_HEIGHT - 75, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
            }
            else {
                imageLabel.setText("No Img");
                imageLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
            }
        }
        catch (Exception e) {
            imageLabel.setText("Error");
            imageLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
        }
        slotPanel.add(imageLabel, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel("<html><div style='text-align:center; width:"+(SLOT_WIDTH - 10)+"px;'>"+item.getName()+"</div></html>");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        nameLabel.setForeground(TEXT_COLOR);
        slotPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel bottomInfoPanel = new JPanel(new BorderLayout());
        bottomInfoPanel.setOpaque(false);

        JLabel quantityLabel = new JLabel("x" + quantity);
        quantityLabel.setHorizontalAlignment(SwingConstants.LEFT);
        quantityLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        quantityLabel.setForeground(TEXT_COLOR.darker());
        bottomInfoPanel.add(quantityLabel, BorderLayout.WEST);

        JButton giveButton = new JButton("Kasih Gift");
        styleButton(giveButton, BUTTON_COLOR);
        giveButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        giveButton.setMargin(new Insets(3,6,3,6));
        giveButton.addActionListener(e -> confirmAndGiveItem(item));
        bottomInfoPanel.add(giveButton, BorderLayout.EAST);

        slotPanel.add(bottomInfoPanel, BorderLayout.SOUTH);

        slotPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                slotPanel.setBackground(SLOT_HOVER_BACKGROUND_COLOR);
                slotPanel.setBorder(new CompoundBorder(
                        BorderFactory.createLineBorder(BUTTON_COLOR.brighter(), 2, true),
                        new EmptyBorder(5,5,5,5))
                );
                int points = calculatePotentialHeartPoints(item);
                String pointText = (points > 0 ? "+" : "") + points;
                potentialHeartChangeLabel.setText("Efek Gift: " + pointText + " heart points");
                if (points > 0) potentialHeartChangeLabel.setForeground(HEART_COLOR_POSITIVE);
                else if (points < 0) potentialHeartChangeLabel.setForeground(HEART_COLOR_NEGATIVE);
                else potentialHeartChangeLabel.setForeground(HEART_COLOR_NEUTRAL);

                int currentHearts = npcTarget.getHeartPoints();
                int resultingHearts = Math.min(npcTarget.getMaxHeartPoints(), Math.max(0, currentHearts + points));
                resultingHeartPointsLabel.setText("➡️ Total: " + resultingHearts + " ❤️");

                if (points == 25) npcMessageLabel.setText(npcTarget.getName() + " bakal seneng banget nih kayaknya!");
                else if (points == 20) npcMessageLabel.setText(npcTarget.getName() + " mungkin bakal suka ini.");
                else if (points == 0) npcMessageLabel.setText(npcTarget.getName() + " mungkin biasa aja nerimanya.");
                else if (points < 0) npcMessageLabel.setText("Hmm, " + npcTarget.getName() + " mungkin kurang suka item ini...");


            }
            @Override
            public void mouseExited(MouseEvent e) {
                slotPanel.setBackground(SLOT_BACKGROUND_COLOR);
                slotPanel.setBorder(new CompoundBorder(
                        BorderFactory.createLineBorder(SLOT_BORDER_COLOR, 1, true),
                        new EmptyBorder(5,5,5,5))
                );
                potentialHeartChangeLabel.setText("Efek Gift: -");
                potentialHeartChangeLabel.setForeground(HEART_COLOR_NEUTRAL);
                resultingHeartPointsLabel.setText("");
                npcMessageLabel.setText("Pilih item yang mau di-gift...");
            }
        });
        return slotPanel;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker().darker()),
                new EmptyBorder(5, 10, 5, 10)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hoverBgColor = bgColor.brighter();
        Color pressedBgColor = bgColor.darker();

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(hoverBgColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(bgColor);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(pressedBgColor);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(hoverBgColor);
            }
        });
    }


    private void confirmAndGiveItem(Item item) {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10,10,10,10));

        JLabel confirmMessage = new JLabel(
                String.format("<html>Yakin mau kasih <b>%s</b> ke <b>%s</b>?</html>", item.getName(), npcTarget.getName()),
                SwingConstants.CENTER);
        confirmMessage.setFont(new Font("SansSerif", Font.PLAIN, 14));
        confirmMessage.setForeground(TEXT_COLOR);
        panel.add(confirmMessage, BorderLayout.CENTER);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            URL imgUrl = getClass().getResource(item.getImagePath());
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image image = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
                panel.add(imageLabel, BorderLayout.NORTH);
            }
        } catch (Exception e) { /* ignore */ }


        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("Button.background", BUTTON_COLOR);
        UIManager.put("Button.foreground", BUTTON_TEXT_COLOR);


        int confirm = JOptionPane.showConfirmDialog(this,
                panel,
                "Konfirmasi Gift", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);


        if (confirm == JOptionPane.YES_OPTION) {
            ProcessGiftingAction giftAction = new ProcessGiftingAction(player, calendar, npcTarget, item);
            String resultMessage = giftAction.execute();

            if (gameScreenPanel != null) {
                gameScreenPanel.showMessage(resultMessage);
            }
            else {
                System.out.println(resultMessage);
            }
            closeDialog();
        }
    }

    private void closeDialog() {
        if (calendar != null) {
            calendar.startTime();
        }
        if (gameScreenPanel != null) {
            gameScreenPanel.requestFocusInWindow();
        }
        setVisible(false);
        dispose();
    }
}