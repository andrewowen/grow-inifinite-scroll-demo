package com.example.infinitepagedemo.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.infinitepagedemo.ui.paging.PAGE_SIZE

class BeerPagingSource(
    private val query: String?,
    private val repo: BeerRepository
) : PagingSource<Int, BeerDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BeerDto> {
        return try {
            val page = params.key ?: 1
            val response = repo.getBeers(query, page, params.loadSize)
            val nextKey =
                if (response.isEmpty()) {
                    null
                } else {
                    page + (params.loadSize / PAGE_SIZE)
                }

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = nextKey

            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BeerDto>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

}