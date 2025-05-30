package ui;

import model.entitas.Player;
import model.entitas.NPC;
import model.calendar.GameCalendar;
import model.actions.Chatting;
import model.actions.ProcessGiftingAction;
import model.actions.Visiting;
import main.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class StatisticsDialog extends JDialog {
    private Player player;
    private GameCalendar calendar;
    private List<NPC> npcList;
    private Main mainFrame;

    private static final Color PRIMARY_BG = new Color(87, 59, 46);
    private static final Color SECONDARY_BG_TABS = new Color(222,184,135);
    private static final Color CARD_CONTENT_BG = new Color(253, 246, 227);
    private static final Color TEXT_ON_DARK_BG = new Color(240, 230, 210);
    private static final Color TEXT_ON_LIGHT_BG = new Color(87, 59, 46);
    private static final Color ACCENT_GREEN = new Color(124, 178, 90);
    private static final Color ACCENT_GOLD = new Color(255, 215, 0);
    private static final Color ACCENT_BLUE = new Color(100, 149, 237);
    private static final Color ACCENT_RED = new Color(205, 92, 92);
    private static final Color BORDER_COLOR = new Color(111, 78, 55);

    private static final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Georgia", Font.BOLD, 18);
    private static final Font SUB_HEADER_FONT = new Font("Georgia", Font.PLAIN, 15);
    private static final Font CONTENT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font ACCENT_FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font SMALL_ACCENT_FONT = new Font("Segoe UI", Font.PLAIN, 11);


    public StatisticsDialog(Frame owner, Player player, GameCalendar calendar, List<NPC> npcList, Main mainFrame) {
        super(owner, "📈 Statistik Pertanian - Tahun " + ((calendar.getDay() - 1) / 10 + 1), true);
        this.player = player;
        this.calendar = calendar;
        this.npcList = npcList;
        this.mainFrame = mainFrame;

        initializeComponents();
        setupDialog();
    }

    private void setupDialog() {
        pack();
        setMinimumSize(new Dimension(800, 700));
        setPreferredSize(new Dimension(850, 750));
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });
    }

    private void initializeComponents() {
        getContentPane().setBackground(PRIMARY_BG);
        setLayout(new BorderLayout(0, 0));

        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(PRIMARY_BG);
        contentPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        contentPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        contentPanel.add(createMainContentPanel(), BorderLayout.CENTER);
        contentPanel.add(createActionPanel(), BorderLayout.SOUTH);

        add(contentPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(15,0));
        headerPanel.setOpaque(false);

        JLabel mainTitle = new JLabel("🌾 Catatan Kemajuan Pertanian 🌾");
        mainTitle.setFont(TITLE_FONT);
        mainTitle.setForeground(TEXT_ON_DARK_BG);
        mainTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainTitle.setBorder(new EmptyBorder(0,0,15,0));
        headerPanel.add(mainTitle, BorderLayout.NORTH);

        headerPanel.add(createQuickStatsPanel(), BorderLayout.CENTER);

        String seasonName = String.valueOf(calendar.getSeason());
        JLabel seasonInfo = new JLabel(seasonName + " • Hari ke-" + calendar.getDay() + " • Tahun " + (((calendar.getDay()-1)/40)+1) );
        seasonInfo.setFont(SUB_HEADER_FONT);
        seasonInfo.setForeground(ACCENT_GREEN);
        seasonInfo.setHorizontalAlignment(SwingConstants.CENTER);
        seasonInfo.setBorder(new EmptyBorder(10,0,10,0));
        headerPanel.add(seasonInfo, BorderLayout.SOUTH);


        return headerPanel;
    }

    private JPanel createQuickStatsPanel() {
        JPanel quickStats = new JPanel(new GridLayout(1, 4, 20, 0));
        quickStats.setOpaque(false);
        quickStats.setBorder(new EmptyBorder(10, 0, 15, 0));

        quickStats.add(createStatCard("💰", formatGold(Player.getTotalIncome()), "Total Pendapatan", ACCENT_GOLD));
        quickStats.add(createStatCard("🌱", String.valueOf(Player.getTotalCropsHarvested()), "Tanaman Dipanen", ACCENT_GREEN));
        quickStats.add(createStatCard("🐟", String.valueOf(Player.getOverallFishCaught()), "Ikan Tertangkap", ACCENT_BLUE));
        quickStats.add(createStatCard("📅", String.valueOf(Player.getTotalDaysPlayed()), "Hari Bermain", BORDER_COLOR));

        return quickStats;
    }

    private JPanel createStatCard(String icon, String value, String label, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_CONTENT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(170, 90));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(4, 4, 4, 4);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        gbc.gridx = 0; gbc.gridy = 0;
        card.add(iconLabel, gbc);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        valueLabel.setForeground(accentColor);
        gbc.gridy = 1;
        card.add(valueLabel, gbc);

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(SMALL_ACCENT_FONT);
        labelLabel.setForeground(TEXT_ON_LIGHT_BG.darker());
        gbc.gridy = 2;
        card.add(labelLabel, gbc);

        return card;
    }

    private JPanel createMainContentPanel() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(SUB_HEADER_FONT);
        tabbedPane.setBackground(SECONDARY_BG_TABS);
        tabbedPane.setForeground(TEXT_ON_LIGHT_BG);

        UIManager.put("TabbedPane.selected", CARD_CONTENT_BG);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabInsets", new Insets(8, 15, 8, 15));
        UIManager.put("TabbedPane.focus", new Color(0,0,0,0));

        tabbedPane.addTab("Keuangan", createFinanceTab());
        tabbedPane.addTab("Hubungan", createRelationshipTab());
        tabbedPane.addTab("Aktivitas", createActivityTab());
        tabbedPane.addTab("Pencapaian", createAchievementTab());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(tabbedPane, BorderLayout.CENTER);
        return wrapper;
    }


    private JTextArea createStyledTextArea(String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setFont(CONTENT_FONT);
        textArea.setBackground(CARD_CONTENT_BG);
        textArea.setForeground(TEXT_ON_LIGHT_BG);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(15, 20, 15, 20));
        return textArea;
    }

    private CompoundBorder createSectionBorder(String title) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1,0,0,0, BORDER_COLOR.brighter()),
                BorderFactory.createTitledBorder(
                        new EmptyBorder(10, 0, 10, 0),
                        title,
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        HEADER_FONT,
                        ACCENT_GREEN
                )
        );
    }
    private JPanel createFinanceTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(CARD_CONTENT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel leftPanel = createFinanceOverview();
        JPanel rightPanel = createFinanceChartPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(380);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFinanceOverview() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(createSectionBorder("💰 Ringkasan Keuangan"));

        StringBuilder content = new StringBuilder();
        content.append("🏦 STATUS KEUANGAN SAAT INI\n");
        content.append("──────────────────────────────\n");
        content.append("   Total Pemasukan:    ").append(formatGold(Player.getTotalIncome())).append("\n");
        content.append("   Total Pengeluaran: ").append(formatGold(Player.getTotalExpenditure())).append("\n");
        content.append("   Keuntungan Bersih:  ").append(formatGold(Player.getTotalIncome() - Player.getTotalExpenditure())).append("\n\n");

        int seasonsPassed = (Player.getTotalDaysPlayed() > 0) ? ((Player.getTotalDaysPlayed() - 1) / 10) + 1 : 0;
        content.append("📊 RATA-RATA PER MUSIM\n");
        content.append("──────────────────────────────\n");

        if (seasonsPassed > 0) {
            content.append("   Pemasukan / Musim:   ").append(formatGold(Player.getTotalIncome() / seasonsPassed)).append("\n");
            content.append("   Pengeluaran / Musim: ").append(formatGold(Player.getTotalExpenditure() / seasonsPassed)).append("\n");
        }
        else {
            content.append("   Pemasukan / Musim:   Belum ada data\n");
            content.append("   Pengeluaran / Musim: Belum ada data\n");
        }

        JTextArea textArea = createStyledTextArea(content.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFinanceChartPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(createSectionBorder("📈 Analisis Finansial"));

        int totalIncome = Player.getTotalIncome();
        int totalExpense = Player.getTotalExpenditure();

        if (totalIncome > 0 || totalExpense > 0) {
            FinanceBarChart barChart = new FinanceBarChart(totalIncome, totalExpense);
            panel.add(barChart, BorderLayout.CENTER);
        }
        else {
            JLabel noDataLabel = new JLabel("📊 Belum ada data keuangan untuk ditampilkan.", SwingConstants.CENTER);
            noDataLabel.setFont(CONTENT_FONT);
            noDataLabel.setForeground(TEXT_ON_LIGHT_BG.darker());
            panel.add(noDataLabel, BorderLayout.CENTER);
        }
        return panel;
    }

    private static class FinanceBarChart extends JPanel {
        private int income;
        private int expense;
        private static final int BAR_HEIGHT = 40;
        private static final int BAR_SPACING = 20;
        private static final int PADDING = 30;

        public FinanceBarChart(int income, int expense) {
            this.income = income;
            this.expense = expense;
            setOpaque(false);
            setPreferredSize(new Dimension(300, BAR_HEIGHT * 2 + BAR_SPACING + PADDING * 2));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int maxValue = Math.max(income, expense);
            if (maxValue == 0) {
                g2.setFont(CONTENT_FONT);
                g2.setColor(TEXT_ON_LIGHT_BG);
                g2.drawString("Tidak ada data pemasukan/pengeluaran.", PADDING, PADDING + BAR_HEIGHT);
                g2.dispose();
                return;
            }

            int chartWidth = getWidth() - 2 * PADDING;

            int incomeBarWidth = (int) ((double) income / maxValue * chartWidth);
            int expenseBarWidth = (int) ((double) expense / maxValue * chartWidth);

            g2.setFont(ACCENT_FONT_BOLD);

            g2.setColor(ACCENT_GREEN);
            g2.fillRoundRect(PADDING, PADDING, incomeBarWidth, BAR_HEIGHT, 10, 10);
            g2.setColor(TEXT_ON_LIGHT_BG);
            g2.drawString("Pemasukan: " + formatGoldStatic(income), PADDING + 5, PADDING + BAR_HEIGHT / 2 + g2.getFontMetrics().getAscent() / 2);

            int expenseY = PADDING + BAR_HEIGHT + BAR_SPACING;
            g2.setColor(ACCENT_RED);
            g2.fillRoundRect(PADDING, expenseY, expenseBarWidth, BAR_HEIGHT, 10, 10);
            g2.setColor(TEXT_ON_LIGHT_BG);
            g2.drawString("Pengeluaran: " + formatGoldStatic(expense), PADDING + 5, expenseY + BAR_HEIGHT / 2 + g2.getFontMetrics().getAscent() / 2);

            g2.dispose();
        }
        private static String formatGoldStatic(int amount) {
            return String.format("%,d", amount) + "g";
        }
    }


    private JPanel createRelationshipTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_CONTENT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel contentArea = new JPanel();
        contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.Y_AXIS));
        contentArea.setOpaque(false);

        JLabel title = new JLabel("💝 Hubungan dengan Warga Desa 💝");
        title.setFont(HEADER_FONT);
        title.setForeground(TEXT_ON_LIGHT_BG);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0,0,15,0));
        contentArea.add(title);

        if (npcList != null && !npcList.isEmpty()) {
            for (NPC npc : npcList) {
                if (npc != null) {
                    contentArea.add(createNpcRelationshipCard(npc));
                    contentArea.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }
        else {
            JLabel emptyLabel = new JLabel("<html><div style='text-align:center;'>🌱 Belum ada data hubungan.<br/>Keluarlah dan berteman!</div></html>", SwingConstants.CENTER);
            emptyLabel.setFont(CONTENT_FONT);
            emptyLabel.setForeground(TEXT_ON_LIGHT_BG.darker());
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentArea.add(emptyLabel);
        }

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);


        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createNpcRelationshipCard(NPC npc) {
        JPanel card = new JPanel(new BorderLayout(10,5));
        card.setBackground(CARD_CONTENT_BG.brighter());
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(10,15,10,15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel npcNameLabel = new JLabel(npc.getName());
        npcNameLabel.setFont(ACCENT_FONT_BOLD);
        npcNameLabel.setForeground(TEXT_ON_LIGHT_BG);
        card.add(npcNameLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(2,1,0,3));
        detailsPanel.setOpaque(false);

        HeartProgressBar heartBar = new HeartProgressBar(npc.getHeartPoints(), npc.getMaxHeartPoints());
        detailsPanel.add(heartBar);

        JLabel statusLabel = new JLabel("Status: " + npc.getRelationshipStatus() +
                " (" + npc.getHeartPoints() + "/" + npc.getMaxHeartPoints() + ")");
        statusLabel.setFont(CONTENT_FONT);
        statusLabel.setForeground(TEXT_ON_LIGHT_BG.darker());
        detailsPanel.add(statusLabel);

        card.add(detailsPanel, BorderLayout.CENTER);
        return card;
    }

    private static class HeartProgressBar extends JComponent {
        private int currentPoints;
        private int maxPoints;
        private static final int HEART_SIZE = 16;
        private static final int HEART_SPACING = 2;
        private static final Color HEART_FULL_COLOR = new Color(220, 50, 50);
        private static final Color HEART_EMPTY_COLOR = new Color(200, 200, 200);
        private static final int POINTS_PER_HEART_SEGMENT = 15;

        public HeartProgressBar(int currentPoints, int maxPoints) {
            this.currentPoints = currentPoints;
            this.maxPoints = maxPoints;
            int numHearts = maxPoints / POINTS_PER_HEART_SEGMENT;
            setPreferredSize(new Dimension(numHearts * (HEART_SIZE + HEART_SPACING) - HEART_SPACING, HEART_SIZE));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int numHeartSegments = maxPoints / POINTS_PER_HEART_SEGMENT;
            int filledSegments = currentPoints / POINTS_PER_HEART_SEGMENT;
            int baseY = 0;

            for (int i = 0; i < numHeartSegments; i++) {
                int x = i * (HEART_SIZE + HEART_SPACING);
                if (i < filledSegments) {
                    g2.setColor(HEART_FULL_COLOR);
                } else {
                    g2.setColor(HEART_EMPTY_COLOR);
                }

                Path2D.Double heartPath = new Path2D.Double();
                heartPath.moveTo(x + HEART_SIZE / 2.0, baseY + HEART_SIZE / 4.0);
                heartPath.quadTo(x, baseY + HEART_SIZE / 5.0, x + HEART_SIZE / 4.0, baseY + HEART_SIZE / 2.0);
                heartPath.quadTo(x + HEART_SIZE / 2.0, baseY + HEART_SIZE * 0.9, x + 3.0 * HEART_SIZE / 4.0, baseY + HEART_SIZE / 2.0);
                heartPath.quadTo(x + HEART_SIZE, baseY + HEART_SIZE / 5.0, x + HEART_SIZE / 2.0, baseY + HEART_SIZE / 4.0);
                heartPath.closePath();
                g2.fill(heartPath);
            }
            g2.dispose();
        }
    }


    private JPanel createActivityTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_CONTENT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        StringBuilder content = new StringBuilder();
        content.append("🎯 REKAP AKTIVITAS HARIAN\n");
        content.append("══════════════════════════════\n\n");

        content.append("🗣️ AKTIVITAS SOSIAL\n");
        content.append("──────────────────────────────\n");
        content.append("   💬 Percakapan:                     ").append(Chatting.getFrequency()).append(" kali\n");
        content.append("   🎁 Gift Diberikan:         ").append(ProcessGiftingAction.getFrequency()).append(" kali\n");
        content.append("   🚶 Visiting Area:            ").append(Visiting.getFrequency()).append(" kali\n\n");

        content.append("🌾 AKTIVITAS PERTANIAN\n");
        content.append("──────────────────────────────\n");
        content.append("   🌱 Total Panen:                     ").append(Player.getTotalCropsHarvested()).append(" tanaman\n\n");

        content.append("🎣 AKTIVITAS MEMANCING\n");
        content.append("──────────────────────────────\n");
        content.append("   🐟 Total Ikan Tertangkap: ").append(Player.getOverallFishCaught()).append("\n");
        content.append("      🐠 Ikan Common:                 ").append(Player.getCommonFishCaught()).append("\n");
        content.append("      🐡 Ikan Regular:             ").append(Player.getRegularFishCaught()).append("\n");
        content.append("      🐲 Ikan LEGENDARY:        ").append(Player.getLegendaryFishCaught()).append("\n");

        JTextArea textArea = createStyledTextArea(content.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAchievementTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_CONTENT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        StringBuilder content = new StringBuilder();
        content.append("🏆 PENCAPAIAN & PRESTASI\n");
        content.append("══════════════════════════════\n\n");

        content.append("⏰ WAKTU BERMAIN\n");
        content.append("──────────────────────────────\n");
        content.append("   📅 Total Hari:          ").append(Player.getTotalDaysPlayed()).append(" hari\n");
        content.append("   🗓️ Musim Selesai: ").append(Player.getTotalDaysPlayed() / 10).append(" musim\n\n");

        content.append("🎖️ PRESTASI PERTANIAN\n");
        content.append("──────────────────────────────\n");
        content.append(Player.getTotalCropsHarvested() >= 100 ? "   🌾 Master Panen                    ✓ (100+ tanaman)\n" :
                "   🌾 Master Panen                    ⏳ (" + Player.getTotalCropsHarvested() + "/100 tanaman)\n");
        content.append(Player.getTotalIncome() >= 10000 ? "   💰 Petani Kaya                      ✓ (10.000g +)\n" :
                "   💰 Petani Kaya                      ⏳ (" + formatGold(Player.getTotalIncome()) + "/10.000g)\n");

        content.append("\n🎣 PRESTASI MEMANCING\n");
        content.append("──────────────────────────────\n");
        content.append(Player.getOverallFishCaught() >= 50 ? "   🐟 Pemancing Ulung             ✓ (50+ ikan)\n" :
                "   🐟 Pemancing Ulung             ⏳ (" + Player.getOverallFishCaught() + "/50 ikan)\n");
        content.append(Player.getLegendaryFishCaught() > 0 ? "   🐲 Pemburu Legenda          ✓ (Ikan LEGENDARY)\n" :
                "   🐲 Pemburu Legenda          ⏳ (Belum ada)\n");

        content.append("\n💕 PRESTASI SOSIAL\n");
        content.append("──────────────────────────────\n");
        content.append(Chatting.getFrequency() >= 20 ? "   🗣️ Kupu-kupu Sosial           ✓ (20+ percakapan)\n" :
                "   🗣️ Kupu-kupu Sosial           ⏳ (" + Chatting.getFrequency() + "/20 percakapan)\n");
        content.append(ProcessGiftingAction.getFrequency() >= 10 ? "   🎁 Dermawan Hati             ✓ (10+ hadiah)\n" :
                "   🎁 Dermawan Hati             ⏳ (" + ProcessGiftingAction.getFrequency() + "/10 hadiah)\n");

        JTextArea textArea = createStyledTextArea(content.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }


    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.setOpaque(false);
        actionPanel.setBorder(new EmptyBorder(15,0,0,0));

        JButton closeButton = createModernButton("🚪 Tutup", BORDER_COLOR);
        closeButton.addActionListener(e -> closeDialog());

        actionPanel.add(closeButton);
        return actionPanel;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color baseColor = bgColor;
                Color lightColor = baseColor.brighter();

                if (getModel().isPressed()) {
                    g2.setColor(baseColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(lightColor);
                } else {
                    g2.setColor(baseColor);
                }

                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);

                g2.setColor(baseColor.darker());
                g2.drawRoundRect(2,2,getWidth()-5, getHeight()-5, 12,12);


                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setForeground(TEXT_ON_DARK_BG);
        button.setFont(ACCENT_FONT_BOLD);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 45));
        return button;
    }



    private String formatGold(int amount) {
        return String.format("%,d", amount) + "g";
    }

    private void closeDialog() {
        if (calendar != null) {
            calendar.startTime();
        }
        if (mainFrame != null && mainFrame.getGameScreenPanel() != null) {
            mainFrame.getGameScreenPanel().requestFocusInWindow();
        }
        setVisible(false);
        dispose();
    }
}