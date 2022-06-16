package uk.fernando.math.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import uk.fernando.math.repository.HistoryRepository


class MultiplayerHistoryViewModel(private val rep: HistoryRepository) : BaseViewModel() {

    val history = Pager(PagingConfig(10)) { rep.getAllHistory(isMultiplayer = true) }
        .flow
        .cachedIn(viewModelScope)

}



