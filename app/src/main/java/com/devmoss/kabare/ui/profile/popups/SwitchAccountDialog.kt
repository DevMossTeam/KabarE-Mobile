package com.devmoss.kabare.ui.auth.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.devmoss.kabare.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SwitchAccountDialog : BottomSheetDialogFragment() {

    // Container for account items
    private lateinit var account1Container: LinearLayout
    private lateinit var account2Container: LinearLayout
    private lateinit var account3Container: LinearLayout
    private lateinit var addAccountTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this dialog
        val view = inflater.inflate(R.layout.dialog_switch_account, container, false)

        // Initialize views
        account1Container = view.findViewById(R.id.account1_container)
        account2Container = view.findViewById(R.id.account2_container)
        account3Container = view.findViewById(R.id.account3_container)
        addAccountTextView = view.findViewById(R.id.text_add_account)

        // Populate account list
        populateAccountList()

        // Set click listener for "Tambah Akun"
        addAccountTextView.setOnClickListener {
            // Handle add account action
        }

        return view
    }

    private fun populateAccountList() {
        // Sample accounts data (You can replace this with actual data)
        val accounts = listOf(
            Account("Nama Lengkap 1", "Penulis", R.drawable.ic_akun),
            Account("Nama Lengkap 2", "Editor", R.drawable.ic_akun),
            Account("Nama Lengkap 3", "Pembaca", R.drawable.ic_akun)
        )

        // Bind data to each account view
        if (accounts.isNotEmpty()) {
            setAccountData(account1Container, accounts[0])
        }
        if (accounts.size > 1) {
            setAccountData(account2Container, accounts[1])
        }
        if (accounts.size > 2) {
            setAccountData(account3Container, accounts[2])
        }
    }

    private fun setAccountData(container: LinearLayout, account: Account) {
        // Get child views based on the container passed
        val profilePic: ImageView = container.findViewById(
            when (container.id) {
                R.id.account1_container -> R.id.account1_profile_picture
                R.id.account2_container -> R.id.account2_profile_picture
                R.id.account3_container -> R.id.account3_profile_picture
                else -> throw IllegalArgumentException("Invalid container ID")
            }
        )

        val accountName: TextView = container.findViewById(
            when (container.id) {
                R.id.account1_container -> R.id.account1_name
                R.id.account2_container -> R.id.account2_name
                R.id.account3_container -> R.id.account3_name
                else -> throw IllegalArgumentException("Invalid container ID")
            }
        )

        val accountStatus: TextView = container.findViewById(
            when (container.id) {
                R.id.account1_container -> R.id.account1_status
                R.id.account2_container -> R.id.account2_status
                R.id.account3_container -> R.id.account3_status
                else -> throw IllegalArgumentException("Invalid container ID")
            }
        )

        // Set data to views
        profilePic.setImageResource(account.profilePic)
        accountName.text = account.name
        accountStatus.text = account.status

        // Set click listener for each account
        container.setOnClickListener {
            // Handle account switch action
        }
    }

    data class Account(val name: String, val status: String, val profilePic: Int)
}
