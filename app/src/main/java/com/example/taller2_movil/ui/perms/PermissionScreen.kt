package com.example.taller2_movil.ui.perms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taller2_movil.R

@Composable
fun PermissionScreen(onRequest: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),   // margen lateral
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.perm_title),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,          // 👈 centra el texto
                modifier = Modifier.fillMaxWidth()     // 👈 dale ancho para centrar
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.perm_subtitle),
                textAlign = TextAlign.Center,          // 👈 centra el texto
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))
            Button(onClick = onRequest) {
                Text(text = stringResource(R.string.perm_button))
            }
        }
    }
}
