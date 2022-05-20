package uk.fernando.math.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.repository.HistoryRepository


class HistoryViewModel(private val rep: HistoryRepository) : BaseViewModel() {

    val history: MutableState<List<HistoryEntity>> = mutableStateOf(emptyList())

    init {
        launchDefault {
            rep.getAllHistory().collect {
                history.value = it
            }
        }
    }

}



