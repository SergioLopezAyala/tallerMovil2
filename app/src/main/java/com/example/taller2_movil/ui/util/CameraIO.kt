package com.example.taller2_movil.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private fun getOutputDirectory(context: Context): File {
    val base = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File(base, "Photobooth")
}

fun takePictureWithController(
    ctx: Context,
    controller: LifecycleCameraController,
    onSaved: (Uri) -> Unit,
    onError: (String) -> Unit
) {
    val dir = getOutputDirectory(ctx).apply { if (!exists()) mkdirs() }
    val filename = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()) + ".jpg"
    val photoFile = File(dir, filename)
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(ctx),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                onSaved(output.savedUri ?: Uri.fromFile(photoFile))
            }
            override fun onError(exception: ImageCaptureException) {
                onError(exception.message ?: "Error al guardar la foto")
            }
        }
    )
}
