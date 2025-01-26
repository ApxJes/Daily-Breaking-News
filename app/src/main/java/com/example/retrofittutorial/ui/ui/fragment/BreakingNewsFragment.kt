package com.example.retrofittutorial.ui.ui.fragment
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofittutorial.R
import com.example.retrofittutorial.databinding.FragmentBreakingNewsBinding
import com.example.retrofittutorial.ui.adapter.NewsAdapter
import com.example.retrofittutorial.ui.ui.NewsActivity
import com.example.retrofittutorial.ui.util.Resource
import com.example.retrofittutorial.ui.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private lateinit var binding: FragmentBreakingNewsBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)
        newsViewModel = (activity as NewsActivity).newsViewModel

        newsAdapter = NewsAdapter()
        binding.rcvBreakingNews.adapter = newsAdapter
        binding.rcvBreakingNews.layoutManager = LinearLayoutManager(activity)

        newsAdapter.setOnItemClickListener { article ->
            val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article)
            findNavController().navigate(action)
        }

        newsViewModel.breakingNews.observe(viewLifecycleOwner) {respone ->
            when(respone) {
                is Resource.Success -> {
                    hideProgressBar()
                    respone.data?.let { resultResponse ->
                        newsAdapter.differ.submitList(resultResponse.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    respone.data?.let { errorMessage ->
                        Snackbar.make(
                            view,
                            "An error occur $errorMessage",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Loading -> showProgressBar()
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