package uk.fernando.math.viewmodel.solo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.repository.HistoryRepository
import uk.fernando.math.viewmodel.BaseViewModel


class SummaryViewModel(private val rep: HistoryRepository) : BaseViewModel() {

    val history: MutableState<HistoryWithPLayers?> = mutableStateOf(null)

    fun getHistory(historyID: Int) {
        launchDefault {
            history.value = rep.getQuestionByHistory(historyID)
        }
    }
}