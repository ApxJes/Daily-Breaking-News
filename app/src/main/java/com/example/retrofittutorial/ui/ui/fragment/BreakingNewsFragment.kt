package com.example.retrofittutorial.ui.ui.fragment
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentBreakingNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerView()

        newsAdapter.onItemClickListener {
            val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }

        viewModel.headlineNews.observe(viewLifecycleOwner){response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { resultResponse ->
                        newsAdapter.differ.submitList(resultResponse.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.data?.let { errorMessage ->
                        Toast.makeText(
                            activity,
                            "An error occur $errorMessage",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Loading -> showProgressBar()
            }
        }
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rcvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showProgressBar() {
        binding.paginationProgress.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.paginationProgress.visibility = View.INVISIBLE
    }
}