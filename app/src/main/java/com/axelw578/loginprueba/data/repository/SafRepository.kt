package com.axelw578.loginprueba.data.repository

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SafRepository @Inject constructor(
    @ApplicationContext val context: Context
) {
    // Lista todos los archivos y carpetas de un URI usando SAF
    fun listFiles(uri: Uri): List<DocumentFile> {
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        return documentFile?.listFiles()?.toList() ?: emptyList()
    }

    // Obtener tipo MIME de un archivo SAF
    fun getMimeType(file: DocumentFile): String {
        return file.type ?: "application/octet-stream"
    }

    // Crea un Intent para abrir un archivo
    fun openFile(context: Context, file: DocumentFile) {
        val mimeType = getMimeType(file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(file.uri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    // Renombrar un archivo
    fun renameFile(file: DocumentFile, newName: String): Boolean {
        return file.renameTo(newName)
    }

    // Eliminar un archivo
    fun deleteFile(file: DocumentFile): Boolean {
        return file.delete()
    }

    // Filtrar archivos de una lista por extensiones (por ejemplo, .pdf, .txt, .png, .mp4, etc.)
    fun filterFilesByExtensions(files: List<DocumentFile>, extensions: List<String>): List<DocumentFile> {
        return files.filter { file ->
            file.isFile && extensions.any { ext ->
                file.name?.lowercase()?.endsWith(ext.lowercase()) == true
            }
        }
    }
}
