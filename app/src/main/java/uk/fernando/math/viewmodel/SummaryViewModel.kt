package uk.fernando.math.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import uk.fernando.math.database.entity.HistoryWithQuestions
import uk.fernando.math.repository.HistoryRepository


class SummaryViewModel(private val rep: HistoryRepository) : BaseViewModel() {

    val history: MutableState<HistoryWithQuestions?> = mutableStateOf(null)

    fun getHistory(historyID: Int) {
        launchDefault {
            history.value = rep.getQuestionByHistory(historyID)
        }
    }

}



