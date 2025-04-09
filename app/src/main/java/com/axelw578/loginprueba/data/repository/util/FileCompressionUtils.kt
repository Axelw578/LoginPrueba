package com.axelw578.loginprueba.data.repository.util

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.apache.commons.compress.archivers.zip.ZipFile
import java.io.File
import java.io.FileOutputStream

object FileCompressionUtils {

    // Comprime una lista de archivos en un archivo ZIP
    fun compressFiles(files: List<File>, destinationZip: File) {
        ZipArchiveOutputStream(FileOutputStream(destinationZip)).use { zipOut ->
            files.forEach { file ->
                if (file.isFile) {
                    zipOut.putArchiveEntry(ZipArchiveEntry(file, file.name))
                    file.inputStream().use { input ->
                        input.copyTo(zipOut)
                    }
                    zipOut.closeArchiveEntry()
                }
                // Si deseas soportar carpetas, deberás implementar lógica recursiva.
            }
            zipOut.finish()
        }
    }

    // Extrae un archivo ZIP en un directorio destino
    fun extractZip(zipFile: File, destinationDir: File) {
        // Asegúrate de que el directorio de destino exista.
        if (!destinationDir.exists()) {
            destinationDir.mkdirs()
        }
        ZipFile(zipFile).use { zip ->
            zip.entries.asSequence().forEach { entry ->
                val outFile = File(destinationDir, entry.name)
                if (entry.isDirectory) {
                    outFile.mkdirs()
                } else {
                    outFile.parentFile?.mkdirs()
                    zip.getInputStream(entry).use { input ->
                        FileOutputStream(outFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }
    }
}
