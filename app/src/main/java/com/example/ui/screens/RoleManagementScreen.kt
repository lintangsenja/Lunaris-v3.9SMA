package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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

data class PermissionSubItemData(
    val key: String,
    val title: String,
    val description: String,
    val defaultVal: Boolean
)

data class PermissionParentItemData(
    val parentKey: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconColor: Color,
    val subItems: List<PermissionSubItemData>
)

data class PermissionGroupData(
    val groupTitle: String,
    val groupSubtitle: String,
    val groupIcon: ImageVector,
    val items: List<PermissionParentItemData>
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

    // Map to track expanded state of each parent menu card
    var expandedParents by remember { mutableStateOf(mapOf<String, Boolean>()) }

    val permissionGroups = remember {
        listOf(
            PermissionGroupData(
                groupTitle = "1. Sirkulasi & Peminjaman",
                groupSubtitle = "Fitur transaksi keluar masuk, QR code, dan riwayat sirkulasi",
                groupIcon = Icons.Default.CloudSync,
                items = listOf(
                    PermissionParentItemData(
                        parentKey = "peminjaman",
                        title = "Peminjaman Alat",
                        description = "Menu utama untuk pengajuan dan peminjaman alat",
                        icon = Icons.Default.Assignment,
                        iconBgColor = Color(0xFFD1FAE5),
                        iconColor = Color(0xFF059669),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "peminjaman_form",
                                title = "Form Ajukan Peminjaman",
                                description = "Izinkan siswa mengisi form transaksi peminjaman barang",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "peminjaman_riwayat",
                                title = "Riwayat & Status Peminjaman",
                                description = "Izinkan siswa melihat daftar transaksi peminjaman aktif/selesai",
                                defaultVal = false
                            )
                        )
                    ),
                    PermissionParentItemData(
                        parentKey = "pengembalian",
                        title = "Pengembalian Alat",
                        description = "Menu utama untuk proses pengembalian alat terpinjam",
                        icon = Icons.Default.AssignmentReturn,
                        iconBgColor = Color(0xFFE0E7FF),
                        iconColor = Color(0xFF4F46E5),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "pengembalian_normal",
                                title = "Pengembalian Kondisi Baik",
                                description = "Proses pengembalian fisik barang dalam keadaan normal",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "pengembalian_parsial",
                                title = "Pengembalian Rusak / Parsial",
                                description = "Opsi pelaporan barang rusak/kurang saat pengembalian",
                                defaultVal = false
                            )
                        )
                    ),
                    PermissionParentItemData(
                        parentKey = "qr_group",
                        title = "Grup QR Code",
                        description = "Pemindaian scanner dan pembuat kode QR barang",
                        icon = Icons.Default.QrCode,
                        iconBgColor = Color(0xFFFCE7F3),
                        iconColor = Color(0xFFDB2777),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "scan_qr",
                                title = "Pindai / Scan QR Code",
                                description = "Memindai QR barang untuk pencarian & transaksi cepat",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "generate_qr",
                                title = "Buat / Generate QR Code",
                                description = "Membuat dan mencetak label QR barang baru/tambahan",
                                defaultVal = false
                            )
                        )
                    ),
                    PermissionParentItemData(
                        parentKey = "log_transaksi",
                        title = "Log Transaksi",
                        description = "Catatan rekam jejak aktivitas sirkulasi barang",
                        icon = Icons.Default.CloudSync,
                        iconBgColor = Color(0xFFCCFBF1),
                        iconColor = Color(0xFF0D9488),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "log_transaksi_view",
                                title = "Melihat Log Terpadu",
                                description = "Membuka riwayat log aktivitas peminjaman & pengembalian",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "log_transaksi_export",
                                title = "Export / Cetak Riwayat Log",
                                description = "Mengunduh atau mencetak berkas log sirkulasi",
                                defaultVal = false
                            )
                        )
                    )
                )
            ),
            PermissionGroupData(
                groupTitle = "2. Inventaris Aset & Alat",
                groupSubtitle = "Katalog alat, status kondisi kelayakan, dan pemeliharaan",
                groupIcon = Icons.Default.Build,
                items = listOf(
                    PermissionParentItemData(
                        parentKey = "alat",
                        title = "Daftar Alat",
                        description = "Katalog aset peralatan lab dan gudang sarpras",
                        icon = Icons.Default.Build,
                        iconBgColor = Color(0xFFF3E8FF),
                        iconColor = Color(0xFF7C3AED),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "alat_view",
                                title = "Katalog & Ketersediaan Alat",
                                description = "Melihat daftar barang, stok tersedia, dan lokasi alat",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "alat_detail",
                                title = "Cari & Detail Spesifikasi Alat",
                                description = "Pencarian lanjutan dan detail teknis/merk alat",
                                defaultVal = false
                            )
                        )
                    ),
                    PermissionParentItemData(
                        parentKey = "kondisi_alat",
                        title = "Kondisi Alat",
                        description = "Informasi kondisi fisik dan tingkat kelayakan alat",
                        icon = Icons.Default.Info,
                        iconBgColor = Color(0xFFFFE4E6),
                        iconColor = Color(0xFFE11D48),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "kondisi_alat_view",
                                title = "Cek Status Kelayakan Alat",
                                description = "Melihat rekap status alat baik, rusak, atau perawatan",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "kondisi_alat_report",
                                title = "Lihat Rekap Fisik Alat",
                                description = "Rincian catatan kondisi fisik setiap barang",
                                defaultVal = false
                            )
                        )
                    ),
                    PermissionParentItemData(
                        parentKey = "alat_rusak",
                        title = "Lapor Alat Rusak",
                        description = "Fitur pelaporan kendala dan kerusakan fisik alat",
                        icon = Icons.Default.Warning,
                        iconBgColor = Color(0xFFFFECEF),
                        iconColor = Color(0xFFEF4444),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "alat_rusak_submit",
                                title = "Form Lapor Kerusakan Alat",
                                description = "Mengirimkan formulir pengaduan kerusakan alat",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "alat_rusak_view",
                                title = "Melihat Daftar Alat Rusak",
                                description = "Daftar daftar alat yang sedang dalam status rusak",
                                defaultVal = false
                            )
                        )
                    ),
                    PermissionParentItemData(
                        parentKey = "pemeliharaan",
                        title = "Pemeliharaan Alat",
                        description = "Jadwal servis berkala dan perawatan aset",
                        icon = Icons.Default.Build,
                        iconBgColor = Color(0xFFEFF6FF),
                        iconColor = Color(0xFF2563EB),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "pemeliharaan_view",
                                title = "Jadwal & Histori Perawatan",
                                description = "Melihat agenda perawatan rutin peralatan",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "pemeliharaan_history",
                                title = "Catatan Servis Perbaikan",
                                description = "Melihat riwayat perbaikan dan peremajaan alat",
                                defaultVal = false
                            )
                        )
                    )
                )
            ),
            PermissionGroupData(
                groupTitle = "3. Bahan Habis Pakai (BHP)",
                groupSubtitle = "Katalog bahan, log pemakaian praktikum, dan afkir",
                groupIcon = Icons.Default.Science,
                items = listOf(
                    PermissionParentItemData(
                        parentKey = "bahan",
                        title = "Daftar Bahan",
                        description = "Stok bahan praktikum, konsumabel, dan logistik",
                        icon = Icons.Default.Science,
                        iconBgColor = Color(0xFFE0F2FE),
                        iconColor = Color(0xFF0284C7),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "bahan_view",
                                title = "Katalog Stok Bahan",
                                description = "Melihat stok terkini bahan habis pakai",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "bahan_detail",
                                title = "Detail Lokasi & Masa Simpan",
                                description = "Melihat informasi ruang simpan dan tanggal kedaluwarsa",
                                defaultVal = false
                            )
                        )
                    ),
                    PermissionParentItemData(
                        parentKey = "pemakaian_bahan",
                        title = "Pemakaian Bahan",
                        description = "Pencatatan konsumsi bahan praktikum siswa",
                        icon = Icons.Default.ShoppingCart,
                        iconBgColor = Color(0xFFFCE7F3),
                        iconColor = Color(0xFFDB2777),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "pemakaian_bahan_form",
                                title = "Form Catat Pemakaian Bahan",
                                description = "Input penggunaan kuantitas bahan praktikum",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "pemakaian_bahan_log",
                                title = "Log Pemakaian Praktikum",
                                description = "Melihat riwayat konsumsi bahan oleh siswa/kelas",
                                defaultVal = false
                            )
                        )
                    ),
                    PermissionParentItemData(
                        parentKey = "bahan_afkir",
                        title = "Bahan Afkir / Rusak",
                        description = "Pengelolaan bahan rusak atau kadaluwarsa",
                        icon = Icons.Default.DeleteSweep,
                        iconBgColor = Color(0xFFFFEDD5),
                        iconColor = Color(0xFFEA580C),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "bahan_afkir_view",
                                title = "Lihat Daftar Bahan Afkir",
                                description = "Melihat list bahan yang dinyatakan tidak layak pakai",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "bahan_afkir_report",
                                title = "Laporan Kedaluwarsa Bahan",
                                description = "Melihat berkas berita acara penghapusan bahan",
                                defaultVal = false
                            )
                        )
                    )
                )
            ),
            PermissionGroupData(
                groupTitle = "4. Master Data & Laporan",
                groupSubtitle = "Pengelolaan data induk, stok opname, dan laporan terpadu",
                groupIcon = Icons.Default.Storage,
                items = listOf(
                    PermissionParentItemData(
                        parentKey = "master_data",
                        title = "Master Data",
                        description = "Data induk barang, kategori, dan lokasi ruang",
                        icon = Icons.Default.Storage,
                        iconBgColor = Color(0xFFD1FAE5),
                        iconColor = Color(0xFF10B981),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "master_data_view",
                                title = "Lihat Induk Barang & Lokasi",
                                description = "Melihat struktur master data barang dan daftar ruang",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "master_data_manage",
                                title = "Pengelolaan Data & Kategori",
                                description = "Pengaturan kategori barang dan parameter sarpras",
                                defaultVal = false
                            )
                        )
                    ),
                    PermissionParentItemData(
                        parentKey = "stok_opname",
                        title = "Stok Opname",
                        description = "Audit fisik dan penyesuaian ketersediaan barang",
                        icon = Icons.Default.Inventory,
                        iconBgColor = Color(0xFFEFF6FF),
                        iconColor = Color(0xFF3B82F6),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "stok_opname_audit",
                                title = "Audit Physical Count Gudang",
                                description = "Opsi verifikasi hitung ulang stok di lapangan",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "stok_opname_reconcile",
                                title = "Penyesuaian Fisik Stok",
                                description = "Fitur penyesuaian angka stok sistem vs fisik",
                                defaultVal = false
                            )
                        )
                    ),
                    PermissionParentItemData(
                        parentKey = "laporan",
                        title = "Laporan & Rekapan",
                        description = "Pratinjau dan unduh berkas laporan bulanan",
                        icon = Icons.Default.Assessment,
                        iconBgColor = Color(0xFFECFEFF),
                        iconColor = Color(0xFF06B6D4),
                        subItems = listOf(
                            PermissionSubItemData(
                                key = "laporan_view",
                                title = "Pratinjau Laporan Sirkulasi",
                                description = "Melihat grafik dan ringkasan aktivitas sarpras",
                                defaultVal = false
                            ),
                            PermissionSubItemData(
                                key = "laporan_export",
                                title = "Cetak & Export PDF / Excel",
                                description = "Mengunduh file laporan dalam format PDF atau Excel",
                                defaultVal = false
                            )
                        )
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
                            text = "Dynamic User Level Control (Hierarki Parent-Child)",
                            fontSize = 11.sp,
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                                text = "Hierarki Akses Role Siswa",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepPurpleText
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Tombol Switch Induk (Parent) merefleksikan status sub-menu di dalamnya: Aktif Penuh (Hijau), Non-Aktif (Abu-abu), atau Parsial (Kuning/Oranye). Tekan panah untuk mengatur sub-menu.",
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

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        group.items.forEach { parentItem ->
                            val isExpanded = expandedParents[parentItem.parentKey] ?: false

                            // Compute child active counts
                            val activeSubCount = parentItem.subItems.count { sub ->
                                studentPermissions[sub.key] ?: sub.defaultVal
                            }
                            val totalSubCount = parentItem.subItems.size

                            // Status Parent Logic: Full, Partial, Off
                            val isFullActive = activeSubCount == totalSubCount
                            val isOff = activeSubCount == 0
                            val isPartial = !isFullActive && !isOff

                            val parentBadgeText = when {
                                isFullActive -> "Aktif Penuh"
                                isPartial -> "Parsial ($activeSubCount/$totalSubCount)"
                                else -> "Non-Aktif"
                            }

                            val parentBadgeBgColor = when {
                                isFullActive -> Color(0xFFDCFCE7)
                                isPartial -> Color(0xFFFEF3C7)
                                else -> Color(0xFFF3F4F6)
                            }

                            val parentBadgeTextColor = when {
                                isFullActive -> Color(0xFF15803D)
                                isPartial -> Color(0xFFB45309)
                                else -> Color(0xFF6B7280)
                            }

                            val parentSwitchChecked = activeSubCount > 0

                            LunarisCard(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (isPartial) Color(0xFFFCD34D) else Color(0xFFE9D5FF)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    // Parent Header Row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    expandedParents = expandedParents.toMutableMap().apply {
                                                        put(parentItem.parentKey, !isExpanded)
                                                    }
                                                }
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .size(42.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(parentItem.iconBgColor)
                                            ) {
                                                Icon(
                                                    imageVector = parentItem.icon,
                                                    contentDescription = parentItem.title,
                                                    tint = parentItem.iconColor,
                                                    modifier = Modifier.size(22.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Text(
                                                        text = parentItem.title,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = CarbonBlackText
                                                    )
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(parentBadgeBgColor)
                                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                                    ) {
                                                        Text(
                                                            text = parentBadgeText,
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = parentBadgeTextColor
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = parentItem.description,
                                                    fontSize = 10.sp,
                                                    color = Color.Gray,
                                                    lineHeight = 13.sp
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        // Parent Switch Logic
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Switch(
                                                checked = parentSwitchChecked,
                                                onCheckedChange = { _ ->
                                                    // Toggling Parent:
                                                    // If currently FULL -> turn ALL sub-items OFF
                                                    // If currently OFF or PARTIAL -> turn ALL sub-items ON
                                                    val targetVal = !isFullActive
                                                    val updates = mutableMapOf<String, Boolean>()
                                                    updates[parentItem.parentKey] = targetVal
                                                    parentItem.subItems.forEach { sub ->
                                                        updates[sub.key] = targetVal
                                                    }
                                                    viewModel.updateStudentPermissionsBatch(updates)

                                                    val statusMsg = if (targetVal) "diaktifkan penuh" else "dinonaktifkan"
                                                    Toast.makeText(
                                                        context,
                                                        "Grup '${parentItem.title}' $statusMsg untuk Siswa",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                },
                                                colors = SwitchDefaults.colors(
                                                    checkedThumbColor = Color.White,
                                                    checkedTrackColor = if (isPartial) Color(0xFFD97706) else Color(0xFF10B981),
                                                    uncheckedThumbColor = Color.White,
                                                    uncheckedTrackColor = Color(0xFFD1D5DB)
                                                ),
                                                modifier = Modifier.testTag("parent_switch_${parentItem.parentKey}")
                                            )

                                            IconButton(
                                                onClick = {
                                                    expandedParents = expandedParents.toMutableMap().apply {
                                                        put(parentItem.parentKey, !isExpanded)
                                                    }
                                                },
                                                modifier = Modifier.size(32.dp).testTag("btn_expand_${parentItem.parentKey}")
                                            ) {
                                                Icon(
                                                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                                    contentDescription = "Expand Submenu",
                                                    tint = Color.Gray
                                                )
                                            }
                                        }
                                    }

                                    // Expandable Sub-Items List
                                    AnimatedVisibility(
                                        visible = isExpanded,
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically()
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 10.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFFF8FAFC))
                                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                                .padding(12.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(bottom = 6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.AccountTree,
                                                    contentDescription = "Submenu",
                                                    tint = Color(0xFF64748B),
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Sub-Menu Fitur Spesifik:",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF475569)
                                                )
                                            }

                                            HorizontalDivider(thickness = 0.8.dp, color = Color(0xFFE2E8F0))

                                            parentItem.subItems.forEachIndexed { subIdx, subItem ->
                                                val isSubChecked = studentPermissions[subItem.key] ?: subItem.defaultVal

                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 8.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Text(
                                                                text = subItem.title,
                                                                fontSize = 12.sp,
                                                                fontWeight = FontWeight.SemiBold,
                                                                color = CarbonBlackText
                                                            )
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Box(
                                                                modifier = Modifier
                                                                    .clip(RoundedCornerShape(4.dp))
                                                                    .background(
                                                                        if (isSubChecked) Color(0xFFDCFCE7) else Color(0xFFF3F4F6)
                                                                    )
                                                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                                                            ) {
                                                                Text(
                                                                    text = if (isSubChecked) "Aktif" else "Mati",
                                                                    fontSize = 8.sp,
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = if (isSubChecked) Color(0xFF15803D) else Color(0xFF6B7280)
                                                                )
                                                            }
                                                        }
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                        Text(
                                                            text = subItem.description,
                                                            fontSize = 10.sp,
                                                            color = Color.Gray,
                                                            lineHeight = 12.sp
                                                        )
                                                    }

                                                    Switch(
                                                        checked = isSubChecked,
                                                        onCheckedChange = { newSubVal ->
                                                            // Update sub item value
                                                            val updates = mutableMapOf<String, Boolean>()
                                                            updates[subItem.key] = newSubVal

                                                            // Recalculate parent state
                                                            val futureActiveCount = parentItem.subItems.count { sub ->
                                                                if (sub.key == subItem.key) newSubVal
                                                                else (studentPermissions[sub.key] ?: sub.defaultVal)
                                                            }
                                                            updates[parentItem.parentKey] = (futureActiveCount > 0)

                                                            viewModel.updateStudentPermissionsBatch(updates)

                                                            val statusTxt = if (newSubVal) "DILANGSUNGKAN" else "DISEMBUNYIKAN"
                                                            Toast.makeText(
                                                                context,
                                                                "Sub-menu '${subItem.title}' $statusTxt",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        },
                                                        colors = SwitchDefaults.colors(
                                                            checkedThumbColor = Color.White,
                                                            checkedTrackColor = Color(0xFF10B981),
                                                            uncheckedThumbColor = Color.White,
                                                            uncheckedTrackColor = Color(0xFFCBD5E1)
                                                        ),
                                                        modifier = Modifier.scale(0.85f).testTag("sub_switch_${subItem.key}")
                                                    )
                                                }

                                                if (subIdx < parentItem.subItems.size - 1) {
                                                    HorizontalDivider(
                                                        thickness = 0.5.dp,
                                                        color = Color(0xFFE2E8F0)
                                                    )
                                                }
                                            }
                                        }
                                    }
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
                    text = "Pengaturan hak akses role Siswa dan hierarki sub-menu akan dikembalikan ke konfigurasi standar (Peminjaman, Pengembalian, Daftar Alat, Daftar Bahan, dan Scan QR diizinkan).",
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
