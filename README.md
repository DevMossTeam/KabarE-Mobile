# Proyek Kabare

**Nama Proyek:** `com.devmoss.kabare`

Proyek ini merupakan aplikasi yang dibangun menggunakan Kotlin dan MySQL (phpMyAdmin) sebagai database. Aplikasi ini memiliki berbagai fitur yang terbagi ke dalam beberapa halaman yang terorganisir dengan baik.

## Struktur Direktori Proyek Utama

```
com/
└── devmoss/
    └── kabare/
        ├── ui/
        │   ├── intro/
        │   │   ├── IntroFragment.kt
        │   │   └── IntroViewModel.kt
        │   ├── welcome/
        │   │   ├── WelcomePage1Fragment.kt
        │   │   ├── WelcomePage2Fragment.kt
        │   │   ├── WelcomePage3Fragment.kt
        │   │   └── WelcomeViewModel.kt
        │   ├── auth/
        │   │   ├── SignInFragment.kt
        │   │   ├── SignUpInputFragment.kt
        │   │   ├── LupaPasswordGantiFragment.kt
        │   │   ├── popups/
        │   │   │   ├── LupaPasswordKonfirmasiEmailDialog.kt
        │   │   │   ├── LupaPasswordVerifEmailDialog.kt
        │   │   │   ├── SignUpKonfirmasiEmailDialog.kt
        │   │   │   ├── SignUpVerifikasiEmailDialog.kt
        │   │   │   ├── SignUpBerhasilDialog.kt
        │   │   │   └── LupaPasswordBerhasilDialog.kt
        │   │   └── viewmodels/
        │   │       ├── SignInViewModel.kt
        │   │       ├── SignUpInputViewModel.kt
        │   │       └── LupaPasswordGantiViewModel.kt
        │   ├── home/
        │   │   ├── HomeFragment.kt
        │   │   ├── ArtikelFragment.kt
        │   │   ├── SearchPageFragment.kt
        │   │   ├── ManajemenArtikelFragment.kt
        │   │   ├── penulisan_artikel/
        │   │   │   ├── PenulisanArtikelFragment.kt
        │   │   │   └── KonfirmasiPublikasiDialog.kt
        │   │   └── viewmodels/
        │   │       ├── HomeViewModel.kt
        │   │       ├── ArtikelViewModel.kt
        │   │       ├── SearchPageViewModel.kt
        │   │       ├── ManajemenArtikelViewModel.kt
        │   │       └── PenulisanArtikelViewModel.kt
        │   ├── notifications/
        │   │   ├── NotifikasiFragment.kt
        │   │   └── viewmodels/
        │   │       └── NotifikasiViewModel.kt
        │   ├── profile/
        │   │   ├── ProfileFragment.kt
        │   │   ├── PengaturanAkunFragment.kt
        │   │   ├── AkunAndaFragment.kt
        │   │   ├── PrivasiFragment.kt
        │   │   ├── TentangFragment.kt
        │   │   ├── KeamananFragment.kt
        │   │   ├── popups/
        │   │   │   ├── ChangePasswordDialog.kt
        │   │   │   └── ChangePasswordBerhasilDialog.kt
        │   │   └── viewmodels/
        │   │       ├── ProfileViewModel.kt
        │   │       ├── PengaturanAkunViewModel.kt
        │   │       ├── AkunAndaViewModel.kt
        │   │       ├── PrivasiViewModel.kt
        │   │       ├── TentangViewModel.kt
        │   │       └── KeamananViewModel.kt
        ├── data/
        │   ├── model/
        │   │   ├── User.kt
        │   │   └── Article.kt
        │   └── repository/
        │       ├── AuthRepository.kt
        │       ├── ArticleRepository.kt
        │       └── ProfileRepository.kt
        ├── network/
        │   ├── ApiClient.kt
        │   └── ApiService.kt
        └── utils/
            ├── Constants.kt
            └── SessionManager.kt
```

### Penjelasan Struktur Proyek Utama

