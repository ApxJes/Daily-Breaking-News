package com.example.retrofittutorial.ui.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.example.retrofittutorial.R
import com.example.retrofittutorial.databinding.FragmentSaveNewsBinding
import com.example.retrofittutorial.ui.adapter.NewsAdapter
import com.example.retrofittutorial.ui.ui.NewsActivity
import com.example.retrofittutorial.ui.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SaveNewsFragment : Fragment(R.layout.fragment_save_news) {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentSaveNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSaveNewsBinding.bind(view)
        newsViewModel = (activity as NewsActivity).newsViewModel

        newsAdapter = NewsAdapter()
        binding.rcvSaveNews.adapter = newsAdapter
        binding.rcvSaveNews.layoutManager = LinearLayoutManager(activity)

        newsAdapter.setOnItemClickListener {
            val action = SaveNewsFragmentDirections.actionSaveNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }

        val itemTouchHelperCallBack = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                newsViewModel.deleteNews(article)
                Snackbar.make(view, "Successfully delete", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        newsViewModel.saveNews(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.rcvSaveNews)
        }

        newsViewModel.getSaveNews().observe(viewLifecycleOwner) {article ->
            newsAdapter.differ.submitList(article)
        }
    }
}