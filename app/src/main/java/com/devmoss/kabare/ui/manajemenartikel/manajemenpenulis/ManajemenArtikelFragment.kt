package com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.devmoss.kabare.databinding.FragmentManajemenArtikelBinding
import com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis.artikeladapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ManajemenArtikelFragment : Fragment() {

    private var _binding: FragmentManajemenArtikelBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout dengan ViewBinding
        _binding = FragmentManajemenArtikelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Hubungkan TabLayout dengan ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Draf Penulis"
                1 -> tab.text = "Dalam Peninjauan"
                2 -> tab.text = "Published"
            }
        }.attach()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        // Set binding ke null saat view dihancurkan untuk menghindari memory leaks
        _binding = null
    }
}
