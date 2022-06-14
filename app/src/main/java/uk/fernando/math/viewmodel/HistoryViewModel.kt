package uk.fernando.math.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.repository.HistoryRepository


class HistoryViewModel(private val rep: HistoryRepository) : BaseViewModel() {

    val history: MutableState<List<HistoryEntity>> = mutableStateOf(emptyList())
    val isLoading = mutableStateOf(false)

    init {
        getAllHistory()
    }

    fun getAllHistory() {
        launchDefault {
            isLoading.value = true
            rep.getAllHistory().collect {
                history.value = it
                isLoading.value = false
            }
        }
    }
}



