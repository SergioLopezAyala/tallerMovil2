package com.example.taller2_movil.ui.camera

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.taller2_movil.R
import com.example.taller2_movil.ui.gallery.GalleryGrid
import com.example.taller2_movil.util.takePictureWithController

@Composable
fun CameraScreen(
    sessionPhotos: List<Uri>,
    onAddPhoto: (Uri) -> Unit
) {
    val ctx = LocalContext.current
    val screenH = LocalConfiguration.current.screenHeightDp.dp

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)) {
        CameraPreviewBox(
            modifier = Modifier.fillMaxWidth().height(screenH * 0.30f),
            onTake = { controller ->
                takePictureWithController(
                    ctx = ctx,
                    controller = controller,
                    onSaved = { uri ->
                        Toast.makeText(ctx, ctx.getString(R.string.saved_ok), Toast.LENGTH_SHORT).show()
                        onAddPhoto(uri)
                    },
                    onError = { msg -> Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show() }
                )
            },
            onSwitch = {}
        )

        Spacer(Modifier.height(16.dp))

        if (sessionPhotos.isEmpty()) {
            Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.no_photos), style = MaterialTheme.typography.titleMedium)
            }
        } else {
            GalleryGrid(
                modifier = Modifier.weight(1f),
                uris = sessionPhotos,
                columns = StaggeredGridCells.Adaptive(minSize = 120.dp)
            )
        }
    }
}
