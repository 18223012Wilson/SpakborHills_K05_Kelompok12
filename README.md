# TubesOOPStardewValley

# Judul Proyek: Spakbor Hills

Game simulasi pertanian "Spakbor Hills", di mana pemain mengembangkan pertanian dan kehidupan sosial di sebuah desa.

## 🎯 Deskripsi Proyek

"Spakbor Hills" adalah sebuah game simulasi pertanian dengan elemen RPG ringan. Pemain diajak untuk mengikuti perjalanan 
karakter utama (Dr. Asep Spakbor) yang memutuskan untuk beralih profesi menjadi seorang petani di Desa Spakbor Hills.

**Tujuan Permainan:**
Tujuan utama pemain adalah mengembangkan lahan pertanian menjadi produktif, mengumpulkan kekayaan (gold) hingga mencapai 
target tertentu, dan membangun hubungan sosial dengan para penduduk desa (NPC), yang berpotensi berujung pada pernikahan. 
Game ini dirancang untuk alur permainan yang berkelanjutan (infinite gameplay).

**Genre Game:** Simulasi Pertanian (Farming Simulation) dengan elemen RPG.

## 🧠 Fitur-Fitur Utama

Berikut adalah fitur-fitur utama yang telah diimplementasikan dalam game "Spakbor Hills":

* **Siklus Pertanian**:
    * Mencangkul tanah (Tilling).
    * Menanam berbagai jenis bibit sesuai musim (Planting).
    * Menyiram tanaman secara manual atau mengandalkan hujan (Watering).
    * Memanen hasil tanaman yang telah matang (Harvesting).
* **Manajemen Waktu, Musim, dan Cuaca**:
    * Pergantian empat musim (Spring, Summer, Fall, Winter) yang memengaruhi jenis tanaman,     
      ketersediaan ikan, dan aspek lainnya.
    * Kondisi cuaca (Sunny, Rainy) yang berdampak pada aktivitas pertanian.
* **Eksplorasi Dunia dan Interaksi**:
    * Navigasi pemain pada peta kebun (Farm Map).
    * Mengunjungi berbagai tempat di luar kebun melalui World Map (Forest River, Mountain Lake, 
      Ocean, Toko, Rumah NPC).
* **Interaksi Sosial dengan NPC**:
    * Berinteraksi dengan berbagai karakter NPC unik yang ada di desa.
    * Memberikan hadiah kepada NPC (Gifting) yang akan memengaruhi tingkat hubungan 
    * Membangun hubungan hingga ke jenjang pertunangan (Proposing) dan pernikahan (Marrying).
* **Aktivitas Pemain Lainnya**:
    * Memancing berbagai jenis ikan (Common, Regular, Legendary) di berbagai lokasi dan kondisi.
    *  Memasak hasil pertanian dan pancingan menjadi berbagai hidangan makanan dengan resep 
       lezat.
* **Sistem Ekonomi dan Inventaris**:
    * Menjual item melalui *shipping bin* untuk mendapatkan gold.
    * Mengelola item yang dimiliki dalam inventaris pemain.
* **Antarmuka Grafis Pengguna (GUI)**:
    * Visualisasi game dan interaksi pemain melalui GUI yang dibangun menggunakan Java Swing.

## ⚙️ Cara Menjalankan Game

**Bahasa Pemrograman & Tools:**
* Bahasa Pemrograman: Java (disarankan JDK 11 atau lebih baru).
* GUI Framework: Java Swing.

**Langkah-langkah Instalasi dan Menjalankan:**

1.  **Prasyarat**: Pastikan Java dengan versi terbaru (23) (atau yang sesuai dengan pengembangan Anda) telah terinstal di sistem Anda.

3.  **Kompilasi**:
    * **Menggunakan IDE (IntelliJ IDEA atau VS Code) - Direkomendasikan**:
        1.  Buka proyek (folder utama, misal `...`) menggunakan IDE Anda.
        2.  Pastikan konfigurasi JDK untuk proyek telah benar. IDE akan menangani kompilasi.
    * **Manual (menggunakan `javac` dari terminal/command prompt)**:
        Navigasi ke direktori `src` dalam proyek. Perintah mungkin memerlukan penyesuaian.
        ```bash
        # Contoh untuk membuat direktori output 'bin' satu level di atas 'src'
        mkdir ../bin
        javac -d ../bin -sourcepath . $(find . -name "*.java")
        # Perintah 'find' untuk Linux/macOS. Alternatif mungkin diperlukan untuk Windows.
        ```
4.  **Menjalankan Aplikasi**:
    * **Dari IDE**:
        1.  Temukan file `Main.java` dalam paket `main`.
        2.  Klik kanan pada file tersebut dan pilih opsi "Run 'Main.main()'".
    * **Dari Command Line (setelah kompilasi ke direktori `bin`)**:
        Navigasi ke direktori root proyek Anda.

---

## 👨‍👩‍👧‍👦 Anggota Tim (Kelompok 12) - K02

* **Stevan Einer Bonagabe** - 18223028
* **Wilson** - 18223012
* **Vincentia Belinda Sumartoyo** - 18223078
* **M Khalfani Shaquille Indrajaya** - 18223104


## 📝 Catatan Tambahan

* Proyek ini dikembangkan sebagai pemenuhan Tugas Besar IF2010 Pemrograman Berorientasi Objek.

* **Design Pattern yang Diimplementasikan**:
    1.  **Factory Pattern**: Digunakan dalam `FishDatabase` dan `Crop`.
    2.  **Command Pattern**: Struktur kelas `model.actions.Action` dan turunannya mengkapsulasi aksi pemain. `ActionPanel` bertindak sebagai *invoker*.
    3.  **Observer Pattern (via Event Listener)**: Pola ini diterapkan dalam penanganan input  menggunakan mekanisme *event listener* (`KeyListener`, `MouseListener`) untuk "mendengarkan" dan merespons aksi pengguna 

* Spesifikasi lengkap proyek dapat dirujuk pada dokumen "[Spesifikasi Tugas Besar IF2010 Pemrograman Berorientasi Objek STI 2024/2025](https://docs.google.com/document/d/1ru0DxHUwVJ8Az76CZUL1KACXg6uCMC1B7WnSy6bnlHw/edit?usp=sharing)".

---
