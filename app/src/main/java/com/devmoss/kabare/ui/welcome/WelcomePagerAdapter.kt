package com.devmoss.kabare.ui.welcome

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class WelcomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3 // Assuming there are 3 fragments for welcome pages
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WelcomePage1Fragment()
            1 -> WelcomePage2Fragment()
            2 -> WelcomePage3Fragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
