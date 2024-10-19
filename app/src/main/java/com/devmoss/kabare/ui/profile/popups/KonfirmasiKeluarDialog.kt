package com.devmoss.kabare.ui.auth.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.devmoss.kabare.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class KonfirmasiKeluarDialog(
    private val onConfirm: () -> Unit, // Callback ketika tombol "Ya" ditekan
    private val onCancel: () -> Unit    // Callback ketika tombol "Tidak" ditekan
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout XML untuk dialog
        val view = inflater.inflate(R.layout.dialog_konfirmasi_keluar, container, false)

        // Inisialisasi tombol dan teks
        val confirmButton: Button = view.findViewById(R.id.button_ya)
        val cancelButton: TextView = view.findViewById(R.id.button_tidak)

        // Set aksi ketika tombol "Ya" ditekan
        confirmButton.setOnClickListener {
            onConfirm() // Panggil fungsi callback ketika tombol "Ya" ditekan
            dismiss()   // Tutup dialog
        }

        // Set aksi ketika tombol "Tidak" ditekan
        cancelButton.setOnClickListener {
            onCancel() // Panggil fungsi callback ketika tombol "Tidak" ditekan
            dismiss()  // Tutup dialog
        }

        return view
    }
}
