package org.d3if0047.canvufyminiproject.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0047.canvufyminiproject.database.PaletDao
import org.d3if0047.canvufyminiproject.model.Pallete


class DetailPalet  (private val dao : PaletDao): ViewModel()  {
    fun insert(
        nama: String,
        kode1: String,
        kode2: String,
        kode3: String,
        kode4: String,
        genre : String

    ) {
        val pallete = Pallete(
            nama = nama,
            kode1 = kode1,
            kode2 = kode2,
            kode3 = kode3,
            kode4 = kode4,
            Genre = genre
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(pallete)
        }
    }

    suspend fun getPalet(id: Long): Pallete? {
        return dao.getPaletById(id)
    }
    fun update(id: Long,
               title: String,
               code1: String,
               code2: String,
               code3: String,
               code4: String,
               genre : String) {
        val pallete = Pallete(
            id = id,
            nama = title ,
            kode1 = code1,
            kode2 = code2,
            kode3 = code3,
            kode4 = code4,
            Genre = genre,
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(pallete)
        }
    }
    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}