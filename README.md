# SILAWET — Aplikasi BPS Kebumen

SILAWET adalah aplikasi Android internal untuk BPS Kabupaten Kebumen yang ditujukan untuk menampilkan data survei, statistik, serta konten infografik dan utilitas pendukung. Aplikasi dibangun menggunakan Kotlin dan Jetpack Compose.

## Fitur Utama
- Tampilan modern berbasis Jetpack Compose Material 3
- Tema gelap/terang (palette terpusat di theme/Color.kt dan Theme.kt)
- Navigasi antar layar: Beranda, Statistik, Infografik
- Navigasi dengan drawer
- Komponen modular untuk kemudahan pengembangan
- Placeholder untuk integrasi backend / sinkronisasi data

## Teknologi
- Kotlin
- Jetpack Compose (Material3)
- AndroidX Navigation Compose
- Gradle / Android Studio

## Struktur Proyek
- app/src/main/java/com/example/bps
  - components/     : Komponen UI yang digunakan dalam aplikasi
  - data/           : Klien Untuk Mengakses API dan Database
  - theme/          : Color.kt, Theme.kt, Typography.kt
  - ui/             : screen Compose (Beranda, Statistik, Maps, Infografik, Lainnya)
  - ui/*            : subpackage layar dan komponen UI
  - utils           : Kode pendukung yang digunakan dalam aplikasi
  - MainActivity.kt : MainActivity sebagai entry point aplikasi
- app/src/main/res  : resource Android (drawable, string, layout jika ada)
- build.gradle      : konfigurasi modul/app

## Persyaratan
- Android Studio (disarankan versi terbaru)
- JDK 11+
- Android SDK sesuai konfigurasi module (periksa build.gradle)

## Cara Menjalankan (pengembangan)
1. Clone repository:
   git clone "https://github.com/evanalif113/Aplikasi-BPS_Kebumen-SILAWET.git"
2. Buka proyek di Android Studio.
3. Sinkronisasi Gradle bila diminta.
4. Jalankan aplikasi pada emulator atau perangkat fisik dari Android Studio (Run 'app').

Catatan: Jika mengalami error terkait tema/status bar, periksa `theme/Theme.kt` dan pastikan `SideEffect` serta `WindowCompat` diterapkan pada Activity yang sesuai.

## Konfigurasi Penting
- Warna aplikasi didefinisikan di `app/src/main/java/com/example/bps/theme/Color.kt`.
- Tema utama di `Theme.kt` memilih skema warna gelap/terang berdasarkan `isSystemInDarkTheme()`.
- TopAppBar dan Scaffold menggunakan `TopAppBarDefaults` dengan `scrollBehavior` untuk mendukung scroll-responsive app bar.

## Pengujian
- Unit tests:
  ./gradlew test
- Instrumented tests (perangkat/emulator):
  ./gradlew connectedAndroidTest

## Kontribusi
- Gunakan branching yang jelas (feature/, fix/, hotfix/).
- Buat PR dengan deskripsi perubahan, alasan, dan langkah verifikasi.
- Ikuti konvensi kode Kotlin dan praktik Jetpack Compose.

## Lisensi
License All Rights Reserved 2026. Hak Cipta dilindungi Undang-undang.

## Kontak
Sertakan nama tim atau email maintainer di sini untuk pertanyaan dan akses repository.

---
Dokumentasi ini ringkas dan ditujukan untuk pengembang yang ingin menjalankan atau mengembangkan SILAWET. 
Tambahkan bagian lebih detail (API, database, CI) bila fitur tersebut diimplementasikan.
