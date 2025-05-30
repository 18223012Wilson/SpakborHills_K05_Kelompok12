package ui;

import model.entitas.Player;
import model.calendar.GameCalendar;
import model.items.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookingDialog extends JDialog {
    private Player player;
    private GameCalendar calendar;
    private GameScreenPanel gameScreenPanel;
    private Inventory inventory;

    private static final Color BACKGROUND_COLOR = new Color(245, 237, 220);
    private static final Color PANEL_COLOR = new Color(255, 248, 235);
    private static final Color ACCENT_COLOR = new Color(139, 69, 19);
    private static final Color SUCCESS_COLOR = new Color(76, 139, 55);
    private static final Color WARNING_COLOR = new Color(204, 85, 0);
    private static final Color ERROR_COLOR = new Color(180, 32, 32);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);

    private JComboBox<Recipe> recipeComboBox;
    private JPanel ingredientsPanel;
    private JScrollPane ingredientsScrollPane;
    private JSpinner servingsSpinner;
    private JComboBox<String> fuelComboBox;
    private JLabel fuelInfoLabel;
    private JLabel foodResultInfoLabel;
    private JButton cookButton;
    private JLabel feedbackLabel;
    private JProgressBar energyBar;
    private JLabel energyLabel;
    private JPanel recipePreviewPanel;
    private JTextArea recipeDescriptionArea;

    public CookingDialog(Frame owner, Player player, GameCalendar calendar, GameScreenPanel gameScreenPanel) {
        super(owner, "🍳 Kitchen - Cooking Menu", true);
        this.player = player;
        this.calendar = calendar;
        this.gameScreenPanel = gameScreenPanel;
        this.inventory = player.getInventory();

        setupLookAndFeel();
        initComponents();
        loadUnlockedRecipes();
        updateUIDisplay();

        pack();
        setMinimumSize(new Dimension(600, 550));
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                calendar.startTime();
                if (gameScreenPanel != null) {
                    gameScreenPanel.requestFocusInWindow();
                }
            }
        });
    }

    private void setupLookAndFeel() {
        getContentPane().setBackground(BACKGROUND_COLOR);
        setBackground(BACKGROUND_COLOR);
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(createStyledBorder("Player Status"));

        JPanel energyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        energyPanel.setBackground(BACKGROUND_COLOR);

        energyLabel = new JLabel("Energy: " + player.getEnergy());
        energyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        energyLabel.setForeground(TEXT_COLOR);
        energyPanel.add(energyLabel);

        energyBar = new JProgressBar(0, 100);
        energyBar.setValue(Math.max(0, player.getEnergy()));
        energyBar.setStringPainted(true);
        energyBar.setPreferredSize(new Dimension(150, 20));
        energyBar.setBackground(PANEL_COLOR);
        energyBar.setForeground(SUCCESS_COLOR);
        energyPanel.add(energyBar);

        headerPanel.add(energyPanel, BorderLayout.WEST);

        JLabel timeLabel = new JLabel("🕐 Time will advance by 1 hour");
        timeLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        timeLabel.setForeground(WARNING_COLOR);
        headerPanel.add(timeLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel recipeSelectionPanel = createRecipeSelectionPanel();
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(recipeSelectionPanel, gbc);

        recipePreviewPanel = createRecipePreviewPanel();
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.weightx = 0.4; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10);
        mainPanel.add(recipePreviewPanel, gbc);

        JPanel ingredientsContainer = createIngredientsPanel();
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(ingredientsContainer, gbc);

        return mainPanel;
    }

    private JPanel createRecipeSelectionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(createStyledBorder("Select Recipe"));

        JLabel label = new JLabel("Choose Recipe: ");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        panel.add(label);

        recipeComboBox = new JComboBox<>();
        recipeComboBox.setPreferredSize(new Dimension(200, 30));
        recipeComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        recipeComboBox.addActionListener(e -> updateUIDisplay());
        styleComboBox(recipeComboBox);
        panel.add(recipeComboBox);

        return panel;
    }

    private JPanel createRecipePreviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(createStyledBorder("Recipe Info"));

        foodResultInfoLabel = new JLabel("<html><center>Select a recipe<br>to see details</center></html>");
        foodResultInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        foodResultInfoLabel.setForeground(TEXT_COLOR);
        foodResultInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        foodResultInfoLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(foodResultInfoLabel, BorderLayout.NORTH);

        recipeDescriptionArea = new JTextArea(3, 15);
        recipeDescriptionArea.setBackground(PANEL_COLOR);
        recipeDescriptionArea.setForeground(TEXT_COLOR);
        recipeDescriptionArea.setFont(new Font("Arial", Font.ITALIC, 11));
        recipeDescriptionArea.setLineWrap(true);
        recipeDescriptionArea.setWrapStyleWord(true);
        recipeDescriptionArea.setEditable(false);
        recipeDescriptionArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.add(recipeDescriptionArea, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createIngredientsPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(BACKGROUND_COLOR);

        ingredientsPanel = new JPanel();
        ingredientsPanel.setLayout(new BoxLayout(ingredientsPanel, BoxLayout.Y_AXIS));
        ingredientsPanel.setBackground(PANEL_COLOR);

        ingredientsScrollPane = new JScrollPane(ingredientsPanel);
        ingredientsScrollPane.setBorder(createStyledBorder("Required Ingredients"));
        ingredientsScrollPane.setPreferredSize(new Dimension(300, 200));
        ingredientsScrollPane.getViewport().setBackground(PANEL_COLOR);
        container.add(ingredientsScrollPane, BorderLayout.CENTER);

        return container;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(createStyledBorder("Cooking Options"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        // Servings control
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel servingsLabel = new JLabel("Servings:");
        servingsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        servingsLabel.setForeground(TEXT_COLOR);
        bottomPanel.add(servingsLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        servingsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        servingsSpinner.setPreferredSize(new Dimension(80, 25));
        servingsSpinner.addChangeListener(e -> updateUIDisplay());
        styleSpinner(servingsSpinner);
        bottomPanel.add(servingsSpinner, gbc);

        // Fuel selection
        gbc.gridx = 2; gbc.gridy = 0; gbc.insets.left = 20;
        JLabel fuelLabel = new JLabel("Fuel Type:");
        fuelLabel.setFont(new Font("Arial", Font.BOLD, 12));
        fuelLabel.setForeground(TEXT_COLOR);
        bottomPanel.add(fuelLabel, gbc);

        gbc.gridx = 3; gbc.gridy = 0; gbc.insets.left = 10;
        fuelComboBox = new JComboBox<>(new String[]{"🪵 Firewood", "🪨 Coal"});
        fuelComboBox.setPreferredSize(new Dimension(120, 25));
        fuelComboBox.addActionListener(e -> updateUIDisplay());
        styleComboBox(fuelComboBox);
        bottomPanel.add(fuelComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4;
        gbc.insets = new Insets(5, 10, 10, 10);
        fuelInfoLabel = new JLabel("Select fuel type to see requirements");
        fuelInfoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        fuelInfoLabel.setForeground(TEXT_COLOR);
        bottomPanel.add(fuelInfoLabel, gbc);

        // Cook button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        gbc.insets = new Insets(10, 10, 5, 10);
        cookButton = new JButton("🍳 Start Cooking!");
        cookButton.setFont(new Font("Arial", Font.BOLD, 16));
        cookButton.setPreferredSize(new Dimension(200, 40));
        cookButton.addActionListener(this::performCooking);
        styleButton(cookButton);
        bottomPanel.add(cookButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        gbc.insets = new Insets(5, 10, 10, 10);
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 12));
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(feedbackLabel, gbc);

        return bottomPanel;
    }

    private void styleButton(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR.darker(), 2),
                new EmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(ACCENT_COLOR.brighter());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(ACCENT_COLOR);
                }
            }
        });
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(PANEL_COLOR);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBorder(new LineBorder(ACCENT_COLOR, 1));
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setBorder(new LineBorder(ACCENT_COLOR, 1));
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setBackground(PANEL_COLOR);
            ((JSpinner.DefaultEditor) editor).getTextField().setForeground(TEXT_COLOR);
        }
    }

    private CompoundBorder createStyledBorder(String title) {
        return new CompoundBorder(
                BorderFactory.createTitledBorder(
                        new LineBorder(ACCENT_COLOR, 2),
                        title,
                        0, 0,
                        new Font("Arial", Font.BOLD, 12),
                        ACCENT_COLOR
                ),
                new EmptyBorder(10, 10, 10, 10)
        );
    }

    private void loadUnlockedRecipes() {
        recipeComboBox.removeAllItems();
        List<Recipe> unlockedRecipes = RecipeDatabase.getUnlockedRecipes(player);
        if (unlockedRecipes.isEmpty()) {
            recipeComboBox.addItem(null);
        } else {
            for (Recipe recipe : unlockedRecipes) {
                recipeComboBox.addItem(recipe);
            }
        }
        recipeComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Recipe) {
                    setText("🍽️ " + ((Recipe) value).getName());
                } else {
                    setText("❌ No Recipes Unlocked");
                }
                setBackground(isSelected ? ACCENT_COLOR : PANEL_COLOR);
                setForeground(isSelected ? Color.WHITE : TEXT_COLOR);
                return this;
            }
        });
    }

    private void updateUIDisplay() {
        Recipe selectedRecipe = (Recipe) recipeComboBox.getSelectedItem();
        ingredientsPanel.removeAll();

        // Update energy display
        energyLabel.setText("Energy: " + player.getEnergy());
        energyBar.setValue(Math.max(0, player.getEnergy()));

        if (selectedRecipe == null) {
            foodResultInfoLabel.setText("<html><center>❌ No recipe selected<br>Choose a recipe to continue</center></html>");
            recipeDescriptionArea.setText("Select a recipe from the dropdown above to see ingredients and cooking information.");
            ingredientsPanel.add(createInfoLabel("Please select a recipe to see ingredients.", TEXT_COLOR));
            fuelInfoLabel.setText("⛽ Fuel: Select a recipe first");
            cookButton.setEnabled(false);
            updatePanelDisplay();
            return;
        }

        Food resultFood = findFoodByName(selectedRecipe.getName());
        if (resultFood != null) {
            foodResultInfoLabel.setText("<html><center>🍽️ <b>" + resultFood.getName() + "</b><br>+" + resultFood.getEnergyValue() + " Energy</center></html>");
            recipeDescriptionArea.setText("This delicious " + resultFood.getName() + " will restore " + resultFood.getEnergyValue() + " energy points. Perfect for maintaining your stamina throughout the day!");
        }
        else {
            foodResultInfoLabel.setText("<html><center>❌ Error<br>Cannot find recipe data</center></html>");
            recipeDescriptionArea.setText("There seems to be an issue with this recipe. Please try selecting a different recipe.");
        }

        int servings = (Integer) servingsSpinner.getValue();
        boolean allIngredientsAvailable = true;

        for (Map.Entry<String, Integer> entry : selectedRecipe.getIngredients().entrySet()) {
            String itemName = entry.getKey();
            int requiredPerServing = entry.getValue();
            int totalRequired = requiredPerServing * servings;
            int available = inventory.getItemCountByCategoryOrName(itemName);

            JPanel ingredientPanel = createIngredientPanel(itemName, available, totalRequired);
            ingredientsPanel.add(ingredientPanel);

            if (available < totalRequired) {
                allIngredientsAvailable = false;
            }
        }

        String fuelType = (String) fuelComboBox.getSelectedItem();
        String cleanFuelType = fuelType.replace("🪵 ", "").replace("🪨 ", "");
        int requiredFuel = getRequiredFuel(cleanFuelType, servings);
        int availableFuel = inventory.getItemCountByCategoryOrName(cleanFuelType);

        String fuelEmoji = fuelType.contains("🪵") ? "🪵" : "🪨";
        fuelInfoLabel.setText(String.format("⛽ Fuel: %d %s %s needed (Available: %d)",
                requiredFuel, fuelEmoji, cleanFuelType, availableFuel));

        if (availableFuel < requiredFuel) {
            fuelInfoLabel.setForeground(ERROR_COLOR);
            allIngredientsAvailable = false;
        }
        else {
            fuelInfoLabel.setForeground(SUCCESS_COLOR);
        }

        boolean hasEnoughEnergy = player.getEnergy() >= 10 && (player.getEnergy() - 10) > -20;

        cookButton.setEnabled(allIngredientsAvailable && resultFood != null && hasEnoughEnergy);

        if (!hasEnoughEnergy) {
            cookButton.setText("😴 Too Tired to Cook");
            cookButton.setBackground(ERROR_COLOR);
        } else if (!allIngredientsAvailable) {
            cookButton.setText("❌ Missing Ingredients");
            cookButton.setBackground(WARNING_COLOR);
        } else {
            cookButton.setText("🍳 Start Cooking!");
            cookButton.setBackground(ACCENT_COLOR);
        }

        updatePanelDisplay();
    }

    private JPanel createIngredientPanel(String itemName, int available, int required) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.setBorder(new EmptyBorder(3, 8, 3, 8));

        String icon = getIngredientIcon(itemName);
        JLabel nameLabel = new JLabel(icon + " " + itemName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel countLabel = new JLabel(available + " / " + required);
        countLabel.setFont(new Font("Arial", Font.BOLD, 12));

        if (available >= required) {
            panel.setBackground(new Color(220, 255, 220));
            nameLabel.setForeground(SUCCESS_COLOR);
            countLabel.setForeground(SUCCESS_COLOR);
            countLabel.setText("✅ " + countLabel.getText());
        }
        else {
            panel.setBackground(new Color(255, 220, 220));
            nameLabel.setForeground(ERROR_COLOR);
            countLabel.setForeground(ERROR_COLOR);
            countLabel.setText("❌ " + countLabel.getText());
        }

        panel.add(nameLabel, BorderLayout.WEST);
        panel.add(countLabel, BorderLayout.EAST);

        return panel;
    }

    private String getIngredientIcon(String itemName) {
        return switch (itemName.toLowerCase()) {
            case "wheat", "flour" -> "🌾";
            case "milk" -> "🥛";
            case "egg", "eggs" -> "🥚";
            case "cheese" -> "🧀";
            case "fish" -> "🐟";
            case "potato", "potatoes" -> "🥔";
            case "tomato", "tomatoes" -> "🍅";
            case "corn" -> "🌽";
            case "carrot", "carrots" -> "🥕";
            case "mushroom", "mushrooms" -> "🍄";
            case "apple", "apples" -> "🍎";
            case "sugar" -> "🍯";
            case "salt" -> "🧂";
            default -> "📦";
        };
    }

    private JLabel createInfoLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.ITALIC, 12));
        label.setForeground(color);
        label.setBorder(new EmptyBorder(10, 10, 10, 10));
        return label;
    }

    private void updatePanelDisplay() {
        ingredientsPanel.revalidate();
        ingredientsPanel.repaint();
        recipePreviewPanel.revalidate();
        recipePreviewPanel.repaint();
    }

    private void performCooking(ActionEvent e) {
        feedbackLabel.setText(" ");
        Recipe selectedRecipe = (Recipe) recipeComboBox.getSelectedItem();

        if (selectedRecipe == null || !cookButton.isEnabled()) {
            feedbackLabel.setForeground(ERROR_COLOR);
            feedbackLabel.setText("❌ Cannot cook. Check requirements above.");
            return;
        }

        int servings = (Integer) servingsSpinner.getValue();
        String fuelType = ((String) fuelComboBox.getSelectedItem()).replace("🪵 ", "").replace("🪨 ", "");
        Food cookedFood = findFoodByName(selectedRecipe.getName());

        int initialEnergyCost = 10;
        if (player.getEnergy() < initialEnergyCost || player.getEnergy() - initialEnergyCost < -20) {
            feedbackLabel.setForeground(ERROR_COLOR);
            feedbackLabel.setText("😴 Too tired to cook! Need at least " + initialEnergyCost + " energy.");
            gameScreenPanel.showMessage("Cooking failed: Not enough energy.");
            return;
        }

        feedbackLabel.setForeground(WARNING_COLOR);
        feedbackLabel.setText("🍳 Cooking in progress...");

        Timer timer = new Timer(500, evt -> {
            // Consume resources
            player.decreaseEnergy(initialEnergyCost);
            calendar.addTime(60);

            consumeIngredients(player, selectedRecipe.getIngredients(), servings);
            inventory.removeItemsByCategoryOrName(fuelType, getRequiredFuel(fuelType, servings));
            inventory.addItem(cookedFood, servings);

            String successMsg = "✅ Successfully cooked " + servings + " " + cookedFood.getName() + "!";
            feedbackLabel.setForeground(SUCCESS_COLOR);
            feedbackLabel.setText(successMsg);
            gameScreenPanel.showMessage(successMsg + " (Energy: " + player.getEnergy() + ")");

            updateUIDisplay();
            ((Timer)evt.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private int getRequiredFuel(String fuelType, int servings) {
        if (fuelType == null) return Integer.MAX_VALUE;
        return switch (fuelType.toLowerCase()) {
            case "firewood" -> servings;
            case "coal" -> (int) Math.ceil(servings / 2.0);
            default -> Integer.MAX_VALUE;
        };
    }

    private void consumeIngredients(Player p, Map<String, Integer> baseIngredients, int servings) {
        for (Map.Entry<String, Integer> entry : baseIngredients.entrySet()) {
            String ingredientName = entry.getKey();
            int quantityPerServing = entry.getValue();
            int totalToConsume = quantityPerServing * servings;
            p.getInventory().removeItemsByCategoryOrName(ingredientName, totalToConsume);
        }
    }

    private Food findFoodByName(String name) {
        for (Food food : Food.getAllFoods()) {
            if (food.getName().equalsIgnoreCase(name)) {
                return food;
            }
        }
        return null;
    }

    @Override
    public void dispose() {
        calendar.startTime();
        if (gameScreenPanel != null) {
            gameScreenPanel.requestFocusInWindow();
        }
        super.dispose();
    }
}