package com.axelw578.loginprueba.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.RecyclerView
import com.axelw578.loginprueba.databinding.ItemFileBinding

class FileAdapter(
    private val context: Context,
    private var files: List<DocumentFile>,
    private val onClick: (DocumentFile) -> Unit,
    private val onRename: (DocumentFile, String) -> Unit,
    private val onDelete: (DocumentFile) -> Unit
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    inner class FileViewHolder(val binding: ItemFileBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = ItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileViewHolder(binding)
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        val name = file.name ?: "Sin nombre"

        holder.binding.tvFileName.text = name
        holder.binding.tvMimeType.text = file.type ?: "Desconocido"

        // Abrir el archivo o navegar a la carpeta
        holder.binding.btnOpen.setOnClickListener {
            onClick(file)
        }

        // Botón para eliminar el archivo
        holder.binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle("¿Eliminar archivo?")
                setMessage("¿Deseas eliminar $name?")
                setPositiveButton("Sí") { _, _ ->
                    onDelete(file)
                }
                setNegativeButton("Cancelar", null)
            }.show()
        }

        // Botón para renombrar el archivo
        holder.binding.btnRename.setOnClickListener {
            val editText = android.widget.EditText(context).apply {
                hint = "Nuevo nombre"
            }
            AlertDialog.Builder(context).apply {
                setTitle("Renombrar archivo")
                setView(editText)
                setPositiveButton("Aceptar") { _, _ ->
                    val newName = editText.text.toString()
                    if (newName.isNotBlank()) {
                        onRename(file, newName)
                    } else {
                        Toast.makeText(context, "Nombre inválido", Toast.LENGTH_SHORT).show()
                    }
                }
                setNegativeButton("Cancelar", null)
            }.show()
        }
    }

    // Método para actualizar la lista de archivos y refrescar el RecyclerView
    fun submitList(newFiles: List<DocumentFile>) {
        files = newFiles
        notifyDataSetChanged()
    }
}
