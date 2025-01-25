package com.example.retrofittutorial.ui.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofittutorial.R
import com.example.retrofittutorial.databinding.FragmentSearchNewsBinding
import com.example.retrofittutorial.ui.ui.NewsActivity
import com.example.retrofittutorial.ui.util.Constants.Companion.DELAY_TIME_FOR_SEARCH_NEWS
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

}