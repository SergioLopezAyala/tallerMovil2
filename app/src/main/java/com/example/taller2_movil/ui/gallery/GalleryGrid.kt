package com.example.taller2_movil.ui.gallery

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryGrid(
    modifier: Modifier = Modifier,
    uris: List<Uri>,
    columns: StaggeredGridCells
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = columns,
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(uris) { uri ->
            Card(Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                )
            }
        }
    }
}
