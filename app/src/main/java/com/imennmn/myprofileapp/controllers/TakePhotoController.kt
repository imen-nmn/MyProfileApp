package com.imennmn.myprofileapp.controllers

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.util.Log
import com.imennmn.myprofileapp.BuildConfig
import java.io.*

/**
 * Created by imen_nmn on 15/11/17.
 */

class TakePhotoController {
    private val authority = BuildConfig.APPLICATION_ID + ".provider"
    private val photoName = "photo"
    private var photoUrl = ""

    @Throws(IOException::class)
     fun takePicture(fragment: Fragment, requestId: Int) {
        val takePhotoDir = getCacheDir("takePhoto", fragment.context)

        val takePhotoFile = File(takePhotoDir, photoName + ".jpg")
        val outputUri = FileProvider.getUriForFile(fragment.context, authority, takePhotoFile)
        photoUrl = takePhotoFile.absolutePath
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        fragment.startActivityForResult(cameraIntent, requestId)
        Log.e("onSelectFromT", " takePictue ==========> " + photoUrl)

    }

    private fun getCacheDir(dirName: String, context: Context): File? {
        val result: File
        result = if (existsSdcard()) {
            val cacheDir = context.externalCacheDir
            if (cacheDir == null) {
                File(Environment.getExternalStorageDirectory(),
                        "Android/data/" + context.packageName + "/cache/" + dirName)
            } else {
                File(cacheDir, dirName)
            }
        } else {
            File(context.cacheDir, dirName)
        }

        return if (result.exists() || result.mkdirs()) {
            result
        } else {
            null
        }
    }

    private fun existsSdcard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    public fun generatePhoto(): Bitmap? {
        Log.e("onSelectFromT", "$photoUrl <========")
        var bitmap: Bitmap
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(photoUrl, options)
        val REQUIRED_SIZE = 200
        var scale = 1
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2
        options.inSampleSize = scale
        options.inJustDecodeBounds = false
        bitmap = BitmapFactory.decodeFile(photoUrl, options)
        val bytes = ByteArrayOutputStream()

        val success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        if (!success) {
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        }
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(photoUrl)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            val bmRotated = rotateBitmap(bitmap, orientation)

            return bmRotated;
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }

    /**
     * Method to rotate photo from camera or galley photo
     *
     * @param bitmap
     * @param orientation
     * @return
     */
    fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        try {
            val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            return bmRotated
        } catch (e: OutOfMemoryError) {
            return null
        }
    }

    private fun saveBitmap(bitmap: Bitmap, mediaPath: String): File {
        var outStream: OutputStream? = null

        var file = File(mediaPath)
        if (file.exists()) {
            file.delete()
            file = File(mediaPath)
        }
        try {
            outStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file

    }

    private fun getFileExtension(fileName: String): String {
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex == -1) "" else fileName.substring(dotIndex + 1)
    }

}