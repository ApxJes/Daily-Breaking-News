package com.example.retrofittutorial.ui.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.retrofittutorial.R
import com.example.retrofittutorial.databinding.ActivityNewsBinding
import com.example.retrofittutorial.ui.adapter.NewsAdapter
import com.example.retrofittutorial.ui.db.ArticleDatabase
import com.example.retrofittutorial.ui.repository.NewsRepository
import com.example.retrofittutorial.ui.viewModel.NewsViewModel
import com.example.retrofittutorial.ui.viewModel.NewsViewModelFactory


class NewsActivity : AppCompatActivity() {

    private var _binding: ActivityNewsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelFactory = NewsViewModelFactory(newsRepository, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}