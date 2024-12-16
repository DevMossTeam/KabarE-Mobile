package com.devmoss.kabare.ui.manajemenartikel.manajemenreviewer.revieweradapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devmoss.kabare.ui.manajemenartikel.manajemenpenulis.PublishedArtikelFragment
import com.devmoss.kabare.ui.manajemenartikel.manajemenreviewer.DalamPeninjauanReviewerFragment

class ViewPagerAdapterReviewer(fragment : Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList = listOf(
        DalamPeninjauanReviewerFragment(),
        PublishedArtikelFragment()
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
