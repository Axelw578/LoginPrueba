package com.axelw578.loginprueba.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.axelw578.loginprueba.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

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

        binding.rvFolders.layoutManager = LinearLayoutManager(requireContext())

        viewModel.folders.observe(viewLifecycleOwner) { folderList ->
            folderAdapter = FolderAdapter(folderList) { folder ->
                viewModel.loadRootFolders(folder.path)

                // Filtrar por tipos comunes de archivos
                val extensions = listOf(".pdf", ".txt", ".png", ".jpg", ".mp4")
                val files = viewModel.getFilteredFiles(folder.path, extensions)

                if (files.isNotEmpty()) {
                    val file = files.first() // Solo abrimos el primero como ejemplo
                    val mimeType = viewModel.getMimeType(file) ?: "application/octet-stream"
                    val uri: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().packageName + ".provider",
                        file
                    )
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, mimeType)
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }
                    try {
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "No se puede abrir este archivo", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "No hay archivos filtrados en ${folder.name}", Toast.LENGTH_SHORT).show()
                }
            }
            binding.rvFolders.adapter = folderAdapter
        }
    }
}