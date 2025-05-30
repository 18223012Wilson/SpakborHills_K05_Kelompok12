package ui;

import model.entitas.Player;
import model.entitas.NPC;
import model.calendar.GameCalendar;
import model.actions.Chatting;
import model.actions.ProcessGiftingAction;
import model.actions.Visiting;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class EndGameStatsDialog extends JDialog {
    private Player player;
    private GameCalendar calendar;
    private List<NPC> npcList;
    private String milestoneType;

    private static final Color PRIMARY_BG = new Color(240, 228, 210);
    private static final Color TEXT_COLOR = new Color(70, 40, 20);
    private static final Color ACCENT_COLOR_GOLD = new Color(204, 153, 0);
    private static final Color ACCENT_COLOR_LOVE = new Color(220, 20, 60);
    private static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 26);
    private static final Font STORY_FONT = new Font("Serif", Font.ITALIC, 16);
    private static final Font STATS_HEADER_FONT = new Font("SansSerif", Font.BOLD, 18);
    private static final Font STATS_FONT = new Font("SansSerif", Font.PLAIN, 14);

    public EndGameStatsDialog(Frame owner, Player player, GameCalendar calendar, List<NPC> npcList, String milestoneType) {
        super(owner, "Pencapaian di Bukit Spakbor!", true);
        this.player = player;
        this.calendar = calendar;
        this.npcList = npcList;
        this.milestoneType = milestoneType;

        initComponents();
        pack();
        setMinimumSize(new Dimension(650, 700));
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
        getContentPane().setBackground(PRIMARY_BG);
        setLayout(new BorderLayout(15, 15));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 25, 20, 25));

        JTextArea storyArea = new JTextArea(generateStoryMessage());
        storyArea.setFont(STORY_FONT);
        storyArea.setForeground(TEXT_COLOR);
        storyArea.setBackground(PRIMARY_BG);
        storyArea.setWrapStyleWord(true);
        storyArea.setLineWrap(true);
        storyArea.setEditable(false);
        storyArea.setFocusable(false);
        storyArea.setBorder(new EmptyBorder(10, 10, 20, 10));
        add(storyArea, BorderLayout.NORTH);

        JPanel statsPanel = createStatsDisplayPanel();
        JScrollPane scrollPane = new JScrollPane(statsPanel);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, TEXT_COLOR.brighter()));
        scrollPane.getViewport().setBackground(PRIMARY_BG);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(PRIMARY_BG);
        JButton closeButton = new JButton("Lanjutkan Petualangan");
        styleButton(closeButton, milestoneType.equals("gold") ? ACCENT_COLOR_GOLD.darker() : ACCENT_COLOR_LOVE.darker());
        closeButton.addActionListener(e -> closeDialog());
        buttonPanel.add(closeButton);
        buttonPanel.setBorder(new EmptyBorder(15,0,0,0));
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private String generateStoryMessage() {
        if ("gold".equals(milestoneType)) {
            return String.format("Selamat, %s!\n\nKerja kerasmu mencari nafkah di Bukit Spakbor telah membuahkan hasil yang luar biasa! " +
                            "Dengan tabungan sebesar %s, kamu telah membuktikan bahwa dedikasi dan cangkul yang tajam adalah kunci menuju kemakmuran. " +
                            "Jalanmu masih panjang, tapi fondasi finansialmu kini kokoh!",
                    player.getName(), formatGold(player.getGold()));
        }
        else if ("marriage".equals(milestoneType) && player.getPartner() != null) {
            return String.format("Selamat, %s dan %s!\n\nCinta kalian telah bersemi di tengah kesuburan tanah Bukit Spakbor. " +
                            "Lonceng pernikahan telah berdentang, menandai awal babak baru kehidupan bersama. " +
                            "Semoga rumah tangga kalian dipenuhi kebahagiaan, panen melimpah, dan cinta yang tak pernah layu. " +
                            "Petualangan hidup berdua baru saja dimulai!",
                    player.getName(), player.getPartner().getName());
        }
        return "Sebuah pencapaian luar biasa telah diraih di Bukit Spakbor!";
    }

    private JPanel createStatsDisplayPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PRIMARY_BG);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        addStatSectionTitle(panel, "Statistik Keuangan");
        addStat(panel, "Total Pendapatan:", formatGold(Player.getTotalIncome()));
        addStat(panel, "Total Pengeluaran:", formatGold(Player.getTotalExpenditure()));

        int daysPlayed = Player.getTotalDaysPlayed();
        int seasonsPassed = (daysPlayed > 0) ? ((daysPlayed - 1) / 10) + 1 : 0;

        if (seasonsPassed > 0) {
            addStat(panel, "Rata-rata Pendapatan per Musim:", formatGold(Player.getTotalIncome() / seasonsPassed));
            addStat(panel, "Rata-rata Pengeluaran per Musim:", formatGold(Player.getTotalExpenditure() / seasonsPassed));
        }
        else {
            addStat(panel, "Rata-rata Pendapatan per Musim:", "N/A");
            addStat(panel, "Rata-rata Pengeluaran per Musim:", "N/A");
        }
        addStat(panel, "Total Hari Bermain:", String.valueOf(daysPlayed) + " hari");

        addSeparator(panel);

        addStatSectionTitle(panel, "Status Hubungan NPC");
        if (npcList != null && !npcList.isEmpty()) {
            for (NPC npc : npcList) {
                if (npc != null) {
                    addStat(panel, "  " + npc.getName() + ":", npc.getRelationshipStatus() +
                            " (" + npc.getHeartPoints() + "/" + npc.getMaxHeartPoints() + " HP)");
                }
            }
        }
        else {
            addStat(panel, "Belum ada interaksi dengan NPC.", "");
        }
        addStat(panel, "Frekuensi Chatting:", String.valueOf(Chatting.getFrequency()) + " kali");
        addStat(panel, "Frekuensi Gifting:", String.valueOf(ProcessGiftingAction.getFrequency()) + " kali");
        addStat(panel, "Frekuensi Visiting NPC:", String.valueOf(Visiting.getFrequency()) + " kali");

        addSeparator(panel);

        addStatSectionTitle(panel, "Aktivitas Pertanian & Memancing");
        addStat(panel, "Total Tanaman Dipanen:", String.valueOf(Player.getTotalCropsHarvested()) + " buah");
        addStat(panel, "Total Ikan Ditangkap:", String.valueOf(Player.getOverallFishCaught()) + " ekor");
        addStat(panel, "  Ikan Common:", String.valueOf(Player.getCommonFishCaught()) + " ekor");
        addStat(panel, "  Ikan Regular:", String.valueOf(Player.getRegularFishCaught()) + " ekor");
        addStat(panel, "  Ikan Legendary:", String.valueOf(Player.getLegendaryFishCaught()) + " ekor");

        return panel;
    }

    private void addStatSectionTitle(JPanel panel, String title) {
        JLabel label = new JLabel(title);
        label.setFont(STATS_HEADER_FONT);
        label.setForeground(TEXT_COLOR.darker());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(15, 0, 8, 0));
        panel.add(label);
    }

    private void addStat(JPanel panel, String key, String value) {
        JPanel statLine = new JPanel(new BorderLayout(5,0));
        statLine.setBackground(PRIMARY_BG);
        JLabel keyLabel = new JLabel(key);
        keyLabel.setFont(STATS_FONT);
        keyLabel.setForeground(TEXT_COLOR);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(STATS_FONT.deriveFont(Font.BOLD));
        valueLabel.setForeground(TEXT_COLOR);

        statLine.add(keyLabel, BorderLayout.WEST);
        statLine.add(valueLabel, BorderLayout.EAST);
        statLine.setAlignmentX(Component.LEFT_ALIGNMENT);
        statLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(statLine);
        panel.add(Box.createRigidArea(new Dimension(0,3)));
    }
    private void addSeparator(JPanel panel) {
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        JSeparator separator = new JSeparator();
        separator.setForeground(TEXT_COLOR.brighter().brighter());
        separator.setBackground(PRIMARY_BG);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(separator);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }


    private String formatGold(int amount) {
        return String.format("%,d g", amount);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2),
                new EmptyBorder(10, 25, 10, 25)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void closeDialog() {
        if (calendar != null) {
            calendar.startTime();
        }
        // Assuming the owner (Main frame) handles regaining focus if needed.
        setVisible(false);
        dispose();
    }
}