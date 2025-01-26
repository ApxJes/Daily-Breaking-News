package com.example.retrofittutorial.ui.repository

import com.example.retrofittutorial.ui.api.RetrofitInstance
import com.example.retrofittutorial.ui.db.ArticleDatabase
import com.example.retrofittutorial.ui.model.Article

class NewsRepository(
    val articleDatabase: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) =
        articleDatabase.articleDao().upsert(article)

    fun getSaveNews() = articleDatabase.articleDao().getAllData()

    suspend fun delete(article: Article) =
        articleDatabase.articleDao().delete(article)
}