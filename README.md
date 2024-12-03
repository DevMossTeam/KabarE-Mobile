# PIYE KABARE?
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
        │   │   ├── SignUpFragment.kt
        │   │   ├── SignUpInputFragment.kt
        │   │   ├── InputPasswordFragment.kt 
        │   │   ├── popups/
        │   │   │   ├── LupaPasswordKonfirmasiEmailDialog.kt
        │   │   │   ├── LupaPasswordVerifEmailDialog.kt
        │   │   │   ├── SignUpVerifikasiEmailDialog.kt
        │   │   │   ├── LupaPasswordBerhasilDialog.kt
        │   │   │   ├── KonfirmasiUbahEmail.kt
        │   │   │   └── ChangePasswordDialog.kt   // Pop up switch account
        │   │   └── viewmodels/
        │   │       ├── SignInViewModel.kt
        │   │       ├── SignUpViewModel.kt
        │   │       ├── SignUpInputViewModel.kt
        │   │       └── InputPasswordViewModel.kt
        │   ├── home/
        │   │   ├── HomeFragment.kt
        │   │   ├── ArtikelFragment.kt
        │   │   ├── KomentarFragment.kt
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
        │   │   ├── settings/                        // Folder settings dipindahkan ke sini
        │   │   │   ├── UmumFragment.kt              // Umum
        │   │   │   ├── SettingNotifikasiFragment.kt        // Notifikasi
        │   │   │   ├── KeamananFragment.kt          // Keamanan
        │   │   │   ├── BantuanFragment.kt           // Bantuan
        │   │   │   ├── PusatBantuanFragment.kt      // Pusat Bantuan
        │   │   │   ├── HubungiFragment.kt           // Hubungi
        │   │   │   ├── PengaturanAkunFragment.kt    // Pengaturan Akun
        │   │   │   ├── AkunAndaFragment.kt          // Akun Anda
        │   │   │   ├── PrivasiFragment.kt           // Privasi
        │   │   │   ├── LicenseFragment.kt           // License
        │   │   │   ├── TentangFragment.kt           // Tentang
        │   │   │   ├── popups/
        │   │   │   │   ├── SocialMediaLinksDialog.kt  // Pop up konfirmasi keluar
        │   │   │   │   ├── SwitchAccountDialog.kt  // Pop up konfirmasi keluar
        │   │   │   │   ├── KonfirmasiKeluarDialog.kt  // Pop up konfirmasi keluar
        │   │   │   │   └── BerhasilKirimLaporanDialog.kt // Pop up berhasil kirim laporan
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
        │   ├── repository/
        │   │   ├── AuthRepository.kt
        │   │   ├── ArticleRepository.kt
        │   │   └── ProfileRepository.kt
        ├── network/
        │   ├── ApiConfig.kt
        │   └── ApiInterface.kt
        └── utils/
            ├── SessionManager.kt
