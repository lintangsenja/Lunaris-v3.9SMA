package com.example.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DynamicLogo(
    modifier: Modifier = Modifier,
    defaultIcon: ImageVector = Icons.Default.Business,
    defaultIconTint: Color = Color(0xFF7C3AED),
    contentDescription: String? = "Logo"
) {
    var logoUrl by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val listener = firestore.collection("pengaturan_global")
            .document("profil_admin")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("DynamicLogo", "Snapshot listener failed", error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val url = snapshot.getString("logo_url")
                    logoUrl = if (url.isNullOrBlank()) null else url
                } else {
                    logoUrl = null
                }
            }

        onDispose {
            listener.remove()
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (!logoUrl.isNullOrEmpty()) {
            AsyncImage(
                model = logoUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = defaultIcon,
                contentDescription = contentDescription,
                tint = defaultIconTint,
                modifier = Modifier.fillMaxSize(0.6f)
            )
        }
    }
}
