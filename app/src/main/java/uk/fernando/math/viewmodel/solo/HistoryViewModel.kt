package uk.fernando.math.viewmodel.solo

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import uk.fernando.math.repository.HistoryRepository
import uk.fernando.math.viewmodel.BaseViewModel


class HistoryViewModel(private val rep: HistoryRepository) : BaseViewModel() {

    val history = Pager(PagingConfig(10)) { rep.getAllHistory(isMultiplayer = false) }
        .flow
        .cachedIn(viewModelScope)

}



