package com.example.wardrobegenerator.data.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageStorageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val wardrobeImagesDir: File
        get() = File(context.getExternalFilesDir(null), WARDROBE_DIR).apply {
            if (!exists()) mkdirs()
        }

    /**
     * Saves an image from a URI to app-specific storage
     * @param sourceUri The URI of the image to save
     * @param category Optional category prefix for filename organization
     * @return The URI string of the saved image
     * @throws FileNotFoundException if the source URI doesn't exist
     * @throws IOException if there's an error reading/writing the file
     */
    suspend fun saveImage(sourceUri: Uri, category: String? = null): String = withContext(Dispatchers.IO) {
        val inputStream = context.contentResolver.openInputStream(sourceUri)
            ?: throw FileNotFoundException("Cannot open input stream for URI: $sourceUri")

        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        val compressedBitmap = compressImage(bitmap)
        val filename = generateFilename(category)
        val file = File(wardrobeImagesDir, filename)

        FileOutputStream(file).use { outputStream ->
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream)
        }

        bitmap.recycle()
        compressedBitmap.recycle()

        file.absolutePath
    }

    /**
     * Saves a bitmap directly to storage
     * @param bitmap The bitmap to save
     * @param category Optional category prefix for filename organization
     * @return The URI string of the saved image
     * @throws IOException if there's an error writing the file
     */
    suspend fun saveBitmap(bitmap: Bitmap, category: String? = null): String = withContext(Dispatchers.IO) {
        val compressedBitmap = compressImage(bitmap)
        val filename = generateFilename(category)
        val file = File(wardrobeImagesDir, filename)

        FileOutputStream(file).use { outputStream ->
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream)
        }

        compressedBitmap.recycle()

        file.absolutePath
    }

    /**
     * Deletes an image file
     * @param imageUri The URI string of the image to delete
     * @return true if the file was deleted, false if it didn't exist
     * @throws SecurityException if we don't have permission to delete the file
     */
    suspend fun deleteImage(imageUri: String): Boolean = withContext(Dispatchers.IO) {
        val file = File(imageUri)
        if (!file.exists()) {
            return@withContext false
        }
        file.delete()
    }

    /**
     * Gets a content URI for an image file (needed for sharing, etc.)
     * @throws IllegalArgumentException if the file doesn't exist
     */
    fun getContentUri(imageUri: String): Uri {
        val file = File(imageUri)
        require(file.exists()) { "Image file does not exist: $imageUri" }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    /**
     * Compresses the bitmap to a max size while maintaining aspect ratio
     */
    private fun compressImage(bitmap: Bitmap): Bitmap {
        val maxSize = MAX_IMAGE_SIZE
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxSize && height <= maxSize) {
            return bitmap
        }

        val scaleFactor = if (width > height) {
            maxSize.toFloat() / width
        } else {
            maxSize.toFloat() / height
        }

        val newWidth = (width * scaleFactor).toInt()
        val newHeight = (height * scaleFactor).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    /**
     * Generates a unique filename with optional category prefix
     */
    private fun generateFilename(category: String?): String {
        val timestamp = System.currentTimeMillis()
        val uuid = UUID.randomUUID().toString().substring(0, 8)
        val categoryPrefix = category?.lowercase() ?: "item"
        return "${categoryPrefix}_${uuid}_$timestamp.jpg"
    }

    /**
     * Gets the total storage size used by wardrobe images in bytes
     */
    suspend fun getStorageUsed(): Long = withContext(Dispatchers.IO) {
        wardrobeImagesDir.walkTopDown()
            .filter { it.isFile }
            .map { it.length() }
            .sum()
    }

    companion object {
        private const val WARDROBE_DIR = "wardrobe"
        private const val MAX_IMAGE_SIZE = 1024 // Max width/height in pixels
        private const val IMAGE_QUALITY = 80 // JPEG quality 0-100
    }
}
