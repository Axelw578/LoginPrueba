package com.axelw578.loginprueba.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.axelw578.loginprueba.data.model.FolderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _folders = MutableLiveData<List<FolderItem>>().apply {
        value = listOf(
            FolderItem(name = "Personal", path = "/cloud/Personal"),
            FolderItem(name = "Trabajo", path = "/cloud/Trabajo"),
            FolderItem(name = "Favoritos", path = "/cloud/Favoritos"),
            FolderItem(name = "+", path = "/cloud/+")  // Opción para crear una nueva carpeta
        )
    }
    val folders: LiveData<List<FolderItem>> = _folders

    // Aquí podrías agregar métodos para crear, renombrar o eliminar carpetas, integrando Firebase
    fun createFolder(newFolder: FolderItem) {
        // Lógica para crear carpeta en la base de datos, por ejemplo, en Firestore
        val updatedList = _folders.value?.toMutableList() ?: mutableListOf()
        updatedList.add(newFolder)
        _folders.value = updatedList
    }
}
