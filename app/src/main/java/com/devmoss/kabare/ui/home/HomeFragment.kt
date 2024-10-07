package com.devmoss.kabare.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.ui.home.adapters.ArticleAdapter
import com.devmoss.kabare.ui.home.viewmodels.HomeViewModel
import com.devmoss.kabare.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // Set up RecyclerView
        articleAdapter = ArticleAdapter {
            // Handle bookmark click
            homeViewModel.toggleBookmark(it)
        }
        binding.rvHome.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
        }

        // Observe article data
        homeViewModel.sortedArticles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.submitList(articles)
        }

        // Load data
        homeViewModel.loadArticles()
    }
}
