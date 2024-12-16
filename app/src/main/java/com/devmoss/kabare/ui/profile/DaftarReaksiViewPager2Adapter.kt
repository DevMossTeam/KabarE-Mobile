package com.devmoss.kabare.ui.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devmoss.kabare.ui.reaksi.DaftarBeritaBookmarkFragment

class DaftarReaksiViewPager2Adapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList = listOf(
        DaftarBeritaBookmarkFragment(),
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}