package com.devmoss.kabare.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R
import kotlinx.android.synthetic.main.fragment_welcome_page_2.*

class WelcomePage2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_page_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view)

        btn_next.setOnClickListener {
            findNavController().navigate(R.id.action_welcomePage2Fragment_to_welcomePage3Fragment)
        }
    }
}
