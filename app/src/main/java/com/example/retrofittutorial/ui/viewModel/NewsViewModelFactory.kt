package com.example.retrofittutorial.ui.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofittutorial.ui.repository.NewsRepository

class NewsViewModelFactory(
    val application: Application,
    val newsRepository: NewsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(application, newsRepository) as T
    }
}