package uk.fernando.math.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import uk.fernando.math.repository.HistoryRepository
import uk.fernando.math.repository.MultiplayerRepository


class MultiplayerHistoryViewModel(private val rep: MultiplayerRepository) : BaseViewModel() {

    val history = Pager(PagingConfig(10)) { rep.getAllHistory() }
        .flow
        .cachedIn(viewModelScope)

}



