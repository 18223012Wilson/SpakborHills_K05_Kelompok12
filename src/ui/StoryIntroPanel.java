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

public class StoryIntroPanel extends JPanel implements ActionListener {
    private Main mainFrame;
    private String playerName;
    private String playerGender;
    private Timer timer;
    private int yOffset;
    private List<String> storyText;
    private final int SCROLL_SPEED = 1;
    private final int FONT_SIZE = 26;
    private Font storyFont;
    private Font titleFont;
    private int lineHeight;
    private int titleLineHeight;

    private boolean isMouseOverSkip = false;
    private Rectangle skipButtonArea;

    public StoryIntroPanel(Main mainFrame, String playerName, String playerGender) {
        this.mainFrame = mainFrame;
        this.playerName = playerName;
        this.playerGender = playerGender;

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(1200, 700));

        titleFont = new Font("Serif", Font.BOLD, FONT_SIZE + 12);
        storyFont = new Font("SansSerif", Font.PLAIN, FONT_SIZE - 2);
        FontMetrics fm = getFontMetrics(storyFont);
        lineHeight = fm.getHeight() + 18;
        FontMetrics fmTitle = getFontMetrics(titleFont);
        titleLineHeight = fmTitle.getHeight() + 22;

        storyText = new ArrayList<>();
        populateStoryText();

