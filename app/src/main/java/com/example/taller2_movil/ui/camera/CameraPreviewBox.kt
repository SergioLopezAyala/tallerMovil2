package com.example.taller2_movil.ui.camera

import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.taller2_movil.R

@Composable
fun CameraPreviewBox(
    modifier: Modifier = Modifier,
    onTake: (LifecycleCameraController) -> Unit,
    onSwitch: () -> Unit
) {
    val ctx = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current

    val controller = remember {
        LifecycleCameraController(ctx).apply {
            setEnabledUseCases(LifecycleCameraController.IMAGE_CAPTURE)
        }
    }

    var lens by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }

    LaunchedEffect(lens) {
        controller.cameraSelector = CameraSelector.Builder().requireLensFacing(lens).build()
        controller.bindToLifecycle(lifecycle)
    }

    Box(modifier) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            factory = { c ->
                PreviewView(c).apply {
                    setController(controller)  // ✅ así es la forma correcta
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            }
        )

        IconButton(
            onClick = {
                lens = if (lens == CameraSelector.LENS_FACING_BACK)
                    CameraSelector.LENS_FACING_FRONT
                else
                    CameraSelector.LENS_FACING_BACK
                onSwitch()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Icon(Icons.Default.Cameraswitch, contentDescription = stringResource(R.string.switch_camera))
        }

        FilledIconButton(
            onClick = { onTake(controller) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
        ) {
            Icon(Icons.Default.Camera, contentDescription = stringResource(R.string.take_picture))
        }
    }
}
    