```
### Penjelasan Struktur Proyek Utama

#### 1. **ui/**
Folder ini menyimpan semua komponen UI aplikasi, diorganisir berdasarkan fungsinya:

- **intro/**: Halaman pengantar aplikasi.
- **welcome/**: Halaman sambutan pengguna dengan beberapa fragment.
- **auth/**: Halaman untuk otentikasi pengguna, termasuk pop-up dialog dan ViewModel terkait.
- **home/**: Halaman beranda dan fitur terkait, termasuk manajemen artikel dan penulisan artikel.
- **notifications/**: Halaman untuk menampilkan notifikasi pengguna.
- **profile/**: Halaman untuk pengaturan profil pengguna, termasuk pengaturan akun dan privasi.

#### 2. **data/**
Berisi model data dan repository untuk mengelola akses data, termasuk:

- **model/**: Kelas model untuk pengguna dan artikel.
- **repository/**: Kelas repository untuk mengakses data dari database lokal dan remote.

#### 3. **utils/**
Kelas utilitas untuk fungsi umum dalam aplikasi, termasuk konstanta, pengelolaan sesi, dan fungsi ekstensi.
## Struktur Direktori Proyek Resource

```
com/
└── devmoss/
    └── kabare/
        ├── res/
        │   ├── drawable/
        │   │   ├── ic_launcher.xml                        // Ikon aplikasi
        │   │   ├── ic_launcher_foreground.xml              // Ikon latar depan
        │   │   ├── ic_launcher_background.xml               // Ikon latar belakang
        │   │   └── ICON LAIN LAIN
        │   ├── layout/
        │   │   ├── activity_intro.xml                       // Layout untuk IntroActivity
        │   │   ├── fragment_intro.xml                       // Layout untuk IntroFragment
        │   │   ├── fragment_welcome_page_1.xml              // Layout untuk WelcomePage1Fragment
        │   │   ├── fragment_welcome_page_2.xml              // Layout untuk WelcomePage2Fragment
        │   │   ├── fragment_welcome_page_3.xml              // Layout untuk WelcomePage3Fragment
        │   │   ├── fragment_sign_in.xml                      // Layout untuk SignInFragment
        │   │   ├── fragment_sign_up_input.xml                // Layout untuk SignUpInputFragment
        │   │   ├── fragment_lupa_password.xml                // Layout untuk LupaPasswordGantiFragment
        │   │   ├── fragment_home.xml                          // Layout untuk HomeFragment
        │   │   ├── fragment_artikel.xml                       // Layout untuk ArtikelFragment
        │   │   ├── fragment_komentar.xml                       // Layout untuk ArtikelFragment
        │   │   ├── fragment_search_page.xml                   // Layout untuk SearchPageFragment
        │   │   ├── fragment_manajemen_artikel.xml             // Layout untuk ManajemenArtikelFragment
        │   │   ├── fragment_notifikasi.xml                    // Layout untuk NotifikasiFragment
        │   │   ├── fragment_profile.xml                       // Layout untuk ProfileFragment
        │   │   ├── fragment_pengaturan_akun.xml               // Layout untuk PengaturanAkunFragment
        │   │   ├── fragment_akun_anda.xml                     // Layout untuk AkunAndaFragment
        │   │   ├── fragment_privasi.xml                       // Layout untuk PrivasiFragment
        │   │   ├── fragment_tentang.xml                       // Layout untuk TentangFragment
        │   │   ├── fragment_keamanan.xml                      // Layout untuk KeamananFragment
        │   │   ├── fragment_umum.xml                          // Layout untuk UmumFragment
        │   │   ├── fragment_settingnotifikasi.xml                    // Layout untuk NotifikasiFragment
        │   │   ├── fragment_bantuan.xml                       // Layout untuk BantuanFragment
        │   │   ├── fragment_pusat_bantuan.xml                 // Layout untuk PusatBantuanFragment
        │   │   ├── fragment_hubungi.xml                       // Layout untuk HubungiFragment
        │   │   ├── fragment_license.xml                       // Layout untuk LicenseFragment
        │   │   └── popups/
        │   │       ├── dialog_lupa_password_konfirmasi_email.xml // Dialog pop-up konfirmasi email
        │   │       ├── dialog_lupa_password_verifikasi_email.xml  // Dialog pop-up verifikasi email
        │   │       ├── dialog_sign_up_konfirmasi_email.xml        // Dialog pop-up konfirmasi email saat mendaftar
        │   │       ├── dialog_sign_up_verifikasi_email.xml        // Dialog pop-up verifikasi email saat mendaftar
        │   │       ├── dialog_social_media_links.xml               // Dialog untuk social media links
        │   │       ├── dialog_konfirmasi_keluar.xml                // Dialog pop-up konfirmasi keluar
        │   │       ├── dialog_switch_account.xml                   // Dialog pop-up switch account
        │   │       ├── dialog_berhasil_kirim_laporan.xml          // Dialog pop-up berhasil kirim laporan
        │   │       ├── dialog_change_password.xml                 // Dialog pop-up berhasil kirim laporan
        │   │       ├── dialog_konfirmasi_ubah_email.xml  // Dialog pop-up verifikasi email
        │   │       └── dialog_lupa_password_berhasil.xml          // Dialog pop-up berhasil mengganti password
        │   ├── values/
        │   │   ├── strings.xml                                  // String sumber daya untuk teks aplikasi
        │   │   ├── colors.xml                                   // Warna yang digunakan dalam aplikasi
        │   │   ├── dimens.xml                                   // Dimensi untuk margin dan padding
        │   │   ├── styles.xml                                   // Gaya yang digunakan untuk elemen UI
        │   │   └── themes.xml                                   // Tema yang digunakan dalam aplikasi
        │   ├── mipmap/
        │   │   ├── ic_launcher.png                              // Ikon aplikasi dalam format PNG
        │   │   ├── ic_launcher_round.png                        // Ikon bulat aplikasi
        │   │   └── other_mipmap_files.png                       // Ikon mipmap lainnya
        │   └── navigation/
        │       └── nav_graph.xml                               // Navigasi graph yang mendefinisikan navigasi antar fragment dan activity
```
### Penjelasan Struktur Folder Resource

Folder **res/** menyimpan semua sumber daya yang digunakan dalam aplikasi, termasuk drawable, layout, values, mipmap, dan navigasi. Berikut adalah rincian setiap subfolder:

#### 1. **drawable/**
Folder ini berisi gambar dan ikon aplikasi dalam format XML atau bitmap. Sumber daya drawable digunakan untuk elemen grafis dalam antarmuka pengguna.

#### 2. **layout/**
Folder ini berisi file XML yang mendefinisikan layout untuk aktivitas dan fragment. Setiap file layout mengatur bagaimana komponen UI ditampilkan.

#### 3. **values/**
Folder ini menyimpan sumber daya nilai seperti string, warna, dimensi, dan gaya. Ini memungkinkan pengelolaan konsisten dari nilai-nilai ini di seluruh aplikasi.

- **strings.xml**: Menyimpan string yang digunakan dalam aplikasi.
- **colors.xml**: Menyimpan definisi warna yang digunakan dalam desain aplikasi.
- **dimens.xml**: Menyimpan dimensi seperti padding dan margin.
- **styles.xml**: Menyimpan gaya untuk elemen UI.

#### 4. **mipmap/**
Folder ini berisi ikon aplikasi dalam berbagai ukuran untuk digunakan di launcher. Ikon mipmap dioptimalkan untuk berbagai resolusi layar.

#### 5. **navigation/**
Folder ini berisi file navigasi yang mendefinisikan bagaimana pengguna dapat berpindah antara fragment dan aktivitas dalam aplikasi.
