package com.example.retrofittutorial.ui.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofittutorial.R
import com.example.retrofittutorial.databinding.FragmentSearchNewsBinding
import com.example.retrofittutorial.ui.adapter.NewsAdapter
import com.example.retrofittutorial.ui.ui.NewsActivity
import com.example.retrofittutorial.ui.util.Constants.Companion.DELAY_TIME_FOR_SEARCH_NEWS
import com.example.retrofittutorial.ui.util.Resource
import com.example.retrofittutorial.ui.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentSearchNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchNewsBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel
        newsAdapter = NewsAdapter()
        binding.rcvSearchNews.adapter = newsAdapter
        binding.rcvSearchNews.layoutManager = LinearLayoutManager(activity)

        newsAdapter.setOnItemClickListener {
            val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }

        var job: Job? = null
        binding.edtSearchNews.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(DELAY_TIME_FOR_SEARCH_NEWS)
                it?.let {
                    if(it.toString().isNotEmpty()){
                        newsViewModel.getSearchNews(it.toString())
                    }
                }
            }

        }

        newsViewModel.searchNews.observe(viewLifecycleOwner){response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { resultResponse ->
                        newsAdapter.differ.submitList(resultResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.data?.let { errorMessage ->
                        Snackbar.make(
                            view,
                            "An error occur $errorMessage",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.paginationProgress.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.paginationProgress.visibility = View.GONE
    }
}