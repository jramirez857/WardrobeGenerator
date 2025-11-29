package com.example.wardrobegenerator.data.storage

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [28])
class ImageStorageManagerTest {

    private lateinit var context: Context
    private lateinit var imageStorageManager: ImageStorageManager

    @Before
    fun setup() {
        context = RuntimeEnvironment.getApplication()
        imageStorageManager = ImageStorageManager(context)
    }

    @Test
    fun `deleteImage returns false when file does not exist`() = runTest {
        val nonExistentPath = "/path/to/nonexistent/image.jpg"

        val result = imageStorageManager.deleteImage(nonExistentPath)

        assertFalse(result)
    }

    @Test
    fun `deleteImage returns true when file is deleted`() = runTest {
        val tempFile = File.createTempFile("test", ".jpg", context.cacheDir)
        tempFile.writeText("test content")
        assertTrue(tempFile.exists())

        val result = imageStorageManager.deleteImage(tempFile.absolutePath)

        assertTrue(result)
        assertFalse(tempFile.exists())
    }

    @Test
    fun `getStorageUsed returns 0 for empty directory`() = runTest {
        val wardrobeDir = File(context.getExternalFilesDir(null), "wardrobe")
        wardrobeDir.mkdirs()
        wardrobeDir.listFiles()?.forEach { it.delete() }

        val storageUsed = imageStorageManager.getStorageUsed()

        assertTrue(storageUsed == 0L)
    }

    @Test
    fun `saveBitmap creates file with correct category prefix`() = runTest {
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val category = "TEST_CATEGORY"

        val savedPath = imageStorageManager.saveBitmap(bitmap, category)

        assertTrue(File(savedPath).exists())
        assertTrue(savedPath.contains("test_category"))
        File(savedPath).delete()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `getContentUri throws when file does not exist`() {
        val nonExistentPath = "/path/to/nonexistent/image.jpg"

        imageStorageManager.getContentUri(nonExistentPath)
    }
}
