package com.example.wardrobegenerator.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.UUID

object CameraTempFileManager {
    private const val TEMP_IMAGE_DIR = "temp_captures"
    private const val MAX_TEMP_FILE_AGE_MS = 24 * 60 * 60 * 1000L // 24 hours

    /**
     * Creates a temporary file for camera capture using FileProvider for security.
     * Returns a content:// URI instead of file:// URI.
     */
    fun createTempImageFile(context: Context): Pair<File, Uri> {
        val tempDir = File(context.cacheDir, TEMP_IMAGE_DIR).apply {
            if (!exists()) {
                mkdirs()
            }
        }

        // Use UUID instead of timestamp for better uniqueness and unpredictability
        val fileName = "capture_${UUID.randomUUID()}.jpg"
        val file = File(tempDir, fileName)

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        return Pair(file, uri)
    }

    /**
     * Cleans up old temporary image files to prevent cache bloat.
     * Call this periodically or when the app starts.
     */
    fun cleanupOldTempFiles(context: Context) {
        val tempDir = File(context.cacheDir, TEMP_IMAGE_DIR)
        if (!tempDir.exists()) return

        val now = System.currentTimeMillis()
        tempDir.listFiles()?.forEach { file ->
            if (now - file.lastModified() > MAX_TEMP_FILE_AGE_MS) {
                file.delete()
            }
        }
    }

    /**
     * Deletes a specific temporary file.
     */
    fun deleteTempFile(file: File) {
        if (file.exists() && file.path.contains(TEMP_IMAGE_DIR)) {
            file.delete()
        }
    }

    /**
     * Deletes all temporary files immediately.
     */
    fun deleteAllTempFiles(context: Context) {
        val tempDir = File(context.cacheDir, TEMP_IMAGE_DIR)
        tempDir.deleteRecursively()
    }
}
