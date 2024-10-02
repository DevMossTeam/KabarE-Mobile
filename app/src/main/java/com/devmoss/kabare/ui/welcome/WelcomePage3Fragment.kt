package com.devmoss.kabare.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import kotlinx.android.synthetic.main.fragment_welcome_page_3.*

class WelcomePage3Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_page_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view)

        btn_next.setOnClickListener {
            // Navigasi ke halaman selanjutnya, misalnya ke HomeFragment
            findNavController().navigate(R.id.action_welcomePage3Fragment_to_homeFragment)
        }
    }
}
