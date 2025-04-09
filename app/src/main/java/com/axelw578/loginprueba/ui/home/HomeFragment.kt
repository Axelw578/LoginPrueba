package com.axelw578.loginprueba.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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
    private lateinit var adapter: FileAdapter

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            viewModel.reloadFiles()
        } else {
            Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }


    // Launcher para seleccionar carpeta raíz mediante SAF
    private val folderPickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        uri?.let {
            requireContext().contentResolver.takePersistableUriPermission(
                it, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            viewModel.setCurrentDirectory(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        binding.rvFiles.layoutManager = LinearLayoutManager(requireContext())
        adapter = FileAdapter(
            context = requireContext(),
            files = emptyList(),
            onClick = { file ->
                if (file.isDirectory) {
                    viewModel.navigateTo(file)
                } else {
                    viewModel.openFile(requireContext(), file)
                }
            },
            onRename = { file, newName ->
                if (!viewModel.renameFile(file, newName)) {
                    Toast.makeText(requireContext(), "No se pudo renombrar", Toast.LENGTH_SHORT).show()
                }
            },
            onDelete = { file ->
                if (!viewModel.deleteFile(file)) {
                    Toast.makeText(requireContext(), "No se pudo eliminar", Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.rvFiles.adapter = adapter

        // Botón para seleccionar la carpeta raíz
        binding.btnPickFolder.setOnClickListener {
            folderPickerLauncher.launch(null)
        }

        // Botón para retroceder en la jerarquía
        binding.btnBack.setOnClickListener {
            if (!viewModel.navigateBack()) {
                Toast.makeText(requireContext(), "Ya estás en el directorio raíz", Toast.LENGTH_SHORT).show()
            }
        }

        // Actualizar lista cuando se carguen archivos
        viewModel.files.observe(viewLifecycleOwner) { fileList ->
            adapter.submitList(fileList)
            // Opcional: actualiza una vista breadcrumb o TextView con la ruta actual
            binding.tvCurrentPath.text = viewModel.getCurrentPath()
        }

        // Si no se ha seleccionado ninguna carpeta, se puede solicitar permiso de MANAGE_EXTERNAL_STORAGE (opcional)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!android.os.Environment.isExternalStorageManager()) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Permiso requerido")
                    .setMessage("Para acceder a todos los archivos, activa el permiso 'Manage External Storage' en Configuración.")
                    .setPositiveButton("Aceptar") { _, _ ->
                        // Aquí asignamos el URI con el package de tu app
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        intent.data = Uri.parse("package:" + requireContext().packageName)
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            } else {
                viewModel.reloadFiles()
            }
        } else {
            // Para versiones menores a Android 11 se usa el permission launcher, si lo defines.
            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }


    }

    private fun showRenameDialog(fileUri: Uri, onRenameConfirmed: (String) -> Unit) {
        val editText = android.widget.EditText(requireContext())
        AlertDialog.Builder(requireContext())
            .setTitle("Renombrar archivo")
            .setView(editText)
            .setPositiveButton("Aceptar") { _, _ ->
                val newName = editText.text.toString()
                if (newName.isNotBlank()) onRenameConfirmed(newName)
                else Toast.makeText(requireContext(), "Nombre no válido", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
