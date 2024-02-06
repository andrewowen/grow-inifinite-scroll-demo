package com.example.infinitepagedemo.ui.launchedeffect

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infinitepagedemo.data.BeerDto
import com.example.infinitepagedemo.data.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PAGE_SIZE = 10

@HiltViewModel
class LaunchedEffectDemoViewModel @Inject constructor() : ViewModel() {

    private val repo: BeerRepository = BeerRepository()

    private val _query = mutableStateOf<String?>(null)
    val query = _query

    private val _isLoading = mutableStateOf(true)
    val isLoading = _isLoading

    private val _isLastPage = mutableStateOf(false)
    val isLastPage = _isLastPage

    private val _page = mutableIntStateOf(1)
    private val page = _page

    private val _beers: MutableStateFlow<List<BeerDto>> = MutableStateFlow(emptyList())
    val beers = _beers

    fun setQuery(query: String?) {
        _query.value = query
    }

    init {
        getBeers()
    }

    fun getBeers() {
        viewModelScope.launch {
            _beers.value = repo.getBeers(
                query = query.value,
                limit = PAGE_SIZE * 3
            )
            _page.intValue += 1
            _isLoading.value = false
        }
    }

    fun loadMoreBeers() {
        viewModelScope.launch {
            val beers = repo.getBeers(
                query = query.value,
                page = page.intValue,
                limit = PAGE_SIZE * 3
            )
            if (beers.isEmpty()) {
                _isLastPage.value = true
            } else {
                _beers.value = _beers.value + beers
                _page.intValue += 1
            }
        }

    }

    fun invalidate() {
        _page.intValue = 1
        _query.value = null
        _beers.value = emptyList()
    }
}