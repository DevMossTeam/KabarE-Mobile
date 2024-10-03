package com.devmoss.kabare.ui.welcome

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class WelcomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return NUM_PAGES // Use a constant to define the number of pages
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WelcomePage1Fragment() // Welcome page 1
            1 -> WelcomePage2Fragment() // Welcome page 2
            2 -> WelcomePage3Fragment() // Welcome page 3
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }

    companion object {
        private const val NUM_PAGES = 3 // Define the number of pages
    }
}
