# SpakborHills_K05_Kelompok12

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

2.  **Menjalankan Aplikasi**:
    * **Menggunakan IDE (IntelliJ IDEA atau VS Code) - Direkomendasikan**:
        1.  Unduh folder “SpakborHills_K05_Kelompok12” dari Github
        2.  Buka folder menggunakan VSCode. Klik kanan pada folder resources dan pilih “Add Folder to Java Source Path”
        3.  Buka folder src, klik folder main dan run file Main.java
        4.  Tampilan GUI akan muncul dan game dapat dimainkan
---

## 👨‍👩‍👧‍👦 Anggota Tim (Kelompok 12) - K05

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

* Buklet Permainan dapaat dilihat pada Link berikut "[[BUKLET-TUBES-OOP]](https://www.canva.com/design/DAGo8JHbLqU/rpCoR2rKtFccMKSYQQbd2w/edit?utm_content=DAGo8JHbLqU&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)".

* Spesifikasi lengkap proyek dapat dirujuk pada dokumen "[Spesifikasi Tugas Besar IF2010 Pemrograman Berorientasi Objek STI 2024/2025](https://docs.google.com/document/d/1ru0DxHUwVJ8Az76CZUL1KACXg6uCMC1B7WnSy6bnlHw/edit?usp=sharing)".

---
