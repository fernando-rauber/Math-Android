package uk.fernando.math.viewmodel.multiplayer

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import uk.fernando.math.repository.HistoryRepositoryImpl
import uk.fernando.math.viewmodel.BaseViewModel


class MultiplayerHistoryViewModel(private val rep: HistoryRepositoryImpl) : BaseViewModel() {

    val history = Pager(PagingConfig(10)) { rep.getAllHistory(isMultiplayer = true) }
        .flow
        .cachedIn(viewModelScope)

}



