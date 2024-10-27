package com.devmoss.kabare.ui.manajemenartikel.artikeladapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devmoss.kabare.ui.manajemenartikel.DalamPeninjauanFragment
import com.devmoss.kabare.ui.manajemenartikel.DrafPenulisFragment
import com.devmoss.kabare.ui.manajemenartikel.PublishedArtikelFragment

class ViewPagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList = listOf(
        DrafPenulisFragment(),
        DalamPeninjauanFragment(),
        PublishedArtikelFragment()
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
