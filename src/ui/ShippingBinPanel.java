package ui;

import model.entitas.Player;
import model.items.Item;
import model.items.Inventory;
import model.items.ShippingBin;
import model.calendar.GameCalendar;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ShippingBinPanel extends JDialog {
    private Player player;
    private Inventory playerInventory;
    private ShippingBin shippingBinModel;
    private GameCalendar gameCalendar;
    private GameScreenPanel gameScreenPanel;

    private JPanel playerInventoryPanel;
    private JPanel shippingBinSlotsPanel;
    private JLabel totalGoldLabel;
    private JLabel capacityLabel;
    private JProgressBar capacityBar;

    private Map<Item, Integer> itemsStagedInUI;

    private static final int MAX_DISPLAY_SLOTS = 16;

    private final int SLOT_WIDTH = 90;
    private final int SLOT_HEIGHT = 100;
    private final int IMAGE_SIZE = 48;

    private static final Color BACKGROUND_COLOR = new Color(245, 235, 215);
    private static final Color PANEL_COLOR = new Color(255, 248, 235);
    private static final Color ACCENT_COLOR = new Color(139, 115, 85);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color WARNING_COLOR = new Color(255, 152, 0);
    private static final Color ERROR_COLOR = new Color(244, 67, 54);
    private static final Color GOLD_COLOR = new Color(255, 193, 7);
    private static final Color ITEM_HOVER_COLOR = new Color(255, 255, 224);

    public ShippingBinPanel(Frame owner, Player player, GameCalendar gameCalendar, GameScreenPanel gameScreenPanel) {
        super(owner, "📦 Shipping Bin", true);
        this.player = player;
        this.playerInventory = player.getInventory();
        this.shippingBinModel = player.getShippingBin();
        this.gameCalendar = gameCalendar;
        this.gameScreenPanel = gameScreenPanel;

        this.itemsStagedInUI = new HashMap<>();
        Map<Item, Integer> currentlyStagedInModel = shippingBinModel.getStagedItems();
        for(Map.Entry<Item,Integer> entry : currentlyStagedInModel.entrySet()){
            this.itemsStagedInUI.put(entry.getKey(), entry.getValue());
        }

        initializeUITheme();
        initComponents();
        refreshUI();
        pack();
        setMinimumSize(new Dimension(Math.max(600, (SLOT_WIDTH + 10) * 3 + 80), 500));
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    private void initializeUITheme() {
        setBackground(BACKGROUND_COLOR);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));
        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);

        JPanel leftPanel = createInventoryPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = createShippingBinPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("📦 Shipping Bin", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel capacityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        capacityPanel.setBackground(BACKGROUND_COLOR);

        capacityLabel = new JLabel("Capacity: 0/" + ShippingBin.MAX_UNIQUE_ITEMS + " items");
        capacityLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        capacityLabel.setForeground(ACCENT_COLOR);

        capacityBar = new JProgressBar(0, ShippingBin.MAX_UNIQUE_ITEMS);
        capacityBar.setStringPainted(true);
        capacityBar.setPreferredSize(new Dimension(200, 20));
        capacityBar.setBackground(Color.WHITE);
        capacityBar.setForeground(SUCCESS_COLOR);

        capacityPanel.add(capacityLabel);
        capacityPanel.add(capacityBar);
        headerPanel.add(capacityPanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createInventoryPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBackground(BACKGROUND_COLOR);

        playerInventoryPanel = new JPanel(new GridLayout(0, 3, 8, 8));
        playerInventoryPanel.setBackground(PANEL_COLOR);

        JScrollPane inventoryScrollPane = new JScrollPane(playerInventoryPanel);
        inventoryScrollPane.setPreferredSize(new Dimension(3 * (SLOT_WIDTH + 8) + 20, 350));
        inventoryScrollPane.getViewport().setBackground(PANEL_COLOR);
        inventoryScrollPane.setBorder(createStyledBorder("🎒 Inventory", SUCCESS_COLOR));

        leftPanel.add(inventoryScrollPane, BorderLayout.CENTER);

        JLabel inventoryTip = new JLabel("<html><div style='text-align: center;'>" +
                "💡 Click items to add to shipping bin<br/>" +
                "Shipping Bin hanya menampilkan sellable items</div></html>");
        inventoryTip.setFont(new Font("SansSerif", Font.ITALIC, 11));
        inventoryTip.setHorizontalAlignment(JLabel.CENTER);
        inventoryTip.setForeground(ACCENT_COLOR);
        inventoryTip.setBorder(new EmptyBorder(5, 5, 5, 5));
        leftPanel.add(inventoryTip, BorderLayout.SOUTH);

        return leftPanel;
    }

    private JPanel createShippingBinPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBackground(BACKGROUND_COLOR);

        shippingBinSlotsPanel = new JPanel(new GridLayout(0, 3, 8, 8));
        shippingBinSlotsPanel.setBackground(PANEL_COLOR);

        JScrollPane shippingBinScrollPane = new JScrollPane(shippingBinSlotsPanel);
        shippingBinScrollPane.setPreferredSize(new Dimension(3 * (SLOT_WIDTH + 8) + 20, 350));
        shippingBinScrollPane.getViewport().setBackground(PANEL_COLOR);
        shippingBinScrollPane.setBorder(createStyledBorder("📦 Shipping Bin", WARNING_COLOR));

        rightPanel.add(shippingBinScrollPane, BorderLayout.CENTER);

        JLabel binTip = new JLabel("<html><div style='text-align: center;'>" +
                "💰 Click items to remove from bin<br/>" +
                "Gold will be collected tomorrow</div></html>");
        binTip.setFont(new Font("SansSerif", Font.ITALIC, 11));
        binTip.setHorizontalAlignment(JLabel.CENTER);
        binTip.setForeground(ACCENT_COLOR);
        binTip.setBorder(new EmptyBorder(5, 5, 5, 5));
        rightPanel.add(binTip, BorderLayout.SOUTH);

        return rightPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JPanel goldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        goldPanel.setBackground(BACKGROUND_COLOR);

        totalGoldLabel = new JLabel("💰 Potential Gold: 0g");
        totalGoldLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalGoldLabel.setForeground(GOLD_COLOR.darker());
        totalGoldLabel.setBorder(new CompoundBorder(
                new LineBorder(GOLD_COLOR, 2, true),
                new EmptyBorder(8, 15, 8, 15)
        ));
        totalGoldLabel.setOpaque(true);
        totalGoldLabel.setBackground(new Color(255, 248, 225));

        goldPanel.add(totalGoldLabel);
        bottomPanel.add(goldPanel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setBackground(BACKGROUND_COLOR);

        JButton sellButton = createStyledButton("💰 Sell Items", SUCCESS_COLOR, Color.WHITE);
        sellButton.addActionListener(e -> handleSell());
        sellButton.setPreferredSize(new Dimension(140, 40));

        JButton cancelButton = createStyledButton("❌ Cancel", ERROR_COLOR, Color.WHITE);
        cancelButton.addActionListener(e -> handleCancel());
        cancelButton.setPreferredSize(new Dimension(140, 40));

        buttonsPanel.add(sellButton);
        buttonsPanel.add(cancelButton);
        bottomPanel.add(buttonsPanel, BorderLayout.CENTER);

        return bottomPanel;
    }

    private CompoundBorder createStyledBorder(String title, Color accentColor) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                new LineBorder(accentColor, 2, true), title);
        titledBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        titledBorder.setTitleColor(accentColor);

        return new CompoundBorder(titledBorder, new EmptyBorder(10, 10, 10, 10));
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setBorder(new CompoundBorder(
                new LineBorder(bgColor.darker(), 1, true),
                new EmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private JPanel createItemSlot(Item item, int quantity, boolean isFromInventory) {
        JPanel slotPanel = new JPanel(new BorderLayout(3,3));
        slotPanel.setPreferredSize(new Dimension(SLOT_WIDTH, SLOT_HEIGHT));
        slotPanel.setBackground(isFromInventory ? Color.WHITE : PANEL_COLOR);
        slotPanel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR.brighter(), 1),
                new EmptyBorder(5,5,5,5))
        );

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(SLOT_WIDTH - 10, IMAGE_SIZE + 5));

        try {
            String imagePath = item.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                URL imgUrl = getClass().getResource(imagePath);
                if (imgUrl != null) {
                    ImageIcon icon = new ImageIcon(imgUrl);
                    Image image = icon.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(image));
                }
                else {
                    imageLabel.setText("N/A");
                    imageLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
                }
            }
            else {
                imageLabel.setText("No Path");
                imageLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
            }
        } catch (Exception ex) {
            imageLabel.setText("Err");
            imageLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));

        }
        slotPanel.add(imageLabel, BorderLayout.CENTER);

        // Label untuk nama item
        JLabel nameLabel = new JLabel("<html><div style='text-align:center; width:"+(SLOT_WIDTH-15)+"px'>"+item.getName()+"</div></html>");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        nameLabel.setForeground(ACCENT_COLOR.darker());
        slotPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3,0));
        infoPanel.setOpaque(false);

        String infoText;
        String tooltipText;
        Runnable action;

        if (isFromInventory) {
            infoText = String.format("(%d) 💰%dg", quantity, item.getSellPrice());
            tooltipText = String.format("<html><b>%s</b><br/>Available: %d<br/>Sell Price: %d g each<br/><i>Click to add to shipping bin</i></html>", item.getName(), quantity, item.getSellPrice());
            action = () -> handleAddItemToBin(item, quantity);
        } else {
            int totalValue = item.getSellPrice() * quantity;
            infoText = String.format("x%d 💰%dg", quantity, totalValue);
            tooltipText = String.format("<html><b>%s</b><br/>Quantity: %d<br/>Total Value: %d g<br/><i>Click to return to inventory</i></html>", item.getName(), quantity, totalValue);
            action = () -> handleRemoveItemFromBin(item, quantity);
        }
        JLabel detailsLabel = new JLabel(infoText);
        detailsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        detailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 9));
        detailsLabel.setForeground(ACCENT_COLOR);
        infoPanel.add(detailsLabel);
        slotPanel.add(infoPanel, BorderLayout.SOUTH);

        slotPanel.setToolTipText(tooltipText);
        slotPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        slotPanel.addMouseListener(new MouseAdapter() {
            Color originalBg = slotPanel.getBackground();
            Border originalBorder = slotPanel.getBorder();
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                slotPanel.setBackground(ITEM_HOVER_COLOR);
                slotPanel.setBorder(new CompoundBorder(
                        BorderFactory.createLineBorder(SUCCESS_COLOR, 2),
                        new EmptyBorder(5,5,5,5))
                );
            }
            @Override
            public void mouseExited(MouseEvent e) {
                slotPanel.setBackground(originalBg);
                slotPanel.setBorder(originalBorder);
            }
        });

        return slotPanel;
    }


    private void refreshUI() {
        playerInventoryPanel.removeAll();
        Map<Item, Integer> currentInventory = playerInventory.getAllItemsWithQuantities();
        List<Item> sortedInventoryItems = new ArrayList<>(currentInventory.keySet());
        sortedInventoryItems.sort(Comparator.comparing(Item::getName));

        for (Item item : sortedInventoryItems) {
            if (item.getSellPrice() > 0) {
                int availableQuantity = currentInventory.get(item);
                if (availableQuantity > 0) {
                    playerInventoryPanel.add(createItemSlot(item, availableQuantity, true));
                }
            }
        }
        playerInventoryPanel.revalidate();
        playerInventoryPanel.repaint();

        shippingBinSlotsPanel.removeAll();
        List<Item> sortedStagedItems = new ArrayList<>(itemsStagedInUI.keySet());
        sortedStagedItems.sort(Comparator.comparing(Item::getName));

        for (Item item : sortedStagedItems) {
            int quantity = itemsStagedInUI.get(item);
            shippingBinSlotsPanel.add(createItemSlot(item, quantity, false));
        }

        for (int i = itemsStagedInUI.size(); i < MAX_DISPLAY_SLOTS; i++) {
            JPanel emptySlot = new JPanel();
            emptySlot.setPreferredSize(new Dimension(SLOT_WIDTH, SLOT_HEIGHT));
            emptySlot.setBorder(BorderFactory.createDashedBorder(Color.LIGHT_GRAY));
            emptySlot.setBackground(PANEL_COLOR.darker());
            shippingBinSlotsPanel.add(emptySlot);
        }

        shippingBinSlotsPanel.revalidate();
        shippingBinSlotsPanel.repaint();

        int potentialGold = 0;
        for (Map.Entry<Item, Integer> entry : itemsStagedInUI.entrySet()) {
            potentialGold += entry.getKey().getSellPrice() * entry.getValue();
        }
        totalGoldLabel.setText("💰 Potential Gold: " + potentialGold + "g");

        long currentUniqueInBin = shippingBinModel.getItemsConfirmedForSale().keySet().size();
        long uniqueStaged = itemsStagedInUI.keySet().stream().distinct().count();
        long totalUnique = currentUniqueInBin + uniqueStaged;


        capacityBar.setValue((int)totalUnique);
        capacityLabel.setText("Capacity: " + totalUnique + "/" + ShippingBin.MAX_UNIQUE_ITEMS + " items");

        if (totalUnique >= ShippingBin.MAX_UNIQUE_ITEMS * 0.8) {
            capacityBar.setForeground(ERROR_COLOR);
        } else if (totalUnique >= ShippingBin.MAX_UNIQUE_ITEMS * 0.6) {
            capacityBar.setForeground(WARNING_COLOR);
        } else {
            capacityBar.setForeground(SUCCESS_COLOR);
        }
    }


    private void handleAddItemToBin(Item item, int maxQuantity) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(5, 5, 5, 5);
        JLabel itemLabel = new JLabel("📦 " + item.getName());
        itemLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        itemLabel.setForeground(ACCENT_COLOR);
        panel.add(itemLabel, gbc);

        gbc.gridy++;
        JLabel priceLabel = new JLabel("💰 " + item.getSellPrice() + "g each");
        priceLabel.setForeground(GOLD_COLOR.darker());
        panel.add(priceLabel, gbc);

        gbc.gridy++;
        JLabel maxLabel = new JLabel("Available: " + maxQuantity);
        maxLabel.setForeground(ACCENT_COLOR);
        panel.add(maxLabel, gbc);

        gbc.gridy++;
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, maxQuantity, 1));
        quantitySpinner.setPreferredSize(new Dimension(100, 25));
        panel.add(quantitySpinner, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add to Shipping Bin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int quantity = (Integer) quantitySpinner.getValue();

            long currentUniqueConfirmed = shippingBinModel.getItemsConfirmedForSale().keySet().stream().distinct().count();
            long uniqueStagedCount = itemsStagedInUI.keySet().stream()
                    .filter(stagedItem -> !shippingBinModel.getItemsConfirmedForSale().containsKey(stagedItem))
                    .distinct()
                    .count();

            boolean isNewUniqueItemToStaging = !itemsStagedInUI.containsKey(item) && !shippingBinModel.getItemsConfirmedForSale().containsKey(item);
            long projectedUniqueItems = currentUniqueConfirmed + uniqueStagedCount + (isNewUniqueItemToStaging ? 1 : 0);


            if (projectedUniqueItems > ShippingBin.MAX_UNIQUE_ITEMS) {
                showStyledMessage("❌ Shipping bin is full!\n\nMaximum " + ShippingBin.MAX_UNIQUE_ITEMS +
                        " unique item types per day.", "Bin Full", JOptionPane.WARNING_MESSAGE, WARNING_COLOR);
                return;
            }

            playerInventory.removeItemByName(item.getName(), quantity);
            itemsStagedInUI.put(item, itemsStagedInUI.getOrDefault(item, 0) + quantity);
            refreshUI();

            showStyledMessage("✅ Added " + quantity + "x " + item.getName() +
                    " to shipping bin!", "Item Added", JOptionPane.INFORMATION_MESSAGE, SUCCESS_COLOR);
        }
    }

    private void handleRemoveItemFromBin(Item item, int currentQuantityInBin) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(5, 5, 5, 5);
        JLabel itemLabel = new JLabel("📤 " + item.getName());
        itemLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        itemLabel.setForeground(ACCENT_COLOR);
        panel.add(itemLabel, gbc);

        gbc.gridy++;
        JLabel currentLabel = new JLabel("In bin: " + currentQuantityInBin);
        currentLabel.setForeground(ACCENT_COLOR);
        panel.add(currentLabel, gbc);

        gbc.gridy++;
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(currentQuantityInBin, 1, currentQuantityInBin, 1));
        quantitySpinner.setPreferredSize(new Dimension(100, 25));
        panel.add(quantitySpinner, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Return to Inventory", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int quantityToRemove = (Integer) quantitySpinner.getValue();

            playerInventory.addItem(item, quantityToRemove);

            int newQuantityInBin = itemsStagedInUI.get(item) - quantityToRemove;
            if (newQuantityInBin <= 0) {
                itemsStagedInUI.remove(item);
            } else {
                itemsStagedInUI.put(item, newQuantityInBin);
            }
            refreshUI();

            showStyledMessage("✅ Returned " + quantityToRemove + "x " + item.getName() +
                    " to inventory!", "Item Returned", JOptionPane.INFORMATION_MESSAGE, SUCCESS_COLOR);
        }
    }

    private void handleSell() {
        if (itemsStagedInUI.isEmpty()) {
            showStyledMessage("📦 No items staged to sell.\n\nAdd items to the shipping bin first!",
                    "Empty Bin", JOptionPane.INFORMATION_MESSAGE, WARNING_COLOR);
            return;
        }

        long currentUniqueInBinModel = shippingBinModel.getItemsConfirmedForSale().keySet().stream().distinct().count();
        long newUniqueStagedItems = itemsStagedInUI.keySet().stream()
                .filter(stagedItem -> !shippingBinModel.getItemsConfirmedForSale().containsKey(stagedItem))
                .distinct()
                .count();

        if (currentUniqueInBinModel + newUniqueStagedItems > ShippingBin.MAX_UNIQUE_ITEMS) {
            showStyledMessage("❌ Cannot sell items!\n\nTotal unique items for today would exceed " +
                    ShippingBin.MAX_UNIQUE_ITEMS + " limit.", "Limit Exceeded", JOptionPane.ERROR_MESSAGE, ERROR_COLOR);
            return;
        }

        int totalItems = itemsStagedInUI.values().stream().mapToInt(Integer::intValue).sum();
        int totalGold = itemsStagedInUI.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getSellPrice() * entry.getValue())
                .sum();

        String message = "💰 Confirm Sale\n\n" +
                "Items to sell: " + totalItems + "\n" +
                "Potential gold: " + totalGold + "g\n\n" +
                "Gold will be collected tomorrow morning.";

        int confirm = JOptionPane.showConfirmDialog(this, message, "Confirm Sale",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            for(Map.Entry<Item, Integer> entry : itemsStagedInUI.entrySet()) {
                shippingBinModel.addItemToConfirmedSales(entry.getKey(), entry.getValue());
            }

            itemsStagedInUI.clear();
            gameCalendar.addTime(15);
            gameCalendar.startTime();

            gameScreenPanel.showMessage("✅ Items placed in shipping bin! Gold will be collected tomorrow morning.");
            dispose();
        }
    }

    private void handleCancel() {
        if (!itemsStagedInUI.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "❓ Return all staged items to inventory?", "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        for (Map.Entry<Item, Integer> entry : itemsStagedInUI.entrySet()) {
            playerInventory.addItem(entry.getKey(), entry.getValue());
        }
        itemsStagedInUI.clear();

        gameCalendar.startTime();
        gameScreenPanel.showMessage("📦 Shipping bin operation cancelled.");
        dispose();
    }

    private void showStyledMessage(String message, String title, int messageType, Color accentColor) {
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("OptionPane.messageForeground", ACCENT_COLOR);

        JOptionPane.showMessageDialog(this, message, title, messageType);

        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
    }
}