        yOffset = getHeight();
        timer = new Timer(45, this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (skipButtonArea != null && skipButtonArea.contains(e.getPoint())) {
                    finishIntroAndProceed();
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

    private void populateStoryText() {
        storyText.clear();
        storyText.add("~ Legenda Sang Penjaga Spakbor ~");
        storyText.add(" ");
        storyText.add(" ");
        storyText.add("Alkisah, di negeri antah berantah yang kini terlupakan,");
        storyText.add("terdapat sebuah bukit sakral, dikenal sebagai Bukit Spakbor.");
        storyText.add("Konon, bukit ini adalah jantung dari keseimbangan alam,");
        storyText.add("dijaga oleh entitas gaib dan dilimpahi keajaiban.");
        storyText.add(" ");
        storyText.add("Namun, seiring waktu, kekuatan jahat bernama 'Korporasi Tubez'");
        storyText.add("mulai menggerogoti vitalitas negeri.");
        storyText.add("Mereka menyebarkan kabut kelabu yang membuat semua serba instan,");
        storyText.add("memudarkan warna-warni kehidupan, dan melupakan kearifan alam.");
        storyText.add("Bukit Spakbor pun meredup, keajaibannya terancam sirna.");
        storyText.add(" ");
        storyText.add("Para tetua desa di kaki bukit berbisik tentang sebuah ramalan kuno:");
        storyText.add("\"Akan datang seorang jiwa pengelana, dengan hati yang murni,\"");
        storyText.add("\"bernama " + playerName + ", yang ditakdirkan memulihkan Spakbor.\"");
        storyText.add(" ");
        storyText.add("Dan kau, " + playerName + ", tanpa sadar adalah jiwa itu.");
        storyText.add("Entah bagaimana, kau merasa tarikan kuat menuju sebuah desa terpencil.");
        storyText.add("Sebuah peta lusuh peninggalan leluhurmu yang entah dari mana asalnya,");
        storyText.add("menuntun langkahmu ke gerbang desa");
        storyText.add("yang tampak sepi namun penuh harap.");
        storyText.add(" ");
        storyText.add("Di sana, seorang Kakek Bijak menyambutmu dengan senyum penuh arti.");
        storyText.add("\"Selamat datang, " + playerName + ". Bukit Spakbor telah menantimu.\"");
        storyText.add("\"Tanah ini merindukan sentuhan yang tulus, menghargai proses.\"");
        storyText.add(" ");
        storyText.add("\"Korporasi Tubez telah meracuni tanah dan hati banyak orang.\"");
        storyText.add("\"Dengan bercocok tanam, menjalin persahabatan, dan menemukan");
        storyText.add("keajaiban sederhana, kau bisa melawan kabut kelabu itu.\"");
        storyText.add(" ");
        storyText.add("Kakek menyerahkan cangkul berkarat dan beberapa benih misterius.");
        storyText.add("\"Ini bekal awalmu. Sisanya......\"");
        storyText.add(" ");
        storyText.add(" ");
        storyText.add("Lalu Sang Kakek berkata ");
        storyText.add(" ");
        storyText.add("Sybau");
        storyText.add(" ");
        storyText.add(playerName + ", takdirmu terbentang di hadapan.");
        storyText.add("Pulihkanlah Bukit Spakbor, kembalikan senyum pada negeri ini.");
        storyText.add("Perjalananmu dimulai... sekarang!");
        storyText.add(" ");
        storyText.add(" ");
    }


    public void startIntro() {
        yOffset = getHeight() + 60;
        setVisible(true);
        timer.start();
        requestFocusInWindow();
    }

    public void stopIntro() {
        timer.stop();
        setVisible(false);
    }

    private void finishIntroAndProceed() {
        stopIntro();
        mainFrame.proceedToGameActual(playerName, playerGender);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int currentY = yOffset;

        for (String line : storyText) {
            if (line.startsWith("~")) {
                g2d.setFont(titleFont);
                g2d.setColor(new Color(180, 220, 100));
            } else {
                g2d.setFont(storyFont);
                g2d.setColor(new Color(230, 240, 230));
            }

            FontMetrics fm = g2d.getFontMetrics();
            int stringWidth = fm.stringWidth(line);
            int x = (getWidth() - stringWidth) / 2;

            if (currentY > getHeight() - 250) {
                float alpha = Math.max(0f, Math.min(1f, 1f - (float)(currentY - (getHeight() - 250)) / 120f));
                if (line.startsWith("~")) {
                    g2d.setColor(new Color(180, 220, 100, (int)(alpha*255)));
                } else {
                    g2d.setColor(new Color(230, 240, 230, (int)(alpha*255)));
                }
            }

            g2d.drawString(line, x, currentY);
            currentY += (line.startsWith("~") ? titleLineHeight : lineHeight);
        }

        String skipText = "[ Sybau ]";
        g2d.setFont(storyFont.deriveFont(Font.ITALIC, FONT_SIZE - 10));
        FontMetrics fmSkip = g2d.getFontMetrics();
        int skipWidth = fmSkip.stringWidth(skipText) + 20;
        int skipHeight = fmSkip.getHeight() + 10;
        int skipX = getWidth() - skipWidth - 20;
        int skipY = getHeight() - skipHeight - 20;

        skipButtonArea = new Rectangle(skipX, skipY, skipWidth, skipHeight);

        if (isMouseOverSkip) {
            g2d.setColor(new Color(100, 130, 100, 180));
        } else {
            g2d.setColor(new Color(60, 90, 60, 180));
        }
        g2d.fillRoundRect(skipX, skipY, skipWidth, skipHeight, 10, 10);
        g2d.setColor(new Color(180, 220, 180));
        g2d.drawRoundRect(skipX, skipY, skipWidth, skipHeight, 10, 10);
        g2d.setColor(isMouseOverSkip ? Color.WHITE : new Color(210, 230, 210));
        g2d.drawString(skipText, skipX + 10, skipY + fmSkip.getAscent() + 5);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        yOffset -= SCROLL_SPEED;
        int totalTextHeight = 0;
        for (String line : storyText) {
            totalTextHeight += (line.startsWith("~") ? titleLineHeight : lineHeight);
        }

        if (yOffset + totalTextHeight < -100) {
            finishIntroAndProceed();
        }
        repaint();
    }

    public void resetAndPrepare(String playerName, String playerGender) {
        this.playerName = playerName;
        this.playerGender = playerGender;
        populateStoryText();
        yOffset = getHeight();
    }
}