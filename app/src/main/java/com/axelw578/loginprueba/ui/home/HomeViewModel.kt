package com.axelw578.loginprueba.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.axelw578.loginprueba.data.model.FolderItem
import com.axelw578.loginprueba.data.repository.FileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {

    private val _folders = MutableLiveData<List<FolderItem>>()
    val folders: LiveData<List<FolderItem>> = _folders

    init {
        loadRootFolders()
    }

    fun loadRootFolders(path: String = "/storage/emulated/0") {
        val folderFiles = fileRepository.listDirectoriesInPath(path)
        val folderItems = folderFiles.map {
            FolderItem(name = it.name, path = it.absolutePath)
        }
        _folders.value = folderItems
    }

    fun getFilesFromPath(path: String): List<File> {
        return fileRepository.listFilesInDirectory(path)
    }

    fun getFilteredFiles(path: String, extensions: List<String>): List<File> {
        return fileRepository.listFilesByExtensions(path, extensions)
    }

    fun getMimeType(file: File): String? {
        return fileRepository.getMimeType(file)
    }
}
