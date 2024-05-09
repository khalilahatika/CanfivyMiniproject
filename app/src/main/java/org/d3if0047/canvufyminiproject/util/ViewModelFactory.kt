package org.d3if0047.canvufyminiproject.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if0047.canvufyminiproject.database.PaletDao
import org.d3if0047.canvufyminiproject.ui.screen.DetailPalet
import org.d3if0047.canvufyminiproject.ui.screen.MainViewModel

class ViewModelFactory (
    private val dao: PaletDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(DetailPalet::class.java)) {
            return DetailPalet(dao) as T
        }

        throw IllegalArgumentException("Unkown ViewModel class")
    }
}