package com.axelw578.loginprueba.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.axelw578.loginprueba.data.model.FolderItem
import com.axelw578.loginprueba.databinding.ItemFolderBinding

class FolderAdapter(
    private val folders: List<FolderItem>,
    private val onItemClick: (FolderItem) -> Unit
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    inner class FolderViewHolder(val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(folder: FolderItem) {
            binding.tvFolderName.text = folder.name
            // Aquí podrías cambiar el ícono en función de la carpeta, por ejemplo:
            // binding.ivFolderIcon.setImageResource(if(folder.name == "+") R.drawable.ic_add else R.drawable.ic_folder_cloud)
            binding.root.setOnClickListener { onItemClick(folder) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.bind(folders[position])
    }

    override fun getItemCount(): Int = folders.size
}
