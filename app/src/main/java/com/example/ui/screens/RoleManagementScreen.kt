package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LunarisCard
import com.example.ui.theme.CarbonBlackText
import com.example.ui.theme.DeepPurpleText
import com.example.ui.theme.pastelGradientBackground
import com.example.ui.viewmodel.InventoryViewModel

data class PermissionItemData(
    val key: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconColor: Color,
    val defaultVal: Boolean
)

data class PermissionGroupData(
    val groupTitle: String,
    val groupSubtitle: String,
    val groupIcon: ImageVector,
    val items: List<PermissionItemData>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleManagementScreen(
    viewModel: InventoryViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val studentPermissions by viewModel.studentPermissions.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    val permissionGroups = remember {
        listOf(
            PermissionGroupData(
                groupTitle = "1. Sirkulasi & Peminjaman",
                groupSubtitle = "Fitur transaksi keluar masuk dan riwayat barang",
                groupIcon = Icons.Default.CloudSync,
                items = listOf(
                    PermissionItemData(
                        key = "peminjaman",
                        title = "Peminjaman Alat",
                        description = "Mengajukan atau mencatat peminjaman alat gudang",
                        icon = Icons.Default.Assignment,
                        iconBgColor = Color(0xFFD1FAE5),
                        iconColor = Color(0xFF059669),
                        defaultVal = true
                    ),
                    PermissionItemData(
                        key = "pengembalian",
                        title = "Pengembalian Alat",
                        description = "Mengembalikan alat yang telah dipinjam",
                        icon = Icons.Default.AssignmentReturn,
                        iconBgColor = Color(0xFFE0E7FF),
                        iconColor = Color(0xFF4F46E5),
                        defaultVal = true
                    ),
                    PermissionItemData(
                        key = "scan_qr",
                        title = "Scan QR Code",
                        description = "Memindai kode QR barang untuk akses dan transaksi cepat",
                        icon = Icons.Default.QrCode,
                        iconBgColor = Color(0xFFFCE7F3),
                        iconColor = Color(0xFFDB2777),
                        defaultVal = true
                    ),
                    PermissionItemData(
                        key = "log_transaksi",
                        title = "Log Transaksi",
                        description = "Melihat seluruh riwayat transaksi sirkulasi",
                        icon = Icons.Default.CloudSync,
                        iconBgColor = Color(0xFFCCFBF1),
                        iconColor = Color(0xFF0D9488),
                        defaultVal = false
                    )
                )
            ),
            PermissionGroupData(
                groupTitle = "2. Inventaris Aset & Alat",
                groupSubtitle = "Katalog alat, status kondisi, dan perawatan",
                groupIcon = Icons.Default.Build,
                items = listOf(
                    PermissionItemData(
                        key = "alat",
                        title = "Daftar Alat",
                        description = "Melihat daftar, ketersediaan, dan spesifikasi alat",
                        icon = Icons.Default.Build,
                        iconBgColor = Color(0xFFF3E8FF),
                        iconColor = Color(0xFF7C3AED),
                        defaultVal = true
                    ),
                    PermissionItemData(
                        key = "kondisi_alat",
                        title = "Kondisi Alat",
                        description = "Cek kelayakan, kelengkapan, dan status alat",
                        icon = Icons.Default.Info,
                        iconBgColor = Color(0xFFFFE4E6),
                        iconColor = Color(0xFFE11D48),
                        defaultVal = false
                    ),
                    PermissionItemData(
                        key = "alat_rusak",
                        title = "Lapor Alat Rusak",
                        description = "Melaporkan kerusakan fisik atau kendala alat",
                        icon = Icons.Default.Warning,
                        iconBgColor = Color(0xFFFFECEF),
                        iconColor = Color(0xFFEF4444),
                        defaultVal = false
                    ),
                    PermissionItemData(
                        key = "pemeliharaan",
                        title = "Pemeliharaan Alat",
                        description = "Melihat jadwal dan histori perawatan/servis alat",
                        icon = Icons.Default.Build,
                        iconBgColor = Color(0xFFEFF6FF),
                        iconColor = Color(0xFF2563EB),
                        defaultVal = false
                    )
                )
            ),
            PermissionGroupData(
                groupTitle = "3. Bahan Habis Pakai (BHP)",
                groupSubtitle = "Katalog bahan, pemakaian praktikum, dan afkir",
                groupIcon = Icons.Default.Science,
                items = listOf(
                    PermissionItemData(
                        key = "bahan",
                        title = "Daftar Bahan",
                        description = "Melihat stok dan katalog bahan habis pakai",
                        icon = Icons.Default.Science,
                        iconBgColor = Color(0xFFE0F2FE),
                        iconColor = Color(0xFF0284C7),
                        defaultVal = true
                    ),
                    PermissionItemData(
                        key = "pemakaian_bahan",
                        title = "Pemakaian Bahan",
                        description = "Mencatat penggunaan bahan praktikum/kegiatan",
                        icon = Icons.Default.ShoppingCart,
                        iconBgColor = Color(0xFFFCE7F3),
                        iconColor = Color(0xFFDB2777),
                        defaultVal = false
                    ),
                    PermissionItemData(
                        key = "bahan_afkir",
                        title = "Bahan Afkir / Rusak",
                        description = "Melaporkan bahan kedaluwarsa atau rusak",
                        icon = Icons.Default.DeleteSweep,
                        iconBgColor = Color(0xFFFFEDD5),
                        iconColor = Color(0xFFEA580C),
                        defaultVal = false
                    )
                )
            ),
            PermissionGroupData(
                groupTitle = "4. Master Data & Laporan",
                groupSubtitle = "Pengelolaan data induk, opname stok, dan analisis",
                groupIcon = Icons.Default.Storage,
                items = listOf(
                    PermissionItemData(
                        key = "master_data",
                        title = "Master Data",
                        description = "Mengelola data barang, kategori, dan lokasi",
                        icon = Icons.Default.Storage,
                        iconBgColor = Color(0xFFD1FAE5),
                        iconColor = Color(0xFF10B981),
                        defaultVal = false
                    ),
                    PermissionItemData(
                        key = "stok_opname",
                        title = "Stok Opname",
                        description = "Audit dan penyesuaian fisik stok gudang",
                        icon = Icons.Default.Inventory,
                        iconBgColor = Color(0xFFEFF6FF),
                        iconColor = Color(0xFF3B82F6),
                        defaultVal = false
                    ),
                    PermissionItemData(
                        key = "laporan",
                        title = "Laporan & Rekapan",
                        description = "Melihat dan mengunduh laporan sirkulasi gudang",
                        icon = Icons.Default.Assessment,
                        iconBgColor = Color(0xFFECFEFF),
                        iconColor = Color(0xFF06B6D4),
                        defaultVal = false
                    )
                )
            )
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Pengaturan Akses",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Dynamic User Level Control (Siswa)",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("btn_back_role_management")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showResetDialog = true },
                        modifier = Modifier.testTag("btn_reset_permissions")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Permission",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .shadow(elevation = 3.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFEA580C),
                                Color(0xFFD97706)
                            )
                        )
                    )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pastelGradientBackground(isDark = false)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Info Header Card
                LunarisCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, Color(0xFFFED7AA)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFEDD5))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Akses Security",
                                tint = Color(0xFFEA580C),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Manajemen Hak Akses Role Siswa",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepPurpleText
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Gunakan Switch di bawah untuk memunculkan atau menyembunyikan menu pada layar Siswa secara real-time via Firebase Firestore.",
                                fontSize = 11.sp,
                                color = CarbonBlackText.copy(alpha = 0.75f),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Permission Groups
                permissionGroups.forEach { group ->
                    Text(
                        text = group.groupTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepPurpleText,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(
                        text = group.groupSubtitle,
                        fontSize = 11.sp,
                        color = CarbonBlackText.copy(alpha = 0.65f),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LunarisCard(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color(0xFFE9D5FF)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            group.items.forEachIndexed { index, item ->
                                val isChecked = studentPermissions[item.key] ?: item.defaultVal

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(item.iconBgColor)
                                        ) {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = item.title,
                                                tint = item.iconColor,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.padding(end = 8.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = item.title,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = CarbonBlackText
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(
                                                            if (isChecked) Color(0xFFDCFCE7) else Color(0xFFF3F4F6)
                                                        )
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = if (isChecked) "Muncul" else "Sembunyi",
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isChecked) Color(0xFF15803D) else Color(0xFF6B7280)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = item.description,
                                                fontSize = 10.sp,
                                                color = Color.Gray,
                                                lineHeight = 13.sp
                                            )
                                        }
                                    }

                                    Switch(
                                        checked = isChecked,
                                        onCheckedChange = { newValue ->
                                            viewModel.updateStudentPermission(item.key, newValue)
                                            val statusText = if (newValue) "DILANGSUNGKAN (MUNCUL)" else "DISEMBUNYIKAN"
                                            Toast.makeText(
                                                context,
                                                "Akses '${item.title}' untuk Siswa $statusText",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = Color(0xFF10B981),
                                            uncheckedThumbColor = Color.White,
                                            uncheckedTrackColor = Color(0xFFD1D5DB)
                                        ),
                                        modifier = Modifier.testTag("switch_perm_${item.key}")
                                    )
                                }

                                if (index < group.items.size - 1) {
                                    HorizontalDivider(
                                        thickness = 0.8.dp,
                                        color = Color(0xFFF3F4F6)
                                    )
                                }
                            }
                        }
                    }
                }

                // Reset Button
                OutlinedButton(
                    onClick = { showResetDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFEA580C)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFEA580C)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_reset_defaults_bottom")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reset ke Pengaturan Akses Default",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = Color(0xFFEA580C)
                )
            },
            title = {
                Text(
                    text = "Reset Hak Akses Siswa?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    text = "Pengaturan hak akses role Siswa akan dikembalikan ke konfigurasi standar (Peminjaman, Pengembalian, Daftar Alat, Daftar Bahan, dan Scan QR diizinkan).",
                    fontSize = 13.sp,
                    color = CarbonBlackText.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetStudentPermissionsToDefault()
                        showResetDialog = false
                        Toast.makeText(context, "Hak akses Siswa berhasil di-reset ke default!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C))
                ) {
                    Text("Ya, Reset", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
