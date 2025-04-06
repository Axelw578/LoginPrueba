package com.axelw578.loginprueba.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.axelw578.loginprueba.data.model.FolderItem
import com.axelw578.loginprueba.databinding.ItemFolderBinding

class FileAdapter(
    private val context: android.content.Context,
    private val folders: List<FolderItem>,
    private val onFolderClick: (FolderItem) -> Unit
) : RecyclerView.Adapter<FileAdapter.FolderViewHolder>() {

    inner class FolderViewHolder(val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folders[position]
        holder.binding.tvFolderName.text = folder.name
        holder.binding.root.setOnClickListener {
            onFolderClick(folder)
        }
    }

    override fun getItemCount(): Int = folders.size
}
