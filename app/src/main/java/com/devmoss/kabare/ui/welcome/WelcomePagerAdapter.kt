// WelcomePagerAdapter.kt
package devmoss.kabare.ui.welcome

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class WelcomePagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3 // Jumlah halaman
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WelcomePage1Fragment()
            1 -> WelcomePage2Fragment()
            2 -> WelcomePage3Fragment()
            else -> WelcomePage1Fragment()
        }
    }
}
