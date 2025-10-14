package com.example.taller2_movil.ui.perms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.taller2_movil.R

@Composable
fun PermissionScreen(onRequest: () -> Unit) {
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.perm_title), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.perm_subtitle))
            Spacer(Modifier.height(24.dp))
            Button(onClick = onRequest) { Text(stringResource(R.string.perm_button)) }
        }
    }
}
