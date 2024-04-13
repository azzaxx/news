package com.example.news.mainpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.DataError
import com.example.news.Result
import com.example.news.di.local.News
import com.example.news.di.network.repo.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainState(
    val isLoading: Boolean = false,
    val data: List<News> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {
    private val _fetchNewsResult: MutableStateFlow<MainState> = MutableStateFlow(MainState())
    val mainStateFlow: StateFlow<MainState> = _fetchNewsResult.asStateFlow()

    init {
        fetchNews()
    }

    fun fetchNews() {
        _fetchNewsResult.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            when (val result = networkRepository.fetchNews()) {
                is Result.ErrorResult -> {
                    _fetchNewsResult.value = MainState(
                        isLoading = false,
                        error = when (result.error) {
                            DataError.NetworkError.UNKNOWN -> "Server unknown error"
                            DataError.NetworkError.REQUEST_TIME_OUT -> "Request time out"
                            DataError.NetworkError.SERVER_DOWN -> "Server not responding"
                        }
                    )
                }

                is Result.SuccessResult -> {
                    _fetchNewsResult.value = MainState(
                        isLoading = false,
                        data = result.data
                    )
                }
            }
        }
    }

    fun addToFavorite(favorite: News) {
        TODO("Not yet implemented")
    }
}