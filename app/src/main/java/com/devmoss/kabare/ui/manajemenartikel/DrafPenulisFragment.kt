    package com.devmoss.kabare.ui.manajemenartikel

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.viewModels
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.devmoss.kabare.R
    import com.devmoss.kabare.databinding.FragmentDrafPenulisBinding
    import com.devmoss.kabare.ui.manajemenartikel.artikeladapters.DrafPenulisAdapter
    import com.devmoss.kabare.ui.manajemenartikel.artikelviewmodels.DrafPenulisViewModel

    class DrafPenulisFragment : Fragment(), DrafPenulisAdapter.OnItemClickListener {

        private var _binding: FragmentDrafPenulisBinding? = null
        private val binding get() = _binding!!
        private val viewModel: DrafPenulisViewModel by viewModels()
        private lateinit var adapter: DrafPenulisAdapter

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentDrafPenulisBinding.inflate(inflater, container, false)
            //navigasi ke fragment buat artikel
            binding.btnTambahArtikel.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_manajemen_artikel_to_buatArtikelFragment)
            }
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            setupRecyclerView()
            observeData()
        }


        private fun setupRecyclerView() {
            // Inisialisasi adapter dengan list kosong dan pasang listener
            adapter = DrafPenulisAdapter(emptyList(), this)
            binding.recyclerViewDrafPenulis.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewDrafPenulis.adapter = adapter
        }

        private fun observeData() {
            // Mengamati LiveData dari ViewModel
            viewModel.drafPenulisLiveData.observe(viewLifecycleOwner) { drafList ->
                // Perbarui data dalam adapter
                adapter.updateData(drafList)
            }
        }

        // Implementasi listener untuk event klik
        override fun onHapusClick(position: Int) {
            val drafArtikel = viewModel.drafPenulisLiveData.value?.get(position)
            drafArtikel?.let {
                // TODO: Hapus artikel
                Toast.makeText(requireContext(), "Hapus artikel: ${it.title}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onEditClick(position: Int) {
            val drafArtikel = viewModel.drafPenulisLiveData.value?.get(position)
            drafArtikel?.let {
                // TODO: Edit artikel
                Toast.makeText(requireContext(), "Edit artikel: ${it.title}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
