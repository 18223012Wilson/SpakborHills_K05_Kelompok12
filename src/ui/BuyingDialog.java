package ui;

import model.entitas.Player;
import model.entitas.Emily;
import model.calendar.GameCalendar;
import model.items.Item;
import model.items.Inventory;
import model.items.Recipe;
import model.items.RecipeDatabase;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class BuyingDialog extends JDialog {
    private Player player;
    private GameCalendar calendar;
    private Emily emily;
    private GameScreenPanel gameScreenPanel;
    private Inventory playerInventory;

    private JPanel itemsPanel;
    private JLabel goldLabel;
    private final int SLOT_WIDTH = 120;
    private final int SLOT_HEIGHT = 150;
    private final int ITEMS_PER_ROW = 4;

    private static final Color BACKGROUND_COLOR = new Color(250, 240, 230);
    private static final Color PANEL_COLOR = new Color(255, 250, 245);
    private static final Color ACCENT_COLOR = new Color(198, 108, 77);
    private static final Color BUTTON_COLOR = new Color(100, 150, 100);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color GOLD_TEXT_COLOR = new Color(204, 153, 0);
    private static final Color DISABLED_BUTTON_COLOR = Color.LIGHT_GRAY;
    private static final Color DISABLED_TEXT_COLOR = Color.DARK_GRAY;


    public BuyingDialog(Frame owner, Player player, GameCalendar calendar, Emily emily, GameScreenPanel gameScreenPanel) {
        super(owner, "🛍️ Emily's Store - Happy Shopping!", true);
        this.player = player;
        this.calendar = calendar;
        this.emily = emily;
        this.gameScreenPanel = gameScreenPanel;
        this.playerInventory = player.getInventory();

        initComponents();
        refreshItemsForSaleDisplay();
        pack();
        setMinimumSize(new Dimension(ITEMS_PER_ROW * (SLOT_WIDTH + 15) + 60, 450));
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
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout(10,5));
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Welcome to Emily's Store!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setForeground(ACCENT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        goldLabel = new JLabel("💰 Your Gold: " + player.getGold() + "g");
        goldLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        goldLabel.setForeground(GOLD_TEXT_COLOR);
        goldLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(goldLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);


        itemsPanel = new JPanel();
        itemsPanel.setBackground(PANEL_COLOR);
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.getViewport().setBackground(PANEL_COLOR);
        scrollPane.setBorder(new LineBorder(ACCENT_COLOR.darker(), 2));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        JButton cancelButton = new JButton("Exit Store Menu");
        styleButton(cancelButton, ACCENT_COLOR.darker().darker(), BUTTON_TEXT_COLOR);
        cancelButton.addActionListener(e -> closeDialog());
        bottomPanel.add(cancelButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshItemsForSaleDisplay() {
        itemsPanel.removeAll();
        goldLabel.setText("💰 Your Gold: " + player.getGold() + "g");

        List<Item> itemsToDisplay = new ArrayList<>(Emily.getItemsForSaleStatic());
        itemsToDisplay.sort(Comparator.comparing(Item::getCategory).thenComparing(Item::getName));

        if (itemsToDisplay.isEmpty()) {
            itemsPanel.setLayout(new BorderLayout());
            JLabel emptyLabel = new JLabel("<html><div style='text-align: center;'>Sorry, no items in stock right now!</div></html>", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            itemsPanel.add(emptyLabel, BorderLayout.CENTER);
        }
        else {
            int numRows = Math.max(1, (int) Math.ceil((double) itemsToDisplay.size() / ITEMS_PER_ROW));
            itemsPanel.setLayout(new GridLayout(numRows, ITEMS_PER_ROW, 10, 10));
            for (Item item : itemsToDisplay) {
                itemsPanel.add(createItemSlot(item));
            }
            int currentSlots = itemsPanel.getComponentCount();
            int totalSlots = numRows * ITEMS_PER_ROW;
            for (int i = currentSlots; i < totalSlots; i++) {
                JPanel emptySlot = new JPanel();
                emptySlot.setPreferredSize(new Dimension(SLOT_WIDTH, SLOT_HEIGHT));
                emptySlot.setBackground(PANEL_COLOR);
                itemsPanel.add(emptySlot);
            }
        }
        itemsPanel.revalidate();
        itemsPanel.repaint();
        if (isVisible()) {
            SwingUtilities.invokeLater(this::pack);
        }
    }

    private JPanel createItemSlot(Item item) {
        JPanel slotPanel = new JPanel(new BorderLayout(5, 5));
        slotPanel.setPreferredSize(new Dimension(SLOT_WIDTH, SLOT_HEIGHT));
        slotPanel.setBackground(BACKGROUND_COLOR);
        slotPanel.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 1, true),
                new EmptyBorder(8,8,8,8))
        );

        String tooltipText = String.format("<html><b>%s</b> (%s)<br/>Price: %dg</html>",
                item.getName(), item.getCategory(), item.getBuyPrice());

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(SLOT_WIDTH - 20, SLOT_HEIGHT - 70));
        try {
            URL imgUrl = getClass().getResource(item.getImagePath());
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image image = icon.getImage().getScaledInstance(SLOT_WIDTH - 40, SLOT_HEIGHT - 80, Image.SCALE_SMOOTH);
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

        JLabel nameLabel = new JLabel("<html><div style='text-align:center; width: " + (SLOT_WIDTH - 20) + "px;'>" + item.getName() + "</div></html>");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        slotPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel bottomInfoPanel = new JPanel(new BorderLayout());
        bottomInfoPanel.setOpaque(false);

        JLabel priceLabel = new JLabel(item.getBuyPrice() + "g");
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        priceLabel.setForeground(GOLD_TEXT_COLOR);
        bottomInfoPanel.add(priceLabel, BorderLayout.WEST);

        JButton buyButton = new JButton("Buy");
        styleButton(buyButton, BUTTON_COLOR, BUTTON_TEXT_COLOR);
        buyButton.setFont(new Font("SansSerif", Font.BOLD, 11));
        buyButton.setMargin(new Insets(3,8,3,8));

        boolean canBuy = true;
        if (item.getName().equalsIgnoreCase("Proposal Ring") && playerInventory.getItemCountByName("Proposal Ring") > 0) {
            buyButton.setText("Owned");
            tooltipText += "<br/><i>You already own this.</i>";
            canBuy = false;
        } else if (item.getName().startsWith("Recipe:")) {
            String recipeNameKey = item.getName().substring("Recipe:".length()).trim();
            Recipe recipeObj = RecipeDatabase.getAllRecipes().stream()
                    .filter(r -> r.getName().equalsIgnoreCase(recipeNameKey))
                    .findFirst().orElse(null);
            if (recipeObj != null && player.hasUnlockedRecipe(recipeObj.getId())) {
                buyButton.setText("Learned");
                tooltipText += "<br/><i>Recipe already learned.</i>";
                canBuy = false;
            }
        }

        if (!canBuy || player.getGold() < item.getBuyPrice() || item.getBuyPrice() == 0) {
            buyButton.setEnabled(false);
            buyButton.setBackground(DISABLED_BUTTON_COLOR);
            buyButton.setForeground(DISABLED_TEXT_COLOR);
            if (item.getBuyPrice() == 0 && canBuy) buyButton.setText("N/A");
        }
        slotPanel.setToolTipText(tooltipText);


        buyButton.addActionListener(e -> handleBuyItem(item));
        bottomInfoPanel.add(buyButton, BorderLayout.EAST);
        slotPanel.add(bottomInfoPanel, BorderLayout.SOUTH);

        slotPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                slotPanel.setBackground(PANEL_COLOR.brighter());
                slotPanel.setBorder(new CompoundBorder(
                        new LineBorder(buyButton.isEnabled() ? BUTTON_COLOR.darker() : ACCENT_COLOR, 2, true),
                        new EmptyBorder(8,8,8,8))
                );
            }
            @Override
            public void mouseExited(MouseEvent e) {
                slotPanel.setBackground(BACKGROUND_COLOR);
                slotPanel.setBorder(new CompoundBorder(
                        new LineBorder(ACCENT_COLOR, 1, true),
                        new EmptyBorder(8,8,8,8))
                );
            }
        });
        return slotPanel;
    }

    private void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker()),
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

    private void handleBuyItem(Item item) {
        if (item.getBuyPrice() == 0) {
            gameScreenPanel.showMessage("This item cannot be bought.");
            return;
        }
        if (player.getGold() < item.getBuyPrice()) {
            gameScreenPanel.showMessage("Not enough gold to buy " + item.getName() + "!");
            return;
        }

        int quantityToBuy = 1;
        boolean isSinglePurchaseItem = item.getName().equalsIgnoreCase("Proposal Ring") || item.getName().startsWith("Recipe:");

        if (!isSinglePurchaseItem) {
            JPanel panel = new JPanel(new GridLayout(0,1, 5,5));
            panel.add(new JLabel("Enter quantity to buy for " + item.getName() + ":"));
            int maxAffordable = player.getGold() / item.getBuyPrice();
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, Math.max(1, maxAffordable), 1));
            panel.add(quantitySpinner);

            int result = JOptionPane.showConfirmDialog(this, panel, "Specify Quantity",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                quantityToBuy = (Integer) quantitySpinner.getValue();
            } else {
                return;
            }
        }

        int totalCost = item.getBuyPrice() * quantityToBuy;
        if (player.getGold() < totalCost) {
            gameScreenPanel.showMessage("Not enough gold to buy " + quantityToBuy + " " + item.getName() + "!");
            return;
        }

        String confirmMsg = "Buy " + quantityToBuy + " " + item.getName() + " for " + totalCost + "g?";
        int confirm = JOptionPane.showConfirmDialog(this, confirmMsg, "Confirm Purchase", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            player.spendGold(totalCost);
            playerInventory.addItem(item, quantityToBuy);
            gameScreenPanel.showMessage("Purchased " + quantityToBuy + " " + item.getName() + "!");

            if (item.getName().startsWith("Recipe:")) {
                String recipeNameKey = item.getName().substring("Recipe:".length()).trim();
                for (Recipe recipe : RecipeDatabase.getAllRecipes()) {
                    if (recipe.getName().equalsIgnoreCase(recipeNameKey)) {
                        if (!player.hasUnlockedRecipe(recipe.getId())) {
                            player.getUnlockedRecipes().add(recipe.getId());
                            gameScreenPanel.showMessage("Recipe '" + recipe.getName() + "' unlocked!");
                        }
                        break;
                    }
                }
            }
            refreshItemsForSaleDisplay();
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