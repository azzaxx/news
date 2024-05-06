package com.example.news.mainpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.news.DataError
import com.example.news.NewsDataRemoteMediator
import com.example.news.Result
import com.example.news.di.database.repo.DataBaseRepository
import com.example.news.di.local.News
import com.example.news.di.network.repo.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUIState(
    val isLoading: Boolean = false,
    val data: List<News> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DataBaseRepository
) : ViewModel() {
    private val _fetchNewsResult: MutableStateFlow<MainUIState> = MutableStateFlow(MainUIState())
    val mainStateFlow: StateFlow<MainUIState> = _fetchNewsResult

//    @OptIn(ExperimentalPagingApi::class)
//    private val pager = Pager(
//        config = PagingConfig(pageSize = 10),
//        remoteMediator = NewsDataRemoteMediator(dataBaseRepository, networkService),
//        pagingSourceFactory = { dataBaseRepository.pagingSource() }
//    ).flow

    init {
        fetchNews()
    }

    fun fetchNews() {
        _fetchNewsResult.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            when (val result = networkRepository.fetchNews()) {
                is Result.ErrorResult -> {
                    val cashedNews = databaseRepository.getNewsList()
                    _fetchNewsResult.value = MainUIState(
                        isLoading = false,
                        error = when (result.error) {
                            DataError.NetworkError.UNKNOWN -> "Server unknown error"
                            DataError.NetworkError.REQUEST_TIME_OUT -> "Request time out"
                            DataError.NetworkError.SERVER_DOWN -> "Server not responding"
                        },
                        data = cashedNews
                    )
                }

                is Result.SuccessResult -> {
                    _fetchNewsResult.value = MainUIState(
                        isLoading = false,
                        data = result.data
                    )
                }
            }
        }
    }

    fun addToFavorite(favorite: News) {
        viewModelScope.launch {
            if (favorite.isFavorite) {
                databaseRepository.addToNews(favorite)
            } else {
                databaseRepository.removeFromFavoriteNews(favorite)
            }
        }
    }
}