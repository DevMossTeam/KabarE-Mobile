package com.devmoss.kabare.data.api

import com.devmoss.kabare.data.model.BookmarkStatusResponse
import com.devmoss.kabare.data.model.CheckUserRequest
import com.devmoss.kabare.data.model.ReaksiRequest
import com.devmoss.kabare.data.model.ReaksiResponse
import com.devmoss.kabare.data.model.ResetPasswordRequest
import com.devmoss.kabare.data.model.ResponseBookmark
import com.devmoss.kabare.data.model.ResponseCheckUser
import com.devmoss.kabare.data.model.ResponseDeleteKomentar
import com.devmoss.kabare.data.model.ResponseGetBerita
import com.devmoss.kabare.data.model.ResponseGetKomentar
import com.devmoss.kabare.data.model.ResponseGetTag
import com.devmoss.kabare.data.model.ResponseJumlahReaksi
import com.devmoss.kabare.data.model.ResponseLaporan
import com.devmoss.kabare.data.model.ResponsePostKomentar
import com.devmoss.kabare.data.model.ResponseReaksi
import com.devmoss.kabare.data.model.ResultBookmark
import com.devmoss.kabare.data.model.SignInRequest
import com.devmoss.kabare.model.PasswordChangeRequest
//import com.devmoss.kabare.model.EmailUpdateRequest
import com.devmoss.kabare.model.SecurityUpdateRequest
import com.devmoss.kabare.model.User
import com.devmoss.kabare.model.UserRequest
import com.devmoss.kabare.model.UserResponse
import com.devmoss.kabare.model.UserUpdateRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.PUT

interface ApiInterface {
    @GET("get_berita_detail.php") // Endpoint PHP untuk detail artikel
    fun getDetailBerita(
        @Query("beritaId") beritaId: String
    ): Call<ResponseGetBerita>

    @GET("get_berita_terkini.php")
    fun getBeritaTerkini(): Call<ResponseGetBerita>

    @GET("get_berita_populer.php")
    fun getBeritaPopuler(): Call<ResponseGetBerita>

    @GET("get_label_populer.php")
    fun getPopularTags(): Call<ResponseGetTag>

    @GET("get_berita_rekomendasi.php")
    fun getBeritaRekomendasi(
        @Query("user_id") userId: String
    ): Call<ResponseGetBerita>

    @GET("get_berita_terkait.php")
    fun getBeritaTerkait(
        @Query("kategori") kategori: String,
        @Query("berita_id") beritaId: String
    ): Call<ResponseGetBerita>

    // Menambah bookmark
    @POST("bookmark.php")
    fun addBookmark(
        @Body bookmark: ResultBookmark
    ): Call<ResponseBookmark>

    // Mendapatkan daftar bookmark berdasarkan user_id
    @GET("bookmark.php")
    fun getBeritaBookmark(
        @Query("user_id") userId: String
    ): Call<ResponseGetBerita>

    // Metode untuk memeriksa status bookmark
    @GET("bookmark.php")
    fun checkBookmarkStatus(
        @Query("user_id") userId: String,
        @Query("berita_id") beritaId: String
    ): Call<BookmarkStatusResponse>

    // Endpoint untuk menambah atau menghapus reaksi (like/dislike)
    @POST("reaksi.php")
    fun postReaksi(@Body reaksiRequest: ReaksiRequest): Call<ResponseReaksi>

    // Endpoint untuk mendapatkan jumlah reaksi (like dan dislike)
    @GET("reaksi.php")
    fun getJumlahReaksi(@Query("berita_id") beritaId: String): Call<ResponseJumlahReaksi>

    @GET("komentar.php/{berita_id}")
    fun getKomentar(@Query("berita_id") beritaId: String): Call<ResponseGetKomentar>

    @POST("komentar.php")
    fun postKomentar(
        @Query("user_id") userId: String,
        @Query("berita_id") beritaId: String,
        @Query("teks_komentar") teksKomentar: String
    ): Call<ResponsePostKomentar>

    @DELETE("komentar.php")
    fun deleteKomentar(@Query("id_komentar") idKomentar: String): Call<ResponseDeleteKomentar>

    @GET("komentar_terbanyak.php")
    fun getKomentarTerbanyak(): Call<ResponseGetBerita>

    @GET("search.php")
    fun searchBerita(
        @Query("katakunci") katakunci: String? = null,
        @Query("kategori") kategori: String? = null,
        @Query("tag") tag: String? = null
    ): Call<ResponseGetBerita>

    @POST("report_berita.php") // Pastikan URL sesuai dengan endpoint API Anda
    fun createLaporanBerita(
        @Query("user_id") userId: String,
        @Query("berita_id") beritaId: String,
        @Query("pesan") pesan: String,
        @Query("detail_pesan") detailPesan: String
    ): Call<ResponseLaporan>

    @GET("status_like.php")
    fun cekStatusLike(
        @Query("user_id") userId: String,
        @Query("berita_id") beritaId: String
    ): Call<ReaksiResponse>

    // POST request to create a new user (signup)
    @POST("signup.php")
    fun createUser(@Body request: User): Call<ResponseBody>

    @POST("signin.php")  // Ensure this matches your API endpoint
    fun signIn(@Body request: SignInRequest): Call<ResponseBody>

    @POST("check_user.php")
    fun checkUser(@Body request: CheckUserRequest): Call<ResponseCheckUser>

    @POST("reset_password.php")
    fun resetPassword(@Body request: ResetPasswordRequest): Call<ResponseBody>

    @POST("pengguna.php")
    fun getUserData(@Body request: UserRequest): Call<UserResponse>

    @PUT("pengguna.php")
    fun updateUserData(@Body request: UserUpdateRequest): Call<UserResponse>

    @PUT("pengguna.php")  // Change this to PUT
    fun updateEmail(@Body request: SecurityUpdateRequest): Call<Void>  // Assuming the response body is empty

    @PATCH("pengguna.php")
    fun changePassword(@Body request: PasswordChangeRequest): Call<UserResponse>

    @POST("masukan.php")
    suspend fun submitComplaint(@Body message: Map<String, String>): Response<ResponseBody>
}