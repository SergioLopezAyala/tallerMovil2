package com.example.taller2_movil.ui

import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.taller2_movil.ui.camera.CameraScreen
import com.example.taller2_movil.ui.perms.PermissionScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhotoboothApp() {

    val camPerm = rememberPermissionState(Manifest.permission.CAMERA)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (camPerm.status) {
            is PermissionStatus.Granted -> {
                val sessionPhotos = remember { mutableStateListOf<android.net.Uri>() }
                CameraScreen(
                    sessionPhotos = sessionPhotos,
                    onAddPhoto = { sessionPhotos.add(it) }
                )
            }
            is PermissionStatus.Denied -> {
                PermissionScreen(
                    onRequest = { camPerm.launchPermissionRequest() }
                )
            }
        }
    }
}
