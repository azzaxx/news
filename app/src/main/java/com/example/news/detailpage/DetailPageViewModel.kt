package com.example.news.detailpage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.DataError
import com.example.news.NEWS_ID_ARGUMENT
import com.example.news.Result
import com.example.news.di.database.repo.DataBaseRepository
import com.example.news.di.local.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailPageUIState(
    val isLoading: Boolean = false,
    val news: News? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class DetailPageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dataBaseRepository: DataBaseRepository
) : ViewModel() {
    private val newsId: String = checkNotNull(savedStateHandle[NEWS_ID_ARGUMENT])
    private val _detailPageUIStatus: MutableStateFlow<DetailPageUIState> = MutableStateFlow(
        DetailPageUIState(isLoading = true)
    )
    val detailPageUIState: StateFlow<DetailPageUIState> = _detailPageUIStatus

    init {
        viewModelScope.launch {
            when (val result = dataBaseRepository.getNewsById(newsId)) {
                is Result.ErrorResult -> {
                    _detailPageUIStatus.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = when (result.error) {
                                DataError.DatabaseError.UNKNOWN -> "Unknown database error"
                                DataError.DatabaseError.NOT_FOUND -> "Entity not found"
                            }
                        )
                    }
                }

                is Result.SuccessResult -> {
                    _detailPageUIStatus.update { it.copy(isLoading = false, news = result.data) }
                }
            }
        }
    }
}