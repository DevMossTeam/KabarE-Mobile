package com.devmoss.kabare.ui.manajemenartikel.manajemenreviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.devmoss.kabare.databinding.FragmentManajemenReviewerBinding
import com.devmoss.kabare.ui.manajemenartikel.manajemenreviewer.revieweradapter.ViewPagerAdapterReviewer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ManajemenReviewerFragment : Fragment() {
    private var _binding: FragmentManajemenReviewerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout dengan ViewBinding
        _binding = FragmentManajemenReviewerBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout
        val adapter = ViewPagerAdapterReviewer(this)
        viewPager.adapter = adapter

        // Hubungkan TabLayout dengan ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Dalam Peninjauan"
                1 -> tab.text = "Published"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Set binding ke null saat view dihancurkan untuk menghindari memory leaks
        _binding = null
    }
}
