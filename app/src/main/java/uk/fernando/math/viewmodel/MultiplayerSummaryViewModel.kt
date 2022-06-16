package uk.fernando.math.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import uk.fernando.math.database.entity.HistoryWithQuestions
import uk.fernando.math.database.entity.multiplayer.HistoryWithPLayers
import uk.fernando.math.repository.HistoryRepository
import uk.fernando.math.repository.MultiplayerRepository


class MultiplayerSummaryViewModel(private val rep: MultiplayerRepository) : BaseViewModel() {

    val history: MutableState<HistoryWithPLayers?> = mutableStateOf(null)

    fun getHistory(historyID: Int) {
        launchDefault {
//            history.value = rep.getQuestionByHistory(historyID)
        }
    }

}



