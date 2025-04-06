package com.axelw578.loginprueba.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Lista todos los archivos y directorios de un path usando java.io.File
    fun listFilesAndDirectories(path: String): List<File> {
        val directory = File(path)
        return if (directory.exists() && directory.isDirectory) {
            directory.listFiles()?.toList() ?: emptyList()
        } else {
            emptyList()
        }
    }

    // Filtra archivos por extensiones
    fun listFilesByExtensions(path: String, extensions: List<String>): List<File> {
        val directory = File(path)
        return if (directory.exists() && directory.isDirectory) {
            directory.listFiles()?.filter { file ->
                file.isFile && extensions.any { file.name.endsWith(it, ignoreCase = true) }
            } ?: emptyList()
        } else {
            emptyList()
        }
    }

    // Obtiene el MIME type basado en la extensión
    fun getMimeType(file: File): String? {
        return android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
    }

    // Crea un Intent para abrir un archivo con FileProvider
    fun getOpenFileIntent(file: File): Intent? {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            file
        )
        val mimeType = getMimeType(file) ?: "application/octet-stream"
        return Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    // Eliminar un archivo
    fun deleteFile(file: File): Boolean {
        return file.delete()
    }

    // Renombrar un archivo
    fun renameFile(file: File, newName: String): Boolean {
        val newFile = File(file.parent, newName)
        return file.renameTo(newFile)
    }
}
