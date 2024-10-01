### Struktur Direktori Proyek Main
```
com/
└── devmoss/
    └── kabare/
        ├── ui/
        │   ├── intro/
        │   │   ├── IntroActivity.kt
        │   ├── welcome/
        │   │   ├── WelcomePage1Activity.kt
        │   │   ├── WelcomePage2Activity.kt
        │   │   ├── WelcomePage3Activity.kt
        │   ├── auth/
        │   │   ├── SignInActivity.kt
        │   │   ├── SignUpInputActivity.kt
        │   │   ├── LupaPasswordGantiActivity.kt
        │   │   ├── popups/
        │   │   │   ├── LupaPasswordKonfirmasiEmailDialog.kt
        │   │   │   ├── LupaPasswordVerifEmailDialog.kt
        │   │   │   ├── SignUpKonfirmasiEmailDialog.kt
        │   │   │   ├── SignUpVerifikasiEmailDialog.kt
        │   │   │   ├── SignUpBerhasilDialog.kt
        │   │   │   ├── LupaPasswordBerhasilDialog.kt
        │   ├── home/
        │   │   ├── HomeActivity.kt
        │   │   ├── ArtikelActivity.kt
        │   │   ├── SearchPageActivity.kt
        │   │   ├── ManajemenArtikelActivity.kt
        │   │   ├── penulisan_artikel/
        │   │   │   ├── PenulisanArtikelActivity.kt
        │   │   │   ├── KonfirmasiPublikasiDialog.kt
        │   ├── notifications/
        │   │   ├── NotifikasiActivity.kt
        │   ├── profile/
        │   │   ├── ProfileActivity.kt
        │   │   ├── PengaturanAkunActivity.kt
        │   │   ├── AkunAndaActivity.kt
        │   │   ├── PrivasiActivity.kt
        │   │   ├── TentangActivity.kt
        │   │   ├── KeamananActivity.kt
        │   │   ├── popups/
        │   │   │   ├── ChangePasswordDialog.kt
        │   │   │   ├── ChangePasswordBerhasilDialog.kt
        ├── data/
        │   ├── model/
        │   │   ├── User.kt
        │   │   ├── Article.kt
        │   ├── repository/
        │   │   ├── AuthRepository.kt
        │   │   ├── ArticleRepository.kt
        │   │   ├── ProfileRepository.kt
        ├── network/
        │   ├── ApiClient.kt
        │   ├── ApiService.kt
        └── utils/
            ├── Constants.kt
            ├── SessionManager.kt
```

### Penjelasan Struktur

#### 1. **UI (User Interface) Layer**
   - **intro**: Folder untuk halaman intro.
     - `IntroActivity.kt`: Activity untuk halaman pengenalan aplikasi.
   
   - **welcome**: Folder untuk halaman-halaman welcome.
     - `WelcomePage1Activity.kt`: Activity untuk welcome page 1.
     - `WelcomePage2Activity.kt`: Activity untuk welcome page 2.
     - `WelcomePage3Activity.kt`: Activity untuk welcome page 3.

   - **auth**: Folder untuk halaman otentikasi (login/sign-up dan reset password).
     - `SignInActivity.kt`: Halaman untuk sign-in (masuk).
     - `SignUpInputActivity.kt`: Halaman input data untuk sign-up.
     - `LupaPasswordGantiActivity.kt`: Halaman untuk mengganti password yang lupa.
     - **popups**: Folder ini menampung semua pop-up terkait otentikasi:
       - `LupaPasswordKonfirmasiEmailDialog.kt`: Pop-up untuk konfirmasi email saat reset password.
       - `LupaPasswordVerifEmailDialog.kt`: Pop-up untuk verifikasi email saat reset password.
       - `SignUpKonfirmasiEmailDialog.kt`: Pop-up untuk konfirmasi email saat sign-up.
       - `SignUpVerifikasiEmailDialog.kt`: Pop-up untuk verifikasi email saat sign-up.
       - `SignUpBerhasilDialog.kt`: Pop-up untuk notifikasi sign-up berhasil.
       - `LupaPasswordBerhasilDialog.kt`: Pop-up untuk notifikasi reset password berhasil.

   - **home**: Folder untuk halaman beranda.
     - `HomeActivity.kt`: Halaman utama beranda setelah login.
     - `ArtikelActivity.kt`: Halaman yang menampilkan artikel.
     - `SearchPageActivity.kt`: Halaman pencarian.
     - `ManajemenArtikelActivity.kt`: Halaman untuk manajemen artikel, termasuk daftar artikel dalam draft, peninjauan, atau yang sudah dipublikasikan.
     - **penulisan_artikel**: Folder ini untuk penulisan artikel.
       - `PenulisanArtikelActivity.kt`: Halaman untuk menulis artikel baru.
       - `KonfirmasiPublikasiDialog.kt`: Pop-up untuk konfirmasi publikasi artikel.

   - **notifications**: Folder untuk halaman notifikasi.
     - `NotifikasiActivity.kt`: Halaman notifikasi untuk menampilkan daftar notifikasi.

   - **profile**: Folder untuk halaman profil pengguna.
     - `ProfileActivity.kt`: Halaman utama profil pengguna.
     - `PengaturanAkunActivity.kt`: Halaman pengaturan akun.
     - `AkunAndaActivity.kt`: Halaman untuk menampilkan detail akun.
     - `PrivasiActivity.kt`: Halaman untuk pengaturan privasi.
     - `TentangActivity.kt`: Halaman tentang aplikasi.
     - `KeamananActivity.kt`: Halaman pengaturan keamanan.
     - **popups**: Folder ini menampung semua pop-up terkait profil dan keamanan:
       - `ChangePasswordDialog.kt`: Pop-up untuk mengubah password.
       - `ChangePasswordBerhasilDialog.kt`: Pop-up untuk menampilkan perubahan password berhasil.

