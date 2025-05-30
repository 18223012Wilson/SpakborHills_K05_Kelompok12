package ui;

import main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class CreditsPanel extends JPanel implements ActionListener {
    private Main mainFrame;
    private Timer timer;
    private int yOffset;
    private List<String> creditsText;
    private final int SCROLL_SPEED = 1;
    private final int FONT_SIZE = 28;
    private Font creditsFont;
    private Font titleFont;
    private Font roleFont;
    private Font namesFont;

    private int lineHeight;
    private int titleLineHeight;
    private int roleLineHeight;
    private int namesLineHeight;

    private boolean isMouseOverSkip = false;
    private Rectangle skipButtonArea;


    public CreditsPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(1200, 800));

        titleFont = new Font("Serif", Font.BOLD, FONT_SIZE + 12);
        roleFont = new Font("Georgia", Font.BOLD, FONT_SIZE - 2);
        namesFont = new Font("SansSerif", Font.PLAIN, FONT_SIZE - 6);
        creditsFont = new Font("SansSerif", Font.ITALIC, FONT_SIZE - 8);

        FontMetrics fmTitle = getFontMetrics(titleFont);
        titleLineHeight = fmTitle.getHeight() + 25;

        FontMetrics fmRole = getFontMetrics(roleFont);
        roleLineHeight = fmRole.getHeight() + 20;

        FontMetrics fmNames = getFontMetrics(namesFont);
        namesLineHeight = fmNames.getHeight() + 15;

        FontMetrics fmCredits = getFontMetrics(creditsFont);
        lineHeight = fmCredits.getHeight() + 15;


        creditsText = new ArrayList<>();
        creditsText.add("~ BorborSpakbor Hills ~");
        creditsText.add(" ");
        creditsText.add(" ");
        creditsText.add("# Sebuah Epik Digital Dipersembahkan Oleh Para Visioner Bukit Spakbor:");
        creditsText.add(" ");
        creditsText.add("* Wilson - 18223012");
        creditsText.add("  Sang EXP laner tb tb");
        creditsText.add(" ");
        creditsText.add("* Stevan Einer Bonagabe - 18223028");
        creditsText.add("  Mid laner andalan");
        creditsText.add(" ");
        creditsText.add("* Vincentia Belinda Sumartoyo - 18223078");
        creditsText.add("  Manager team tercinta");
        creditsText.add(" ");
        creditsText.add("* M Khalfani Shaquille Indrajaya - 18223104");
        creditsText.add("  Hyper jungler");
        creditsText.add(" ");
        creditsText.add(" ");
        creditsText.add("# Dibimbing Dalam Petualangan Ini Oleh:");
        creditsText.add(" ");
        creditsText.add("Mata Kuliah: IF2010 Pemrograman Berorientasi Objek");
        creditsText.add("Institut Teknologi Bandung (ITB)");
        creditsText.add("Sekolah Teknik Elektro dan Informatika (STEI)");
        creditsText.add("Tahun: 2024/2025");
        creditsText.add(" ");
        creditsText.add(" ");
        creditsText.add("# Apresiasi Tertinggi Kami Berikan Kepada:");
        creditsText.add(" ");
        creditsText.add("Kak Nazhif, Asisten Kami Tercinta <3");
        creditsText.add("Atas segala waktu, asistensi, dan ilmu yang berharga.");
        creditsText.add(" ");
        creditsText.add("Seluruh Penduduk Bukit Spakbor,");
        creditsText.add("Yang telah memberi inspirasi dan warna dalam perjalanan ini.");
        creditsText.add(" ");
        creditsText.add("Dan RED BULL ENERGY DRINK");
        creditsText.add("Yang telah membangkitkan semangat di tiap malam.");
        creditsText.add("Untuk mengerjakan tubez yang tak hingga ini");
        creditsText.add(" ");
        creditsText.add("Dan Tentu Saja, KAU, Sang Petualang!");
        creditsText.add("Terima kasih telah memilih bertualang di dunia kami.");
        creditsText.add(" ");
        creditsText.add(" ");
        creditsText.add("Semoga cangkulmu selalu tajam, panenmu melimpah,");
        creditsText.add("dan hari-harimu di Bukit Spakbor selalu cerah!");
        creditsText.add(" ");
        creditsText.add(" ");
        creditsText.add("~ Sampai Jumpa di Petualangan Berikutnya! ~");


        yOffset = getHeight();
        timer = new Timer(40, this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (skipButtonArea != null && skipButtonArea.contains(e.getPoint())) {
                    stopAndReturnToMenu();
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (skipButtonArea != null) {
                    isMouseOverSkip = skipButtonArea.contains(e.getPoint());
                    repaint();
                }
            }
        });
    }

    public void startCredits() {
        yOffset = getHeight() + 50;
        setVisible(true);
        timer.start();
        requestFocusInWindow();
    }

    public void stopCredits() {
        timer.stop();
        setVisible(false);
    }

    private void stopAndReturnToMenu() {
        stopCredits();
        mainFrame.showMainMenu();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int currentY = yOffset;

        for (String line : creditsText) {
            int currentLineHeight = lineHeight;

            if (line.startsWith("~")) {
                g2d.setFont(titleFont);
                g2d.setColor(new Color(255, 215, 0));
                currentLineHeight = titleLineHeight;
            }
            else if (line.startsWith("#")) {
                g2d.setFont(roleFont);
                g2d.setColor(new Color(173, 216, 230));
                line = line.substring(1).trim();
                currentLineHeight = roleLineHeight;
            }
            else if (line.startsWith("*")) {
                g2d.setFont(namesFont);
                g2d.setColor(new Color(220, 220, 220));
                line = line.substring(1).trim();
                currentLineHeight = namesLineHeight;
            }
            else if (line.startsWith("  ")) {
                g2d.setFont(creditsFont);
                g2d.setColor(new Color(180, 180, 180));
                line = line.trim();
            }
            else {
                g2d.setFont(creditsFont);
                g2d.setColor(Color.WHITE);
            }

            FontMetrics fm = g2d.getFontMetrics();
            int stringWidth = fm.stringWidth(line);
            int x = (getWidth() - stringWidth) / 2;

            if (line.startsWith("  ") || (creditsText.indexOf(line) > 0 && creditsText.get(creditsText.indexOf(line)-1).startsWith("*")) && !line.trim().isEmpty() && !line.startsWith("*")) {
                FontMetrics nameFm = getFontMetrics(namesFont);
                int nameIndentX = (getWidth() - nameFm.stringWidth(creditsText.get(creditsText.indexOf(line)-1).substring(1).trim())) / 2;
                x = nameIndentX + 20;
            }


            if (currentY > getHeight() - 200) {
                float alpha = Math.max(0f, Math.min(1f, 1f - (float)(currentY - (getHeight() - 200)) / 100f));
                Color currentColor = g2d.getColor();
                g2d.setColor(new Color(currentColor.getRed()/255f, currentColor.getGreen()/255f, currentColor.getBlue()/255f, alpha));
            }

            g2d.drawString(line, x, currentY);
            currentY += currentLineHeight;
        }

        String skipText = "[ ts credits pmo ]";
        g2d.setFont(creditsFont.deriveFont(Font.BOLD, FONT_SIZE - 10));
        FontMetrics fmSkip = g2d.getFontMetrics();
        int skipWidth = fmSkip.stringWidth(skipText) + 20;
        int skipHeight = fmSkip.getHeight() + 10;
        int skipX = getWidth() - skipWidth - 20;
        int skipY = getHeight() - skipHeight - 20;

        skipButtonArea = new Rectangle(skipX, skipY, skipWidth, skipHeight);

        if (isMouseOverSkip) {
            g2d.setColor(new Color(110, 110, 110, 180));
        } else {
            g2d.setColor(new Color(60, 60, 60, 180));
        }
        g2d.fillRoundRect(skipX, skipY, skipWidth, skipHeight, 10, 10);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawRoundRect(skipX, skipY, skipWidth, skipHeight, 10, 10);
        g2d.setColor(isMouseOverSkip ? Color.WHITE : Color.LIGHT_GRAY);
        g2d.drawString(skipText, skipX + 10, skipY + fmSkip.getAscent() + 5);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        yOffset -= SCROLL_SPEED;

        int totalTextHeight = 0;
        for(String line : creditsText) {
            if (line.startsWith("~")) totalTextHeight += titleLineHeight;
            else if (line.startsWith("#")) totalTextHeight += roleLineHeight;
            else if (line.startsWith("*")) totalTextHeight += namesLineHeight;
            else totalTextHeight += lineHeight;
        }

        if (yOffset + totalTextHeight < -50) {
            stopAndReturnToMenu();
        }
        repaint();
    }
}