package com.axelw578.loginprueba.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.axelw578.loginprueba.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var folderAdapter: FolderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar el RecyclerView
        binding.rvFolders.layoutManager = LinearLayoutManager(requireContext())
        folderAdapter = FolderAdapter(emptyList()) { folder ->
            // Aquí maneja el clic en la carpeta. Por ejemplo, si folder.name es "+", abrir diálogo para crear una carpeta.
        }
        binding.rvFolders.adapter = folderAdapter

        // Observar el LiveData de carpetas
        viewModel.folders.observe(viewLifecycleOwner) { folders ->
            folderAdapter = FolderAdapter(folders) { folder ->
                // Manejar clic en carpeta
            }
            binding.rvFolders.adapter = folderAdapter
        }
    }
}
