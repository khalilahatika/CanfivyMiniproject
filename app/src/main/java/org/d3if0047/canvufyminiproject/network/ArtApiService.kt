package org.d3if0047.canvufyminiproject.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if0047.canvufyminiproject.model.Art
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

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
    suspend fun getArt(): List<Art>
}

object ArtApi {
    val service: ArtApiService by lazy {
        retrofit.create(ArtApiService::class.java)
    }
    fun getArtUrl(imageId: String): String {
        return "${BASE_URL}json.php$imageId.jpg"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED}
