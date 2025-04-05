package com.axelw578.loginprueba.data.repository

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.net.URLConnection
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class FileRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Método para listar todos los archivos en un directorio usando java.io.File
    fun listFilesInDirectory(path: String): List<File> {
        val directory = File(path)
        return if (directory.exists() && directory.isDirectory) {
            directory.listFiles()?.toList() ?: emptyList()
        } else {
            emptyList()
        }
    }

    // Método para listar solo directorios en un camino dado
    fun listDirectoriesInPath(path: String): List<File> {
        val directory = File(path)
        return directory.listFiles()?.filter { it.isDirectory } ?: emptyList()
    }

    // Método para listar archivos y carpetas usando SAF (DocumentFile)
    fun listFilesWithDocumentFile(uri: Uri): List<DocumentFile> {
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        return documentFile?.listFiles()?.toList() ?: emptyList()
    }

    // Obtener el MIME type de un archivo (a partir de su nombre)
    fun getMimeType(file: File): String? {
        return URLConnection.guessContentTypeFromName(file.name)
    }

    // Listar archivos en un directorio filtrados por extensiones (por ejemplo, [".pdf", ".png", ".txt", ".mp4"])
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
}
