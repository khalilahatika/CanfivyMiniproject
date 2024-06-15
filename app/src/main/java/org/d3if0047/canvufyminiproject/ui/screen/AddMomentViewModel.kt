package org.d3if0047.canvufyminiproject.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.d3if0047.canvufyminiproject.model.Art
import org.d3if0047.canvufyminiproject.network.ApiStatus
import org.d3if0047.canvufyminiproject.network.ArtApi
import java.lang.Exception
import kotlin.math.log

class AddMomentViewModel : ViewModel() {
    var data = mutableStateOf(emptyList<Art>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set
    init {
        retrieveData()
    }

    fun retrieveData(){
        viewModelScope.launch (Dispatchers.IO){
            status.value = ApiStatus.LOADING
            try {
                data.value = ArtApi.service.getArt()
                status.value = ApiStatus.SUCCESS

            } catch (e: Exception){
                Log.d("AddMomentViewModel","Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }
}