package com.example.retrofittutorial.ui.ui.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.retrofittutorial.R
import com.example.retrofittutorial.databinding.FragmentArticleBinding
import com.example.retrofittutorial.ui.adapter.NewsAdapter
import com.example.retrofittutorial.ui.ui.NewsActivity
import com.example.retrofittutorial.ui.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {
    lateinit var binding: FragmentArticleBinding
    lateinit var newsViewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel
        val article = args.article
        article.url?.let {
            binding.webView.apply {
                webViewClient = WebViewClient()
                loadUrl(it)
            }
        }

        binding.floatingBtnSaveNews.setOnClickListener {
            newsViewModel.saveNews(article)
            Snackbar.make(
                view,
                "Successfully save",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}