- **ui/**: Folder ini menyimpan semua komponen UI aplikasi, yang diorganisir berdasarkan fungsinya:
  - **intro/**: Halaman pengantar aplikasi.
  - **welcome/**: Tiga halaman sambutan pengguna, masing-masing diwakili oleh `WelcomePage1Fragment`, `WelcomePage2Fragment`, dan `WelcomePage3Fragment`, serta `WelcomeViewModel`.
  - **auth/**: Halaman untuk otentikasi pengguna, termasuk pop-up dialog dan ViewModel terkait.
  - **home/**: Halaman beranda dan fitur terkait, termasuk manajemen artikel dan penulisan artikel, dengan ViewModel terpisah untuk masing-masing.
  - **notifications/**: Halaman untuk menampilkan notifikasi pengguna, dengan ViewModel.
  - **profile/**: Halaman untuk pengaturan profil pengguna, termasuk pop-up dialog dan ViewModel untuk masing-masing bagian.

- **data/**: Berisi model data dan repository untuk mengelola akses data.
  - **model/**: Kelas model untuk pengguna dan artikel.
  - **repository/**: Kelas repository untuk mengakses data dari database.

- **network/**: Kelas untuk mengelola komunikasi dengan API.

- **utils/**: Kelas utilitas untuk fungsi umum dalam aplikasi.

## Struktur Direktori Proyek Resource

```
com/
└── devmoss/
    └── kabare/
        ├── res/
        │   ├── drawable/
        │   │   ├── ic_launcher.xml           // Ikon aplikasi
        │   │   ├── ic_launcher_foreground.xml // Ikon latar depan
        │   │   ├── ic_launcher_background.xml  // Ikon latar belakang
        │   │   └── other_drawable_files.xml    // Gambar drawable lainnya
        │   ├── layout/
        │   │   ├── activity_intro.xml          // Layout untuk IntroActivity
        │   │   ├── fragment_intro.xml          // Layout untuk IntroFragment
        │   │   ├── fragment_welcome_page_1.xml // Layout untuk WelcomePage1Fragment
        │   │   ├── fragment_welcome_page_2.xml // Layout untuk WelcomePage2Fragment
        │   │   ├── fragment_welcome_page_3.xml // Layout untuk WelcomePage3Fragment
        │   │   ├── fragment_sign_in.xml         // Layout untuk SignInFragment
        │   │   ├── fragment_sign_up_input.xml   // Layout untuk SignUpInputFragment
        │   │   ├── fragment_lupa_password.xml    // Layout untuk LupaPasswordGantiFragment
        │   │   ├── fragment_home.xml             // Layout untuk HomeFragment
        │   │   ├── fragment_artikel.xml          // Layout untuk ArtikelFragment
        │   │   ├── fragment_search_page.xml      // Layout untuk SearchPageFragment
        │   │   ├── fragment_manajemen_artikel.xml // Layout untuk ManajemenArtikelFragment
        │   │   ├── fragment_notifikasi.xml       // Layout untuk NotifikasiFragment
        │   │   ├── fragment_profile.xml          // Layout untuk ProfileFragment
        │   │   ├── fragment_pengaturan_akun.xml  // Layout untuk PengaturanAkunFragment
        │   │   ├── fragment_akun_anda.xml        // Layout untuk AkunAndaFragment
        │   │   ├── fragment_privasi.xml          // Layout untuk PrivasiFragment
        │   │   ├── fragment_tentang.xml          // Layout untuk TentangFragment
        │   │   ├── fragment_keamanan.xml         // Layout untuk KeamananFragment
        │   │   └── popups/
        │   │       ├── dialog_lupa_password_konfirmasi_email.xml // Dialog pop-up konfirmasi email
        │   │       ├── dialog_lupa_password_verifikasi_email.xml  // Dialog pop-up verifikasi email
        │   │       ├── dialog_sign_up_konfirmasi_email.xml        // Dialog pop-up konfirmasi email saat mendaftar
        │   │       ├── dialog_sign_up_verifikasi_email.xml        // Dialog pop-up verifikasi email saat mendaftar
        │   │       ├── dialog_sign_up_berhasil.xml                // Dialog pop-up berhasil mendaftar
        │   │       └── dialog_lupa_password_berhasil.xml          // Dialog pop-up berhasil mengganti password
        │   ├── values/
        │   │   ├── strings.xml                     // String sumber daya untuk teks aplikasi
        │   │   ├── colors.xml                      // Warna yang digunakan

 dalam aplikasi
        │   │   ├── dimens.xml                      // Dimensi untuk margin dan padding
        │   │   └── styles.xml                      // Gaya yang digunakan untuk elemen UI
        │   └── mipmap/
        │       ├── ic_launcher.png                 // Ikon aplikasi dalam format PNG
        │       ├── ic_launcher_round.png           // Ikon bulat aplikasi
        │       └── other_mipmap_files.png          // Ikon mipmap lainnya
```

### Penjelasan Struktur Resource

- **drawable/**: Folder untuk menyimpan gambar dan ikon dalam format drawable. Ini mencakup ikon aplikasi, ikon latar belakang, dan gambar lain yang digunakan dalam aplikasi.
  
- **layout/**: Folder untuk menyimpan file XML yang mendefinisikan tampilan UI untuk setiap Activity dan Fragment dalam aplikasi. Setiap halaman memiliki file layout yang sesuai.

- **values/**: Folder yang menyimpan file sumber daya terstruktur, seperti string, warna, dimensi, dan gaya.
  - **strings.xml**: Berisi string sumber daya untuk teks yang digunakan dalam aplikasi.
  - **colors.xml**: Mendefinisikan warna yang dapat digunakan di seluruh aplikasi.
  - **dimens.xml**: Mengatur dimensi yang digunakan untuk margin, padding, dan ukuran lainnya.
  - **styles.xml**: Menentukan gaya untuk elemen UI.

- **mipmap/**: Folder untuk menyimpan ikon aplikasi dalam berbagai ukuran untuk berbagai resolusi layar.
