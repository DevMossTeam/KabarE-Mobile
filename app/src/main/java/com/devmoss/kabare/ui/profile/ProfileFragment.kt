package com.devmoss.kabare.ui.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.devmoss.kabare.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Redirect to Sign In Fragment after 1 second
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_navigation_profil_to_signInFragment)
        }, 1000) // 1000 milliseconds = 1 second
    }
}
