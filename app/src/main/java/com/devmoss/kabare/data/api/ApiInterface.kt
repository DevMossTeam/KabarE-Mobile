package com.devmoss.kabare.data.api

import com.devmoss.kabare.data.model.CheckUserRequest
import com.devmoss.kabare.data.model.ReaksiRequest
import com.devmoss.kabare.data.model.ResponseBookmark
import com.devmoss.kabare.data.model.ResponseCheckUser
import com.devmoss.kabare.data.model.ResponseGetBerita
import com.devmoss.kabare.data.model.ResponseGetTag
import com.devmoss.kabare.data.model.ResponseJumlahReaksi
import com.devmoss.kabare.data.model.ResponseReaksi
import com.devmoss.kabare.data.model.ResultBookmark
import com.devmoss.kabare.data.model.SignInRequest
import com.pakelcomedy.kabare.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
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
    fun getBookmarks(
        @Query("user_id") userId: String
    ): Call<List<ResultBookmark>>

    // Endpoint untuk menambah atau menghapus reaksi (like/dislike)
    @POST("reaksi.php")
    fun postReaksi(@Body reaksiRequest: ReaksiRequest): Call<ResponseReaksi>

    // Endpoint untuk mendapatkan jumlah reaksi (like dan dislike)
    @GET("reaksi.php")
    fun getJumlahReaksi(@Query("berita_id") beritaId: String): Call<ResponseJumlahReaksi>

    // POST request to create a new user (signup)
    @POST("signup.php")
    fun createUser(@Body request: User): Call<ResponseBody>

    @POST("signin.php")  // Ensure this matches your API endpoint
    fun signIn(@Body request: SignInRequest): Call<ResponseBody>

    @POST("check_user.php")
    fun checkUser(@Body request: CheckUserRequest): Call<ResponseCheckUser>
}
