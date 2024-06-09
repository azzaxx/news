package com.example.news.mainpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.DataError
import com.example.news.Result
import com.example.news.di.database.repo.DataBaseRepository
import com.example.news.di.local.News
import com.example.news.di.network.repo.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.lastOrNull
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

    init {
        viewModelScope.launch {
            databaseRepository.getNewsList().collect { news ->
                Log.d("NEWS APP", "Database news updated ${news.size}")
                _fetchNewsResult.update { it.copy(data = news, isLoading = false) }
            }
        }
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
                        data = cashedNews.lastOrNull()?: emptyList()
                    )
                }

                is Result.SuccessResult -> {
                    databaseRepository.clearCashedNewsAndAddNew(result.data)
                    _fetchNewsResult.update { it.copy(isLoading = false) }
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