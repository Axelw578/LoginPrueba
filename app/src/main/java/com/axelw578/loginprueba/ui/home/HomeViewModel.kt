package com.axelw578.loginprueba.ui.home

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axelw578.loginprueba.data.model.FolderItem
import com.axelw578.loginprueba.data.repository.SafRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val safRepository: SafRepository
) : ViewModel() {

    // LiveData para mostrar la lista actual de archivos/carpetas (DocumentFile) en el directorio actual.
    private val _files = MutableLiveData<List<DocumentFile>>()
    val files: LiveData<List<DocumentFile>> = _files

    // Mantiene el historial de navegación con una pila de URIs.
    private val navigationStack = ArrayDeque<Uri>()

    fun setCurrentDirectory(uri: Uri) {
        navigationStack.clear()
        navigationStack.add(uri)
        loadFiles(uri)
    }

    fun reloadFiles() {
        navigationStack.lastOrNull()?.let { uri ->
            loadFiles(uri)
        }
    }

    private fun loadFiles(uri: Uri) {
        viewModelScope.launch {
            val list = safRepository.listFiles(uri)
            println("Cargando archivos en: $uri, elementos encontrados: ${list.size}")
            _files.postValue(list)
        }
    }


    fun navigateTo(folder: DocumentFile) {
        if (folder.isDirectory) {
            folder.uri?.let { uri ->
                navigationStack.add(uri)
                loadFiles(uri)
            }
        }
    }

    fun navigateBack(): Boolean {
        return if (navigationStack.size > 1) {
            navigationStack.removeLast()
            val uri = navigationStack.last()
            loadFiles(uri)
            true
        } else {
            false
        }
    }

    fun openFile(context: Context, file: DocumentFile) {
        safRepository.openFile(context, file)
    }

    fun renameFile(file: DocumentFile, newName: String): Boolean {
        val renamed = safRepository.renameFile(file, newName)
        if (renamed) reloadFiles()
        return renamed
    }

    fun deleteFile(file: DocumentFile): Boolean {
        val deleted = safRepository.deleteFile(file)
        if (deleted) reloadFiles()
        return deleted
    }

    // Función para obtener la raíz actual (si se desea mostrar en la UI, por ejemplo en un breadcrumb)
    fun getCurrentPath(): String {
        return navigationStack.lastOrNull()?.toString() ?: ""
    }

    // (Opcional) Función de búsqueda en el directorio actual por nombre
    fun searchFiles(query: String): List<DocumentFile> {
        return _files.value?.filter { it.name?.contains(query, ignoreCase = true) == true } ?: emptyList()
    }
}
