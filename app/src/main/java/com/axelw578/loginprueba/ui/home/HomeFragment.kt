package com.axelw578.loginprueba.ui.home

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.axelw578.loginprueba.databinding.FragmentHomeBinding
import com.axelw578.loginprueba.ui.home.adapter.FileAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var fileAdapter: FileAdapter

    // Solicitar permiso para acceder al almacenamiento
    private val permissionLauncher = registerForActivityResult(RequestPermission()) { granted ->
        if (granted) {
            viewModel.reloadFiles()
        } else {
            Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }
    

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFolders.layoutManager = LinearLayoutManager(requireContext())

        // Solicitar permisos (si es necesario)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // En Android 11+, si usas MANAGE_EXTERNAL_STORAGE, deberás manejarlo según tu lógica
            viewModel.reloadFiles()
        } else {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        viewModel.folders.observe(viewLifecycleOwner) { folderList ->
            fileAdapter = FileAdapter(requireContext(), folderList, onFolderClick = { folder ->
                // Al hacer clic en una carpeta, mostramos archivos filtrados
                viewModel.loadRootFolders(folder.path)
                val extensions = listOf(".pdf", ".txt", ".png", ".jpg", ".mp4")
                val files = viewModel.getFilteredFiles(folder.path, extensions)
                if (files.isNotEmpty()) {
                    // Abrir el primer archivo como ejemplo
                    viewModel.openFile(requireContext(), files.first())
                } else {
                    Toast.makeText(requireContext(), "No se encontraron archivos en ${folder.name}", Toast.LENGTH_SHORT).show()
                }
            })
            binding.rvFolders.adapter = fileAdapter
        }
    }
}
