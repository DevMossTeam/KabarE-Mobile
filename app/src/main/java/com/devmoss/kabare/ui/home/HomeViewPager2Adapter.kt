    package com.devmoss.kabare.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devmoss.kabare.ui.home.populer.HomePopulerFragment
import com.devmoss.kabare.ui.home.terbaru.HomeTerbaruFragment

class HomeViewPager2Adapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList = listOf(
        HomeTerbaruFragment(),
        HomePopulerFragment()
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}