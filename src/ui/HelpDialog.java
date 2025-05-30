package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpDialog extends JDialog {
    private JTabbedPane tabbedPane;
    private final Color bgColor = new Color(245, 235, 220);
    private final Color textBgColor = new Color(253, 250, 240);
    private final Color titleColor = new Color(70, 40, 20);
    private final Color textColor = new Color(50, 30, 10);
    private final Color accentColor = new Color(100, 70, 50);

    public HelpDialog(Frame owner) {
        super(owner, "Panduan Bermain - Spakbor Hills", true);
        initComponents();
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(900, 700));
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(bgColor);

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        tabbedPane = createTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(bgColor);
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("🌾 Selamat Datang di Spakbor Hills! 🌾", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 32));
        titleLabel.setForeground(titleColor);

        JLabel subtitleLabel = new JLabel("Panduan lengkap untuk memulai petualangan farming terbaikmu", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(120, 80, 40));
        subtitleLabel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setBackground(bgColor);
        titleContainer.add(titleLabel, BorderLayout.CENTER);
        titleContainer.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(titleContainer, BorderLayout.CENTER);
        return headerPanel;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane pane = new JTabbedPane();
        pane.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        pane.setBackground(bgColor);
        pane.setForeground(titleColor);

        pane.addTab("🎮 Kontrol Dasar", createBasicsPanel());
        pane.addTab("🌱 Bertani & Memasak", createFarmingPanel());
        pane.addTab("🎣 Aktivitas Lainnya", createActivitiesPanel());
        pane.addTab("💖 Hubungan & Sosial", createSocialPanel());
        pane.addTab("📊 Statistik & Tips", createStatsPanel());

        return pane;
    }

    private Font getEmojiSupportedFont() {
        String[] fontNames = {"Segoe UI Emoji", "Apple Color Emoji", "Noto Color Emoji", "SansSerif"};

        for (String fontName : fontNames) {
            Font font = new Font(fontName, Font.PLAIN, 15);
            if (!font.getFamily().equals("Dialog")) {
                return font;
            }
        }
        return new Font("SansSerif", Font.PLAIN, 15);
    }

    private JScrollPane createScrollableTextArea(String content) {
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/plain");
        textPane.setText(content);
        textPane.setEditable(false);
        textPane.setFont(getEmojiSupportedFont());
        textPane.setBackground(textBgColor);
        textPane.setForeground(textColor);
        textPane.setMargin(new Insets(20, 20, 20, 20));
        textPane.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 160, 140), 2),
                new EmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private JPanel createBasicsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String content = "🎯 TUJUAN UTAMA\n" +
                "Mulai kehidupan barumu sebagai petani di Spakbor Hills! Kembangkan lahan pertanianmu, " +
                "berinteraksi dengan penduduk desa yang ramah, dan jelajahi berbagai wilayah menarik. " +
                "Capai target seperti mengumpulkan 17.209 emas atau menemukan pasangan hidup dan menikah!\n\n" +

                "🎮 KONTROL PERMAINAN\n\n" +
                "🚶 Pergerakan:\n" +
                "• Gunakan tombol WASD untuk menggerakkan karaktermu\n" +
                "• Bergeraklah dengan lancar di seluruh peta dan wilayah\n\n" +

                "⚡ Interaksi dengan Keyboard:\n" +
                "• Tekan 'E' saat berada di dekat objek yang bisa diinteraksi\n" +
                "• Gunakan untuk membuka pintu rumah, shipping bin, atau berbicara dengan NPC\n" +
                "• Tekan 'E' di tepi peta untuk opsi perjalanan ke wilayah lain\n\n" +

                "🖱️ Interaksi dengan Mouse:\n" +
                "• Klik kiri pada tile peta untuk melihat aksi yang tersedia (till, plant, water, recover, harvest)\n" +

                "📦 Menu dan Interface:\n" +
                "• Tekan 'I' untuk membuka/menutup inventory\n" +
                "• Tekan '1' untuk melihat statistik dan progress permainan\n" +
                "• Tekan 'Esc' untuk menutup dialog atau membatalkan aksi tile\n\n" +

                "⏰ WAKTU DAN ENERGI\n\n" +
                "🕐 Sistem Waktu:\n" +
                "• Waktu game berjalan terus-menerus (5 menit game per 100ms real time)\n" +
                "• Siang hari: 06:00-17:59 | Malam hari: 18:00-05:59\n" +
                "• Perhatikan siklus siang-malam untuk aktivitas optimal\n\n" +

                "💪 Sistem Energi:\n" +
                "• Energi maksimum: 100 poin\n" +
                "• Masih bisa beraktivitas hingga -20 energi (cadangan)\n" +
                "• Pingsan jika energi mencapai -20 atau terjaga hingga jam 2 pagi\n" +
                "• Tidur untuk memulihkan energi dan melanjutkan ke hari berikutnya";

        JScrollPane scrollPane = createScrollableTextArea(content);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFarmingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String content = "🌱 SENI BERTANI DI SPAKBOR HILLS\n\n" +
                "🚜 Langkah-langkah Bertani:\n\n" +
                "1️⃣ Membajak Tanah:\n" +
                "• Gunakan Cangkul pada tile 'Land' untuk mengubahnya menjadi 'Soil'\n" +
                "• Tanah yang sudah dibajak siap untuk ditanami\n" +
                "• Pastikan area cukup luas untuk hasil maksimal\n\n" +

                "2️⃣ Menanam Benih:\n" +
                "• Pilih tile 'Soil' dan pilih benih dari inventory\n" +
                "• Tile akan berubah menjadi 'Planted Land'\n" +
                "• Setiap musim memiliki tanaman yang berbeda\n\n" +

                "3️⃣ Menyiram Tanaman:\n" +
                "• Gunakan Watering Can pada 'Planted Land' setiap hari\n" +
                "• Tanaman tidak akan tumbuh jika tidak disiram!\n" +
                "• Kecuali saat hujan - alam akan menyiram untukmu 🌧️\n\n" +

                "4️⃣ Memanen Hasil:\n" +
                "• Panen tanaman yang sudah matang untuk mendapatkan hasil\n" +
                "• Tanah akan kembali menjadi 'Soil' (t) setelah panen\n" +
                "• Bisa langsung ditanami lagi untuk siklus berikutnya\n\n" +

                "🍳 SENI MEMASAK\n\n" +
                "👨‍🍳 Cara Memasak:\n" +
                "• Interaksi dengan rumahmu atau kompor untuk membuka menu masak\n" +
                "• Butuh resep (beberapa sudah diketahui, lainnya harus dibeli/dibuka)\n" +
                "• Siapkan bahan-bahan dari inventory\n" +
                "• Butuh bahan bakar: Coal atau Firewood\n\n" +

                "⏱️ Detail Memasak:\n" +
                "• Memasak membutuhkan 1 jam game\n" +
                "• Mengonsumsi 10 energi per masakan\n" +
                "• Hasil masakan bisa dimakan untuk energi atau dijual\n\n" +

                "🌦️ MUSIM DAN CUACA\n\n" +
                "📅 Sistem Musim:\n" +
                "• Setiap musim berlangsung 10 hari\n" +
                "• Spring (Musim Semi) - Summer (Musim Panas) - Fall (Musim Gugur) - Winter (Musim Dingin)\n" +
                "• Setiap musim memiliki tanaman dan ikan yang berbeda\n\n" +

                "☀️🌧️ Pengaruh Cuaca:\n" +
                "• Cerah: Aktivitas normal, perlu menyiram manual\n" +
                "• Hujan: Tanaman otomatis tersiram, hemat energi!\n" +
                "• Cek TV di rumah untuk ramalan cuaca harian";

        JScrollPane scrollPane = createScrollableTextArea(content);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActivitiesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String content = "🎣 MEMANCING - KEGIATAN SANTAI YANG MENGUNTUNGKAN\n\n" +
                "🎯 Cara Memancing:\n" +
                "• Berdiri di dekat badan air (Kolam farm, Sungai Hutan, Danau Gunung, atau Laut)\n" +
                "• Mulai aksi memancing untuk mini-game seru\n" +
                "• Tebak angka dalam rentang dan percobaan yang diberikan\n\n" +

                "🐟 Lokasi Memancing:\n" +
                "• Farm Pond: Ikan dasar, mudah untuk pemula\n" +
                "• Forest River: Ikan yang beragam\n" +
                "• Mountain Lake: Ikan langka dengan harga tinggi\n" +
                "• Ocean: Ikan laut LEGENDARY dan bernilai\n\n" +

                "💰 EKONOMI DAN PERDAGANGAN\n\n" +
                "📦 Menjual Barang:\n" +
                "• Masukkan item ke Shipping Bin di farm\n" +
                "• Emas dari penjualan otomatis terkumpul keesokan pagi\n" +
                "• Jual hasil panen, ikan, masakan, dan barang lainnya\n" +

                "🛒 Berbelanja di Toko Emily:\n" +
                "• Beli benih untuk musim yang berbeda\n" +
                "• Tools dan upgrade peralatan\n" +
                "• Makanan untuk energi darurat\n" +
                "• Resep masakan baru\n" +
                "• Barang-barang berguna lainnya\n\n" +

                "🏠 MANAJEMEN RUMAH\n\n" +
                "🛏️ Tidur dan Istirahat:\n" +
                "• Interaksi dengan tempat tidur untuk tidur\n" +
                "• Tidur normal: Energi penuh kembali\n" +
                "• Tidur setelah pingsan: Hanya setengah energi\n" +
                "• Game berlanjut ke hari berikutnya jam 06:00\n\n" +

                "📺 Informasi Harian:\n" +
                "• Cek TV setiap pagi untuk ramalan cuaca\n" +
                "• Rencanakan aktivitas berdasarkan cuaca\n" +
                "• Hujan = tidak perlu menyiram tanaman!\n\n" +

                "🗺️ EKSPLORASI WILAYAH\n\n" +
                "🌍 Area yang Bisa Dijelajahi:\n" +
                "• Farm: Rumahmu dan lahan pertanian\n" +
                "• Village: Pusat aktivitas dan rumah NPC\n" +
                "• Forest: Sumber daya alam dan sungai\n" +
                "• Mountain: Danau memancing dan pemandangan\n" +
                "• Beach: Laut untuk memancing ikan langka\n\n" +

                "💡 Tips Eksplorasi:\n" +
                "• Setiap area memiliki sumber daya unik\n" +
                "• Waktu perjalanan antar area memakan energi\n" +
                "• Jelajahi semua area untuk pengalaman lengkap";

        JScrollPane scrollPane = createScrollableTextArea(content);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSocialPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String content = "💖 MEMBANGUN HUBUNGAN DENGAN PENDUDUK DESA\n\n" +
                "👥 Berinteraksi dengan NPC:\n" +
                "• Kunjungi NPC di rumah mereka (NPCMap) atau Emily di Tokonya\n" +
                "• Ngobrol dengan mereka untuk membangun kedekatan\n" +
                "• Setiap percakapan membantu meningkatkan relationship\n" +
                "• Beberapa NPC memiliki jadwal dan lokasi khusus\n\n" +

                "🎁 Sistem Hadiah:\n" +
                "• Berikan hadiah dari inventory untuk meningkatkan heart points\n" +
                "• Setiap NPC punya preferensi berbeda:\n" +
                "  - Loved Items: +25 heart points, reaksi sangat senang\n" +
                "  - Liked Items: +20 heart points, reaksi positif\n" +
                "  - Neutral Items: +0heart points, reaksi biasa\n" +
                "  - Hated Items: -25 heart points, reaksi sangat negatif\n\n" +

                "💝 Tips Memberikan Hadiah:\n" +
                "• Perhatikan reaksi NPC untuk mengetahui preferensi mereka\n" +

                "💒 SISTEM PERNIKAHAN\n\n" +
                "💕 Jalan Menuju Pernikahan:\n" +
                "• Bangun hubungan dengan kandidat pernikahan hingga level tinggi\n" +
                "• Berikan hadiah secara konsisten\n" +
                "• Ngobrol setiap hari jika memungkinkan\n" +
                "• Perhatikan dialog khusus yang menandakan hubungan semakin dekat\n\n" +

                "🏆 Manfaat Hubungan Baik:\n" +
                "• Akses ke dialog dan event khusus\n" +
                "• Beberapa NPC mungkin memberikan hadiah balik\n" +
                "• Informasi tips dan trik bermain\n" +
                "• Pencapaian goal pernikahan (salah satu target utama game)\n\n" +


                "💬 Dialog dan Conversation:\n" +
                "• Dialog berubah sesuai level hubungan\n" +
                "• Heart points menentukan kedalaman percakapan\n" +
                "• Beberapa NPC membuka cerita personal di level tinggi\n" +
                "• Respon mereka berbeda tergantung cuaca dan musim\n\n" +

                "🌟 EMILY SI PEMILIK TOKO\n\n" +
                "🛍️ Emily's Store:\n" +
                "• Satu-satunya toko di Spakbor Hills\n" +
                "• Menjual semua kebutuhan farming dan daily life\n" +
                "• Stok berubah sesuai musim\n" +
                "• Bisa diajak ngobrol seperti NPC lainnya\n\n" +

                "💡 Tips Bersosialisasi:\n" +
                "• Jangan lupa menyapa semua NPC secara rutin\n" +
                "• Catat preferensi hadiah setiap NPC\n" +
                "• Manfaatkan hari hujan untuk fokus bersosialisasi\n" +
                "• Hubungan baik membuat game lebih menyenangkan dan bermakna!";

        JScrollPane scrollPane = createScrollableTextArea(content);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String content = "📊 STATISTIK DAN PROGRESS TRACKING\n\n" +
                "📈 Cara Mengakses Statistik:\n" +
                "• Tekan tombol '1' kapan saja saat bermain untuk membuka menu statistik\n" +
                "• Menu statistik menampilkan progress keseluruhan permainanmu\n" +
                "• Pantau pencapaian dan target yang sudah dicapai\n" +
                "• Lihat performa di berbagai aspek permainan\n\n" +

                "🎯 Target Utama yang Bisa Dicapai:\n" +
                "• 💰 Mengumpulkan 17.209 emas (target keuangan utama)\n" +
                "• 💒 Menemukan pasangan hidup dan menikah\n" +
                "• 🌾 Menjadi petani sukses dengan lahan produktif\n" +
                "• 🏆 Menyelesaikan berbagai achievement dalam game\n\n" +

                "💡 TIPS PRO UNTUK SUKSES\n\n" +
                "🌅 Manajemen Waktu:\n" +
                "• Selalu cek TV untuk ramalan cuaca setiap pagi\n" +
                "• Rencanakan aktivitas harian berdasarkan cuaca\n" +
                "• Hari hujan = fokus bersosialisasi atau cooking\n" +
                "• Hari cerah = maksimalkan farming dan fishing\n\n" +

                "⚡ Manajemen Energi:\n" +
                "• Jangan biarkan energi turun terlalu rendah\n" +
                "• Makan makanan untuk restore energi saat darurat\n" +
                "• Tidur sebelum energi mencapai -20 untuk menghindari pingsan\n" +
                "• Pingsan akan mengurangi efisiensi hari berikutnya\n\n" +

                "🗺️ Eksplorasi Strategis:\n" +
                "• Jelajahi semua area untuk menemukan semua sumber daya\n" +
                "• Setiap area memiliki ikan dan resource yang berbeda\n" +
                "• Kenali semua NPC di berbagai lokasi\n" +
                "• Buka semua area sejak awal untuk akses maksimal\n\n" +

                "💰 Strategi Ekonomi:\n" +
                "• Diversifikasi sumber income: farming, fishing, cooking\n" +
                "• Tanaman dengan profit margin tinggi untuk musim tertentu\n" +
                "• Jual hasil masakan untuk harga lebih tinggi daripada bahan mentah\n" +

                "🌱 Farming Pro Tips:\n" +
                "• Rencanakan farming berdasarkan musim\n" +
                "• Siapkan benih untuk musim berikutnya sejak dini\n" +
                "• Maksimalkan penggunaan lahan dengan crop rotation\n" +
                "• Manfaatkan hari hujan untuk tidak perlu menyiram\n\n" +

                "🎣 Fishing Excellence:\n" +
                "• Berbeda lokasi = berbeda jenis ikan\n" +
                "• Beberapa ikan hanya muncul di musim tertentu\n" +
                "• Practice mini-game fishing untuk konsistensi\n" +
                "• Ikan langka bisa jadi sumber income utama\n\n" +

                "💖 Social Mastery:\n" +
                "• Konsistensi lebih penting daripada hadiah mahal\n" +
                "• Catat preferensi setiap NPC untuk hadiah optimal\n" +
                "• Manfaatkan waktu indoor saat cuaca buruk\n" +
                "• Hubungan baik membuka content dan story tambahan\n\n" +

                "🔥 RAHASIA KESUKSESAN:\n" +
                "• Balance antara semua aktivitas, jangan fokus satu hal saja\n" +
                "• Patience is key - progress butuh waktu tapi pasti\n" +
                "• Enjoy the journey, bukan cuma fokus ke target akhir\n" +
                "• Setiap hari adalah opportunity baru untuk maju\n\n" +

                "Selamat menikmati petualangan farming terbaikmu di Spakbor Hills! 🌾✨";

        JScrollPane scrollPane = createScrollableTextArea(content);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(bgColor);
        footerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JLabel footerLabel = new JLabel("Semoga berhasil menjadi petani terbaik di Spakbor Hills! 🌟", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 14));
        footerLabel.setForeground(new Color(120, 80, 40));

        JButton closeButton = new JButton("Tutup Panduan");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        closeButton.setBackground(accentColor);
        closeButton.setForeground(Color.WHITE);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setFocusPainted(false);
        closeButton.setBorder(new CompoundBorder(
                new LineBorder(new Color(70, 50, 30), 2),
                new EmptyBorder(10, 25, 10, 25)
        ));
        closeButton.addActionListener(e -> dispose());

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.setBackground(bgColor);
        buttonContainer.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonContainer.add(closeButton);

        footerPanel.add(footerLabel, BorderLayout.NORTH);
        footerPanel.add(buttonContainer, BorderLayout.CENTER);

        return footerPanel;
    }
}