#### 2. **Data Layer**
   - **model**: Folder ini berisi model data yang digunakan dalam aplikasi.
     - `User.kt`: Model untuk data pengguna.
     - `Article.kt`: Model untuk data artikel.
   
   - **repository**: Folder ini menampung class untuk mengelola komunikasi antara aplikasi dan database atau API.
     - `AuthRepository.kt`: Menangani semua request terkait otentikasi (sign-in, sign-up, lupa password).
     - `ArticleRepository.kt`: Menangani semua request terkait artikel (pembuatan, update, delete, dll).
     - `ProfileRepository.kt`: Menangani request terkait profil pengguna.

#### 3. **Network Layer**
   - **ApiClient.kt**: Mengelola koneksi ke API dengan Retrofit atau library HTTP lain.
   - **ApiService.kt**: Mendefinisikan endpoint API yang digunakan untuk otentikasi, artikel, dan profil.

#### 4. **Utilities Layer (utils)**
   - **Constants.kt**: Menyimpan konstanta global seperti URL API, kode status, dll.
   - **SessionManager.kt**: Mengelola sesi login pengguna, menyimpan token autentikasi, dll.

### Alur Proyek
1. **Intro dan Welcome Pages**: Pengguna pertama kali akan melihat halaman intro, lalu tiga welcome pages.
2. **Sign-In dan Sign-Up**: Pengguna bisa login atau sign-up. Setiap proses melibatkan pop-up untuk konfirmasi/verifikasi email dan status keberhasilan.
3. **Home/Artikel**: Setelah login, pengguna masuk ke halaman beranda yang menampilkan daftar artikel, serta dapat menulis atau mengelola artikel.
4. **Notifikasi dan Profil**: Pengguna juga dapat mengakses notifikasi dan halaman profil untuk mengatur akun dan preferensi.

### Struktur Direktori Proyek Res
```
res/
├── drawable/
│   ├── bg_intro.xml
│   ├── ic_logo.xml
│   ├── ic_search.xml
│   ├── ic_notification.xml
│   ├── ic_profile.xml
│   ├── ic_back.xml
│   ├── ic_password_visibility.xml
│   ├── ic_password_visibility_off.xml
├── layout/
│   ├── activity_intro.xml
│   ├── activity_welcome_page1.xml
│   ├── activity_welcome_page2.xml
│   ├── activity_welcome_page3.xml
│   ├── activity_sign_in.xml
│   ├── activity_sign_up_input.xml
│   ├── activity_lupa_password_ganti.xml
│   ├── activity_home.xml
│   ├── activity_artikel.xml
│   ├── activity_search_page.xml
│   ├── activity_manajemen_artikel.xml
│   ├── activity_penulisan_artikel.xml
│   ├── activity_notifikasi.xml
│   ├── activity_profile.xml
│   ├── activity_pengaturan_akun.xml
│   ├── activity_akun_anda.xml
│   ├── activity_privasi.xml
│   ├── activity_tentang.xml
│   ├── activity_keamanan.xml
│   ├── dialog_lupa_password_konfirmasi_email.xml
│   ├── dialog_lupa_password_verif_email.xml
│   ├── dialog_sign_up_konfirmasi_email.xml
│   ├── dialog_sign_up_verifikasi_email.xml
│   ├── dialog_sign_up_berhasil.xml
│   ├── dialog_lupa_password_berhasil.xml
│   ├── dialog_konfirmasi_publikasi.xml
│   ├── dialog_change_password.xml
│   ├── dialog_change_password_berhasil.xml
├── mipmap/
│   ├── ic_launcher.png
│   ├── ic_launcher_round.png
│   └── ... (ikon untuk launcher dalam berbagai resolusi)
├── values/
│   ├── colors.xml
│   ├── strings.xml
│   ├── styles.xml
│   ├── dimens.xml
│   ├── themes.xml
└── xml/
    ├── network_security_config.xml
```
