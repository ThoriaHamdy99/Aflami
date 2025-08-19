package com.amsterdam.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


internal fun <T : Any> createPagingSource(
    pageSize: Int = 20,
    scope: CoroutineScope,
    onPagingSource: ((com.amsterdam.paging.PagingSource<T>) -> Unit)? = null,
    action: suspend (page: Int) -> List<T>,
): Flow<PagingData<T>> {
    return Pager(
        config = PagingConfig(pageSize = pageSize),
        pagingSourceFactory = {
            PagingSource { page ->
                action(page)
            }.also {
                onPagingSource?.invoke(it)
            }
        }
    ).flow.cachedIn(scope)
}

internal class PagingSource<T : Any>(
    private val fetch: suspend (page: Int) -> List<T>,
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? =
        state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        return try {
            val data = fetch(page)
            LoadResult.Page(
                data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}
