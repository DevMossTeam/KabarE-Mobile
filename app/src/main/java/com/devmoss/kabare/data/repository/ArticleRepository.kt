package com.devmoss.kabare.data.repository

import com.devmoss.kabare.data.model.Artikel
import java.util.*

object ArticleRepository {

    // Simulated data source
    private val articles = mutableListOf<Artikel>()

    init {
        // Add some sample articles
        articles.add(
            Artikel(
                id = 1,
                label = listOf("#habiburrohman","#technology","#prestasi"),
                title = "Diterbitkan,Diterima Event Metode dalam pengujian perangkat lunak terbagi menjadi beberapa kategori utama berdasarkan aspek yang diuji, tingkat detail pengujian, dan pendekatan yang digunakan. Berikut adalah beberapa metode pengujian perangkat lunak yang umum:",
                timestamp = System.currentTimeMillis() - 60000, // 1 minute ago
                authorProfile = "John Doe",
                kategori = "Prestasi",
                description = "Metode dalam pengujian perangkat lunak terbagi menjadi beberapa kategori utama berdasarkan aspek yang diuji, tingkat detail pengujian, dan pendekatan yang digunakan. Berikut adalah beberapa metode pengujian perangkat lunak yang umum:\n" +
                        "\n" +
                        "Pengujian Berdasarkan Tingkatannya:\n" +
                        "\n" +
                        "Unit Testing: Menguji bagian terkecil dari perangkat lunak, biasanya berupa fungsi atau metode, untuk memastikan setiap unit bekerja dengan benar.\n" +
                        "Integration Testing: Menguji integrasi antara beberapa unit atau modul untuk memastikan bahwa modul-modul tersebut bekerja bersama dengan baik.\n" +
                        "System Testing: Menguji seluruh sistem perangkat lunak sebagai satu kesatuan untuk memastikan bahwa semua bagian bekerja sesuai spesifikasi.\n" +
                        "Acceptance Testing: Menguji perangkat lunak dari sudut pandang pengguna akhir untuk memastikan bahwa sistem memenuhi kebutuhan dan persyaratan pengguna.\n" +
                        "Pengujian Berdasarkan Pendekatannya:\n" +
                        "\n" +
                        "Black Box Testing: Menguji perangkat lunak tanpa melihat ke dalam kode. Fokusnya pada input dan output tanpa mengetahui struktur internal sistem.\n" +
                        "White Box Testing: Menguji perangkat lunak dengan pengetahuan tentang struktur internal kode dan alur logika program.\n" +
                        "Gray Box Testing: Kombinasi antara black box dan white box testing, di mana penguji memiliki sebagian pengetahuan tentang struktur internal perangkat lunak.\n" +
                        "Pengujian Berdasarkan Otomatisasi:\n" +
                        "\n" +
                        "Manual Testing: Pengujian yang dilakukan secara manual oleh penguji tanpa menggunakan alat atau skrip otomatis.\n" +
                        "Automated Testing: Pengujian yang dilakukan menggunakan alat otomatisasi untuk menjalankan tes berulang kali, terutama dalam regression testing.\n" +
                        "Pengujian Berdasarkan Tujuannya:\n" +
                        "\n" +
                        "Functional Testing: Menguji fungsionalitas perangkat lunak untuk memastikan bahwa semua fitur bekerja sesuai dengan persyaratan.\n" +
                        "Non-functional Testing: Menguji aspek non-fungsional seperti performa, keamanan, kegunaan, dan keandalan.\n" +
                        "Performance Testing: Mengukur kinerja perangkat lunak seperti kecepatan, skalabilitas, dan efisiensi di bawah beban tertentu.\n" +
                        "Load Testing: Menguji bagaimana sistem menangani volume pekerjaan yang besar.\n" +
                        "Stress Testing: Menguji batas ketahanan perangkat lunak dengan memaksakan beban yang sangat besar hingga titik kegagalan.\n" +
                        "Scalability Testing: Menguji kemampuan sistem untuk menangani peningkatan volume pekerjaan.\n" +
                        "Security Testing: Menguji untuk memastikan bahwa perangkat lunak terlindungi dari serangan, kerentanan, dan ancaman keamanan.\n" +
                        "Usability Testing: Menguji apakah perangkat lunak mudah digunakan dan intuitif bagi pengguna.\n" +
                        "Compatibility Testing: Menguji perangkat lunak untuk memastikan kompatibilitasnya dengan berbagai lingkungan, seperti sistem operasi, browser, atau perangkat keras.\n" +
                        "Pengujian Berdasarkan Teknik:\n" +
                        "\n" +
                        "Regression Testing: Menguji kembali fitur perangkat lunak setelah ada perubahan untuk memastikan tidak ada fungsi yang rusak.\n" +
                        "Smoke Testing: Pengujian awal untuk memastikan bahwa fungsi-fungsi utama bekerja dengan baik sebelum melakukan pengujian yang lebih mendalam.\n" +
                        "Sanity Testing: Pengujian cepat untuk memeriksa apakah fitur baru atau bug fix bekerja dengan benar.\n" +
                        "Exploratory Testing: Pengujian tanpa skrip pengujian yang spesifik, biasanya dilakukan untuk menemukan bug dengan mengeksplorasi sistem secara spontan.\n" +
                        "Metode-metode ini bisa digunakan secara bersamaan tergantung pada kebutuhan, skala, dan kompleksitas dari perangkat lunak yang diuji.",
                isBookmarked = true,
                jumlahDislike = 31,
                jumlahLike = 552,
                jumlahShare = 90,
                isTerkini = true,
                tanggalPengajuan ="12 September 2024",
                tanggalDiperbarui = "Diperbarui 2 jam yang lalu",
                status = com.devmoss.kabare.data.model.StatusArtikel.DITERBITKAN,
                statusPeninjauan = com.devmoss.kabare.data.model.StatusPeninjauan.PUBLISHED,
                isTeratas = false
            )
        )
        articles.add(
            Artikel(
                id = 44,
                label = listOf("#polije","#technology","#prestasi"),
                title = "Diterbitkan,Diterima Didik SMPN 1 Paiton di Ajang Polije Open Karate Championship Direktur Cup XII Tahun 2024",
                timestamp = System.currentTimeMillis() - 60000, // 1 minute ago
                authorProfile = "John Doe",
                kategori = "Prestasi",
                description = "Prestasi Gemilang Peserta Didik SMPN 1 Paiton di Ajang Polije Open Karate Championship Direktur Cup XII Tahun 2024\n" +
                        "\n" +
                        "Dengan bangga, SMPN 1 Paiton mengumumkan prestasi luar biasa yang diraih oleh dua peserta didik berbakat, Dinda Khayla Salsabila dan Kayla Alfanora Putri, dalam ajang Polije Open Karate Championship Direktur Cup XII Tahun 2024 yang diselenggarakan di GOR Perjuangan 45 Politeknik Negeri Jember pada tanggal 7 -- 8 September 2024.\n" +
                        "\n" +
                        "Dalam kategori KATA Perorangan SMP Putri Festival, Dinda Khayla Salsabila berhasil meraih Juara 1 dan membawa pulang medali emas. Prestasi ini membuktikan dedikasi, kerja keras, dan kemampuan luar biasa yang dimiliki oleh Dinda dalam dunia olahraga karate.\n" +
                        "\n" +
                        "Sementara itu, Kayla Alfanora Putri tak kalah gemilang dengan prestasinya sebagai Juara 2 dalam kategori yang sama. Kayla berhasil memperoleh medali perak dan sertifikat sebagai bentuk apresiasi atas perjuangan dan ketangguhan yang ia tunjukkan selama kompetisi.\n" +
                        "\n" +
                        "Kedua prestasi ini tidak hanya menjadi kebanggaan bagi Dinda dan Kayla, tetapi juga mengharumkan nama SMPN 1 Paiton di tingkat regional. Semoga capaian ini menjadi inspirasi bagi seluruh peserta didik untuk terus berprestasi dan mengembangkan potensi di berbagai bidang.\n" +
                        "\n" +
                        "#pengumuman resmi sekolah atau apresiasi terhadap prestasi siswa-siswi tersebut.",
                isBookmarked = true,
                jumlahDislike = 31,
                jumlahLike = 552,
                jumlahShare = 90,
                tanggalPengajuan ="12 September 2024",
                tanggalDiperbarui = "Diperbarui 2 jam yang lalu",
                status = com.devmoss.kabare.data.model.StatusArtikel.DITERBITKAN,
                statusPeninjauan = com.devmoss.kabare.data.model.StatusPeninjauan.PUBLISHED,
                isTerkini = true,
                isTeratas = false
            )
        )

        articles.add(
            Artikel(
                id = 3,
                label = listOf("#nnfkfjk","#JKNjnjjnk"),
                title = "Draf,Diterima Sertifikasi TOEIC untuk Guru Bahasa Inggris SMK di Jember",
                timestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
                authorProfile = "Jane Smith",
                description = "Politeknik Negeri Jember (Polije) melalui Unit Pengembangan Akademik (UPA) Bahasa, menggelar kegiatan TOEIC Workshop",
                isBookmarked = false,
                jumlahDislike = 31,
                jumlahLike = 442,
                jumlahShare = 40,
                kategori = "Prestasi",
                status = com.devmoss.kabare.data.model.StatusArtikel.DRAF,
                statusPeninjauan = com.devmoss.kabare.data.model.StatusPeninjauan.PUBLISHED,
                isTerkini = false,
                tanggalPengajuan ="12 September 2024",
                tanggalDiperbarui = "Diperbarui 2 jam yang lalu",
                isTeratas = true // Tambahkan atribut ini
            )
        )
        articles.add(
            Artikel(
                id = 4,
                label = listOf("#hanay","#sport"),
                title = "Diterbitkan,Diterima Latest Tech Innovations",
                timestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
                authorProfile = "Jane Smith",
                description = "This article discusses the latest innovations in technology.",
                isBookmarked = false,
                kategori = "Prestasi",
                isTerkini = false,
                status = com.devmoss.kabare.data.model.StatusArtikel.DITERBITKAN,
                statusPeninjauan = com.devmoss.kabare.data.model.StatusPeninjauan.PUBLISHED,
                tanggalPengajuan ="12 September 2024",
                tanggalDiperbarui = "Diperbarui 2 jam yang lalu",
                isTeratas = true // Tambahkan atribut ini
            )
        )
        articles.add(
            Artikel(
                id = 5,
                label = listOf("#habi","#baan"),
                title = "Dalam Peninjauan,Diterima Latest Tech Innovations",
                timestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
                authorProfile = "Jane Smith",
                description = "This article discusses the latest innovations in technology.",
                isBookmarked = false,
                kategori = "Sport",
                isTerkini = false,
                tanggalPengajuan ="12 September 2024",
                tanggalDiperbarui = "Diperbarui 2 jam yang lalu",
                status = com.devmoss.kabare.data.model.StatusArtikel.DALAM_PENINJAUAN,
                statusPeninjauan = com.devmoss.kabare.data.model.StatusPeninjauan.DIAJUKAN,
                isTeratas = true // Tambahkan atribut ini
            )
        )
        articles.add(
            Artikel(
                id = 10,
                label = listOf("#habi","#baan"),
                title = "Dalam Peninjauan,Diterima Latest Tech Innovations",
                timestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
                authorProfile = "Jane Smith",
                description = "This article discusses the latest innovations in technology.",
                isBookmarked = false,
                kategori = "Sport",
                isTerkini = false,
                tanggalPengajuan ="12 September 2024",
                tanggalDiperbarui = "Diperbarui 2 jam yang lalu",
                status = com.devmoss.kabare.data.model.StatusArtikel.DALAM_PENINJAUAN,
                statusPeninjauan = com.devmoss.kabare.data.model.StatusPeninjauan.REVISI_KECIL,
                isTeratas = true // Tambahkan atribut ini
            )
        )
        articles.add(
            Artikel(
                id = 110,
                label = listOf("#habi","#baan"),
                title = "Dalam Peninjauan,Diterima Latest Tech Innovations",
                timestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
                authorProfile = "Jane Smith",
                description = "This article discusses the latest innovations in technology.",
                isBookmarked = false,
                kategori = "Sport",
                isTerkini = false,
                tanggalPengajuan ="12 September 2024",
                tanggalDiperbarui = "Diperbarui 2 jam yang lalu",
                status = com.devmoss.kabare.data.model.StatusArtikel.DALAM_PENINJAUAN,
                statusPeninjauan = com.devmoss.kabare.data.model.StatusPeninjauan.DITOLAK,
                isTeratas = true // Tambahkan atribut ini
            )
        )
        articles.add(
            Artikel(
                id = 5,
                label = listOf("#habi","#baan"),
                title = "Dalam Peninjauan,Diterima Latest Tech Innovations",
                timestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
                authorProfile = "Jane Smith",
                description = "This article discusses the latest innovations in technology.",
                isBookmarked = false,
                kategori = "Sport",
                tanggalPengajuan ="12 September 2024",
                tanggalDiperbarui = "Diperbarui 2 jam yang lalu",
                isTerkini = false,
                status = com.devmoss.kabare.data.model.StatusArtikel.DALAM_PENINJAUAN,
                statusPeninjauan = com.devmoss.kabare.data.model.StatusPeninjauan.REVISI_BESAR,
                isTeratas = true // Tambahkan atribut ini
            )
        )
        articles.add(
            Artikel(
                id = 6,
                label = listOf("#hbhjdb","#djddnd"),
                title = "Diterbitkan,Diterima Latest Tech Innovations",
                timestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
                authorProfile = "Jane Smith",
                description = "This article discusses the latest innovations in technology.",
                isBookmarked = false,
                kategori = "Olahraga",
                isTerkini = false,
                tanggalPengajuan ="12 September 2024",
                tanggalDiperbarui = "Diperbarui 2 jam yang lalu",
                status = com.devmoss.kabare.data.model.StatusArtikel.DITERBITKAN,
                statusPeninjauan = com.devmoss.kabare.data.model.StatusPeninjauan.PUBLISHED,
                isTeratas = true // Tambahkan atribut ini
            )
        )
        articles.add(
            Artikel(
                id = 7,
                label = listOf("#ghvdgv","#dddd"),
                title = "Diterbitkan,Diterima Latest Tech Innovations",
                timestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
                authorProfile = "Jane Smith",
                description = "This article discusses the latest innovations in technology.",
                isBookmarked = false,
                kategori = "Balap",
                tanggalPengajuan ="12 September 2024",
                tanggalDiperbarui = "Diperbarui 2 jam yang lalu",
                status = com.devmoss.kabare.data.model.StatusArtikel.DITERBITKAN,
                statusPeninjauan = com.devmoss.kabare.data.model.StatusPeninjauan.PUBLISHED,
                isTerkini = false,
                isTeratas = true // Tambahkan atribut ini
            )
        )
    }

    // Function to retrieve all articles
    fun getArticles(): List<Artikel> {
        return articles
    }

    // Function to toggle bookmark status of an article
    fun toggleBookmark(articleId: Int) {
        val article = articles.find { it.id == articleId }
        article?.let {
            it.isBookmarked = !it.isBookmarked
        }
    }

    // Add a new article to the repository (for testing or adding new content)
    fun addArticle(article: Artikel) {
        articles.add(article)
    }

    // Function to remove an article (optional)
    fun removeArticle(articleId: Int) {
        articles.removeAll { it.id == articleId }
    }
}
