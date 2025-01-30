package com.example.retrofittutorial.ui.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.retrofittutorial.ui.model.Article
import com.example.retrofittutorial.ui.model.NewsResponse
import com.example.retrofittutorial.ui.repository.NewsRepository
import com.example.retrofittutorial.ui.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(
    private val application: Application,
    private val newsRepository: NewsRepository
): AndroidViewModel(application) {

    var headlineNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesNewsPageSize = 1

    var searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPageSize = 1

    init {
        getHeadlineNews("us")
    }

    fun getHeadlineNews(countryCode: String) = viewModelScope.launch {
        safeHeadlineNewsCall(countryCode)
    }

    private fun handleHeadlineNews(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    fun getSearchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleSearchNews(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private suspend fun safeHeadlineNewsCall(countryCode: String) {
        headlineNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getHandleNews(countryCode, headlinesNewsPageSize)
                headlineNews.postValue(handleSearchNews(response))
            } else {
                headlineNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> headlineNews.postValue(Resource.Error("Network failure"))
                else -> headlineNews.postValue(Resource.Error("Conversion error!"))
            }
        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNews.postValue(Resource.Loading())

        try {
            if(hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPageSize)
                searchNews.postValue(handleSearchNews(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable){
            when(t) {
                is IOException -> searchNews.postValue(Resource.Error("Internet failure"))
                else -> searchNews.postValue(Resource.Error("Conversion error"))
            }
        }

    }

    fun saveNews(article: Article) = viewModelScope.launch {
        newsRepository.saveNews(article)
    }

    fun getSaveNews() = newsRepository.getSaveNews()

    fun deleteNews(article: Article) = viewModelScope.launch {
        newsRepository.deleteNews(article)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}