package com.axelw578.loginprueba.ui.home

import android.content.Context
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

    // Definimos una ruta raíz (por ejemplo, el almacenamiento principal)
    var rootPath: String = "/storage/emulated/0"

    init {
        loadRootFolders()
    }

    fun loadRootFolders(path: String = rootPath) {
        val folderFiles = fileRepository.listFilesAndDirectories(path).filter { it.isDirectory }
        val folderItems = folderFiles.map {
            FolderItem(name = it.name, path = it.absolutePath)
        }
        _folders.value = folderItems
    }

    fun getFilesFromPath(path: String): List<File> {
        return fileRepository.listFilesAndDirectories(path).filter { it.isFile }
    }

    fun getFilteredFiles(path: String, extensions: List<String>): List<File> {
        return fileRepository.listFilesByExtensions(path, extensions)
    }

    fun getMimeType(file: File): String? {
        return fileRepository.getMimeType(file)
    }

    fun openFile(context: Context, file: File) {
        fileRepository.getOpenFileIntent(file)?.let {
            context.startActivity(it)
        }
    }

    fun reloadFiles() {
        loadRootFolders(rootPath)
    }
}
