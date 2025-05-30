package ui;

import model.items.Inventory;
import model.items.Item;
import model.items.EdibleItem;
import model.calendar.GameCalendar;
import model.entitas.Player;
import model.actions.Eating;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class InventoryDialog extends JDialog {
    private Inventory inventory;
    private GameCalendar gameCalendar;
    private Player player; // Tambahkan player
    private GameScreenPanel gameScreenPanel;
    private JPanel itemsPanel;
    private final int SLOT_SIZE = 80;
    private final int ITEMS_PER_ROW = 5;


    public InventoryDialog(Frame owner, Player player, Inventory inventory, GameCalendar gameCalendar, GameScreenPanel gameScreenPanel) {
        super(owner, "Inventory", true);
        this.player = player;
        this.inventory = inventory;
        this.gameCalendar = gameCalendar;
        this.gameScreenPanel = gameScreenPanel;

        initComponents();
        refreshInventoryDisplay();
        pack();
        setLocationRelativeTo(owner);

        this.gameCalendar.pauseTime();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_I) {
                    closeDialog();
                }
            }
        });
        setFocusable(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        itemsPanel = new JPanel();
        int numItems = Math.max(1, inventory.getAllItemsWithQuantities().size());
        int numRows = (int) Math.ceil((double)numItems / ITEMS_PER_ROW) ;
        itemsPanel.setLayout(new GridLayout(numRows, ITEMS_PER_ROW, 10, 10));
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setPreferredSize(new Dimension(ITEMS_PER_ROW * (SLOT_SIZE + 10) + 40, 3 * (SLOT_SIZE +10)));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        JLabel instructionLabel = new JLabel("Klik item untuk opsi. Tekan 'I' atau 'Esc' untuk menutup.", SwingConstants.CENTER);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        add(instructionLabel, BorderLayout.SOUTH);
    }

    public void refreshInventoryDisplay() {
        itemsPanel.removeAll();
        Map<Item, Integer> currentItems = inventory.getAllItemsWithQuantities();

        List<Map.Entry<Item, Integer>> sortedItems = new ArrayList<>(currentItems.entrySet());
        sortedItems.sort(Comparator.comparing(entry -> entry.getKey().getName()));

        if (sortedItems.isEmpty()) {
            JLabel emptyLabel = new JLabel("Inventory kosong.");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            itemsPanel.setLayout(new BorderLayout());
            itemsPanel.add(emptyLabel, BorderLayout.CENTER);
        }
        else {
            int numItems = sortedItems.size();
            int numRows = Math.max(1, (int) Math.ceil((double)numItems / ITEMS_PER_ROW)) ;
            itemsPanel.setLayout(new GridLayout(numRows, ITEMS_PER_ROW, 10, 10));

            for (Map.Entry<Item, Integer> entry : sortedItems) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                itemsPanel.add(createItemSlot(item, quantity));
            }
            int totalSlots = numRows * ITEMS_PER_ROW;
            for (int i = sortedItems.size(); i < totalSlots; i++) {
                JPanel emptySlot = new JPanel();
                emptySlot.setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
                emptySlot.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                itemsPanel.add(emptySlot);
            }
        }

        itemsPanel.revalidate();
        itemsPanel.repaint();
        SwingUtilities.invokeLater(() -> {
            pack();
        });
    }

    private JPanel createItemSlot(Item item, int quantity) {
        JPanel slotPanel = new JPanel(new BorderLayout(5,5));
        slotPanel.setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
        slotPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        slotPanel.setToolTipText(item.getName() + " (" + item.getCategory() + ")");

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        try {
            String imagePath = item.getImagePath();
            URL imgUrl = getClass().getResource(imagePath);
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image image = icon.getImage().getScaledInstance(SLOT_SIZE - 30, SLOT_SIZE - 30, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
            }
            else {
                imageLabel.setText("N/A");
            }
        }
        catch (Exception e) {
            imageLabel.setText("Err");
        }
        slotPanel.add(imageLabel, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 10));
        slotPanel.add(nameLabel, BorderLayout.NORTH);

        JLabel quantityLabel = new JLabel("x" + quantity);
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        slotPanel.add(quantityLabel, BorderLayout.SOUTH);

        slotPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (item instanceof EdibleItem) {
                        showItemOptions(slotPanel, item, e.getX(), e.getY());
                    }
                }
            }
        });

        return slotPanel;
    }

    private void showItemOptions(Component component, Item item, int x, int y) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem eatMenuItem = new JMenuItem("Eat " + item.getName());
        eatMenuItem.addActionListener(e -> {
            Eating eatingAction = new Eating(player, gameCalendar, item);
            String result = eatingAction.execute();
            gameScreenPanel.showMessage(result);

            if (inventory.getItemCountByName(item.getName()) == 0) {
                refreshInventoryDisplay();
            }
            else {
                refreshInventoryDisplay();
            }
            if (inventory.getAllItemsWithQuantities().isEmpty()) {
                refreshInventoryDisplay();
            }
        });
        popupMenu.add(eatMenuItem);
        popupMenu.show(component, x, y);
    }

    private void closeDialog() {
        if (gameCalendar != null) {
            gameCalendar.startTime();
        }
        if (gameScreenPanel != null) {
            gameScreenPanel.requestFocusInWindow();
        }
        setVisible(false);
        dispose();
    }
}