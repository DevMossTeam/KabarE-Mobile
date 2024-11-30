package com.devmoss.kabare.ui.manajemenartikel.manajemenreviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmoss.kabare.databinding.FragmentDalamPeninjauanReviewerBinding
import com.devmoss.kabare.ui.manajemenartikel.manajemenreviewer.revieweradapter.DalamPeninjauanReviewerAdapter
import com.devmoss.kabare.ui.manajemenartikel.manajemenreviewer.reviewerviewmodel.DalamPeninjauanReviewerViewModel


class DalamPeninjauanReviewerFragment : Fragment(), DalamPeninjauanReviewerAdapter.OnItemClickListener{
    private var _binding: FragmentDalamPeninjauanReviewerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DalamPeninjauanReviewerViewModel by viewModels()
    private lateinit var adapter: DalamPeninjauanReviewerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDalamPeninjauanReviewerBinding.inflate(inflater, container, false)
        return binding.root
       }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = DalamPeninjauanReviewerAdapter(emptyList(), this) // Menggunakan 'this' sebagai listener
        binding.rvDalamPeninjauanReviewer.layoutManager = LinearLayoutManager(context)
        binding.rvDalamPeninjauanReviewer.adapter = adapter
    }

    private fun observeData() {
        // Di sini Anda bisa mengamati data dari ViewModel dan memperbarui adapter
        // Misalnya:
        viewModel.dalamPeninjauanReviewerLiveData.observe(viewLifecycleOwner) { dataList ->
            adapter = DalamPeninjauanReviewerAdapter(dataList, this) // Update adapter dengan data baru
            binding.rvDalamPeninjauanReviewer.adapter = adapter
        }
    }

    override fun onReviewClick(position: Int){
        Toast.makeText(requireContext(), "Review item clicked at position $position", Toast.LENGTH_SHORT).show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}