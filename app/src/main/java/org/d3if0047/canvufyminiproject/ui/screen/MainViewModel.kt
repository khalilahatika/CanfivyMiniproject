package org.d3if0047.canvufyminiproject.ui.screen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if0047.canvufyminiproject.database.PaletDao
import org.d3if0047.canvufyminiproject.model.Pallete

class MainViewModel(dao: PaletDao) : ViewModel() {

    val data: StateFlow<List<Pallete>> = dao.getPalet().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )


}

