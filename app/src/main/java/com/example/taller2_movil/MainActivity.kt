package com.example.taller2_movil
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.google.accompanist.permissions.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoboothApp()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)
@Composable
fun PhotoboothApp() {
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        if (cameraPermission.status.isGranted) {
            // Estado de la sesión (solo en memoria)
            val sessionPhotos = remember { mutableStateListOf<Uri>() }
            CameraWithGallery(
                onPhotoCaptured = { uri -> sessionPhotos.add(uri) },
                sessionPhotos = sessionPhotos
            )
        } else {
            PermissionScreen(
                onRequest = { cameraPermission.launchPermissionRequest() }
            )
        }
    }
}

/** Pantalla a pantalla completa solicitando permiso (Accompanist). */
@Composable
fun PermissionScreen(onRequest: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.perm_title), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.perm_subtitle))
            Spacer(Modifier.height(24.dp))
            Button(onClick = onRequest) {
                Text(stringResource(id = R.string.perm_button))
            }
        }
    }
}

/** Vista de Cámara (30% alto) + Galería de la sesión debajo. */
@SuppressLint("RestrictedApi")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CameraWithGallery(
    onPhotoCaptured: (Uri) -> Unit,
    sessionPhotos: List<Uri>
) {
    val ctx = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeightPx = configuration.screenHeightDp.dp

    val controller = remember {
        LifecycleCameraController(ctx).apply {
            setEnabledUseCases(
                androidx.camera.view.CameraController.IMAGE_CAPTURE)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        // ---- Vista de Cámara: ancho completo + 30% del alto de pantalla ----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeightPx * 0.30f)
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                factory = { context ->
                    PreviewView(context).apply {
                        this.controller = controller
                        controller.bindToLifecycle(context as ComponentActivity)
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }
                }
            )

            // Botón "Cambiar cámara" (esquina superior izquierda)
            IconButton(
                onClick = { controller.cameraSelector =
                    if (controller.cameraSelector.lensFacing == androidx.camera.core.CameraSelector.LENS_FACING_BACK)
                        androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
                    else
                        androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.Cameraswitch, contentDescription = stringResource(R.string.switch_camera))
            }

            // Botón "Tomar foto" (centrado abajo dentro del recuadro de cámara)
            FilledIconButton(
                onClick = {
                    takePicture(ctx, controller) { uri ->
                        Toast.makeText(ctx, ctx.getString(R.string.saved_ok), Toast.LENGTH_SHORT).show()
                        onPhotoCaptured(uri)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
            ) {
                Icon(Icons.Default.Camera, contentDescription = stringResource(R.string.take_picture))
            }
        }

        Spacer(Modifier.height(16.dp))

        // ---- Galería de la sesión (se vacía al relanzar la app) ----
        if (sessionPhotos.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.no_photos),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        } else {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.weight(1f),
                columns = StaggeredGridCells.Adaptive(minSize = 120.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(sessionPhotos) { uri ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f) // cuadrado
                        )
                    }
                }
            }
        }
    }
}

/** Toma la foto con CameraX y guarda en almacenamiento externo de la app. */
private fun takePicture(
    ctx: Context,
    controller: LifecycleCameraController,
    onSaved: (Uri) -> Unit
) {
    val outputDir = getOutputDirectory(ctx)
    if (!outputDir.exists()) outputDir.mkdirs()

    val filename = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()) + ".jpg"
    val photoFile = File(outputDir, filename)

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(ctx),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val uri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                onSaved(uri)
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(ctx, exception.message ?: "Error al guardar la foto", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

private fun getOutputDirectory(context: Context): File {
    val base = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File(base, "Photobooth")
}
