package com.example.retrofittutorial.ui.repository

import androidx.lifecycle.LiveData
import com.example.retrofittutorial.ui.api.RetrofitInstance
import com.example.retrofittutorial.ui.db.ArticleDatabase
import com.example.retrofittutorial.ui.model.Article
import com.example.retrofittutorial.ui.model.NewsResponse
import retrofit2.Response

class NewsRepository(
    private val  db: ArticleDatabase
) {
    suspend fun getHandleNews(countryCode: String, pageSize: Int): Response<NewsResponse> =
        RetrofitInstance.api.getHeadlineNews(countryCode, pageSize)

    suspend fun searchNews(searchQuery: String, pageSize: Int): Response<NewsResponse> =
        RetrofitInstance.api.searchNews(searchQuery, pageSize)

    suspend fun saveNews(article: Article): Long = db.newsDao().upsert(article)

    fun getSaveNews(): LiveData<List<Article>> = db.newsDao().getAllData()

    suspend fun deleteNews(article: Article) = db.newsDao().delete(article)
}