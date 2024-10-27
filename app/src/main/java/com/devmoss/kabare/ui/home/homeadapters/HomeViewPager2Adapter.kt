package com.devmoss.kabare.ui.home.homeadapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devmoss.kabare.ui.home.HomePopulerFragment
import com.devmoss.kabare.ui.home.HomeTerbaruFragment

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