package com.example.infinitepagedemo.ui.paging

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.infinitepagedemo.data.BeerPagingSource
import com.example.infinitepagedemo.data.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val PAGE_SIZE = 10

@HiltViewModel
class PagingDemoViewModel @Inject constructor(): ViewModel() {

    private val _query = mutableStateOf<String?>(null)
    val query = _query

    private val repo: BeerRepository = BeerRepository()
    private lateinit var pagingSource : BeerPagingSource

    val beerPager = Pager(PagingConfig(
        pageSize = PAGE_SIZE,

    )) {
        BeerPagingSource(query.value, repo).also {
            pagingSource = it }
    }.flow

    fun setQuery(query: String?) {
        _query.value = query
    }

    fun invalidate() {
        pagingSource.invalidate()
    }

}