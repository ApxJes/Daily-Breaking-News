package com.example.retrofittutorial.ui.repository

import com.example.retrofittutorial.ui.db.ArticleDatabase
import com.example.retrofittutorial.ui.model.NewsResponse
import retrofit2.Response
import retrofit2.Retrofit

class NewsRepository(
    val articleDatabase: ArticleDatabase
) {


}