package org.d3if0047.canvufyminiproject.ui.screen

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if0047.canvufyminiproject.model.Art
import org.d3if0047.canvufyminiproject.network.ApiStatus
import org.d3if0047.canvufyminiproject.network.ArtApi
import java.io.ByteArrayOutputStream
import java.lang.Exception


class AddMomentViewModel : ViewModel() {
    var data = mutableStateOf(emptyList<Art>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = ArtApi.service.getArt(userId)
                status.value = ApiStatus.SUCCESS

            } catch (e: Exception) {
                Log.e("AddMomentViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, deskripsi: String, alamat: String, harga: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ArtApi.service.postArt(
                    userId,
                    deskripsi.toRequestBody("text/plain".toMediaTypeOrNull()),
                    alamat.toRequestBody("text/plain".toMediaTypeOrNull()),
                    harga.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    retrieveData(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("AddMomentViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteData(userId: String, obatId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("AddMomentViewModel", "Attempting to delete obat with ID: $obatId using user ID: $userId")
                val result = ArtApi.service.deleteArt(userId, obatId)
                Log.d("AddMomentViewModel", "API Response: status=${result.status}, message=${result.message}")
                if (result.status == "success") {
                    retrieveData(userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("AddMomentViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody)
    }


    fun clearMessage() { errorMessage.value=null}
}
