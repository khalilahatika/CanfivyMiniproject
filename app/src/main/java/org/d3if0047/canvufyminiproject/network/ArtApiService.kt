package org.d3if0047.canvufyminiproject.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if0047.canvufyminiproject.model.Art
import org.d3if0047.canvufyminiproject.model.OpStatus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

// Ensure the BASE_URL ends with a slash
private const val BASE_URL = "https://khalilaatika.my.id/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ArtApiService {
    @GET("json.php")
    suspend fun getArt(
        @Query("auth") userId: String
    ): List<Art>


    @Multipart
    @POST("json.php")
    suspend fun postArt(
        @Part("auth")userId: String,
        @Part("deskripsi")deskripsi: RequestBody,
        @Part("alamat")alamat: RequestBody,
        @Part("harga")harga: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @DELETE("json.php")
    suspend fun deleteArt(
        @Query("auth")userId: String,
        @Query("id") id: String,
    ): OpStatus

}

object ArtApi {
    val service: ArtApiService by lazy {
        retrofit.create(ArtApiService::class.java)
    }
    fun getArtUrl(image: String): String {
        return "$BASE_URL$image"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED}
