package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActivityEntity
import com.example.data.CollaboratorEntity
import com.example.data.JournalEntity
import com.example.ui.theme.CaveatFontFamily
import com.example.ui.theme.PlayfairFontFamily
import com.example.ui.theme.QuicksandFontFamily
import com.example.util.SoundManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollaborationSheet(
    journal: JournalEntity,
    collaborators: List<CollaboratorEntity>,
    activities: List<ActivityEntity>,
    isViewerMode: Boolean,
    onDismiss: () -> Unit,
    onInvite: (name: String, email: String, permission: String) -> Unit,
    onUpdatePermission: (collaboratorId: Long, newPermission: String) -> Unit,
    onRemoveCollaborator: (CollaboratorEntity) -> Unit,
    onToggleViewerMode: () -> Unit
) {
    val context = LocalContext.current
    var showInviteForm by remember { mutableStateOf(false) }
    var inviteName by remember { mutableStateOf("") }
    var inviteEmail by remember { mutableStateOf("") }
    var invitePermission by remember { mutableStateOf("CAN_EDIT") } // CAN_EDIT or CAN_VIEW

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Kolaborasi Lembaran",
                        fontFamily = PlayfairFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Ajak teman menghias lembaran OurJour bersama",
                        fontFamily = QuicksandFontFamily,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Tutup")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Kode & Tautan Kolaborasi
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Kode Lembaran Bersama",
                            fontFamily = QuicksandFontFamily,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = journal.collaborationCode,
                            fontFamily = QuicksandFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Kode OurJour", journal.collaborationCode)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Kode OurJour disalin ke papan klip!", Toast.LENGTH_SHORT).show()
                            SoundManager.playClick()
                        },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Outlined.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Salin Kode", fontFamily = QuicksandFontFamily, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Switch Simulasi Peran (Coba mode Lihat Saja vs Bisa Edit)
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isViewerMode) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isViewerMode) Icons.Outlined.Visibility else Icons.Outlined.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (isViewerMode) "Mode: Lihat Saja (Viewer)" else "Mode: Bisa Edit (Editor)",
                                fontFamily = QuicksandFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (isViewerMode) "Simulasi hak akses tamu 'Lihat Saja'" else "Kamu memiliki akses penuh mendekorasi",
                                fontFamily = QuicksandFontFamily,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = isViewerMode,
                        onCheckedChange = { onToggleViewerMode() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Daftar Kolaborator
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Daftar Teman (${collaborators.size + 1})",
                    fontFamily = QuicksandFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                TextButton(
                    onClick = {
                        showInviteForm = !showInviteForm
                        SoundManager.playClick()
                    }
                ) {
                    Icon(
                        if (showInviteForm) Icons.Default.ExpandLess else Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (showInviteForm) "Tutup Form" else "Undang Teman",
                        fontFamily = QuicksandFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Form Undang Teman jika dibuka
            if (showInviteForm) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Kirim Undangan Kolaborasi",
                            fontFamily = QuicksandFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = inviteName,
                            onValueChange = { inviteName = it },
                            placeholder = { Text("Nama Teman (misal: Rian)", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = inviteEmail,
                            onValueChange = { inviteEmail = it },
                            placeholder = { Text("Email teman (misal: rian@estetik.id)", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Kelola Izin Akses Teman:",
                            fontFamily = QuicksandFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            FilterChip(
                                selected = invitePermission == "CAN_EDIT",
                                onClick = {
                                    invitePermission = "CAN_EDIT"
                                    SoundManager.playClick()
                                },
                                label = { Text("✏️ Bisa Edit", fontSize = 12.sp, fontFamily = QuicksandFontFamily) },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = invitePermission == "CAN_VIEW",
                                onClick = {
                                    invitePermission = "CAN_VIEW"
                                    SoundManager.playClick()
                                },
                                label = { Text("👁️ Lihat Saja", fontSize = 12.sp, fontFamily = QuicksandFontFamily) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Button(
                            onClick = {
                                if (inviteName.isNotBlank()) {
                                    onInvite(inviteName, inviteEmail, invitePermission)
                                    inviteName = ""
                                    inviteEmail = ""
                                    showInviteForm = false
                                    Toast.makeText(context, "Undangan berhasil dikirim!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Tambahkan Teman ke Lembaran", fontFamily = QuicksandFontFamily, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // List of Collaborators
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Pemilik Jurnal (Kamu)
                item {
                    CollaboratorItemRow(
                        name = "Kamu (Pemilik)",
                        email = "Kamu yang memulai lembar ini",
                        avatarColor = MaterialTheme.colorScheme.primary,
                        permission = "CAN_EDIT",
                        isOwner = true,
                        isOnline = true,
                        onPermissionChange = {},
                        onRemove = {}
                    )
                }

                // Teman-teman yang diundang
                items(collaborators) { collab ->
                    CollaboratorItemRow(
                        name = collab.name,
                        email = collab.email,
                        avatarColor = Color(collab.avatarColorHex),
                        permission = collab.permission,
                        isOwner = false,
                        isOnline = collab.isOnline,
                        onPermissionChange = { newPerm ->
                            onUpdatePermission(collab.id, newPerm)
                        },
                        onRemove = { onRemoveCollaborator(collab) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Catatan Aktivitas Kolaborasi Terkini
            Text(
                text = "Riwayat Dekorasi Teman",
                fontFamily = QuicksandFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 100.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(activities.take(5)) { act ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${act.authorName} ${act.actionText}",
                            fontFamily = QuicksandFontFamily,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CollaboratorItemRow(
    name: String,
    email: String,
    avatarColor: Color,
    permission: String, // "CAN_EDIT" or "CAN_VIEW"
    isOwner: Boolean,
    isOnline: Boolean,
    onPermissionChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.firstOrNull()?.toString()?.uppercase() ?: "U",
                    fontFamily = QuicksandFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 15.sp
                )
                if (isOnline) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50))
                            .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Nama & Email
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontFamily = QuicksandFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = email,
                    fontFamily = QuicksandFontFamily,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Opsi Kelola Izin Akses
            if (isOwner) {
                AssistChip(
                    onClick = {},
                    label = { Text("Pemilik", fontSize = 11.sp, fontFamily = QuicksandFontFamily) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Dropdown/Toggle Izin: Bisa Edit vs Lihat Saja
                    var expanded by remember { mutableStateOf(false) }

                    Box {
                        AssistChip(
                            onClick = { expanded = true },
                            label = {
                                Text(
                                    if (permission == "CAN_EDIT") "✏️ Edit" else "👁️ Lihat",
                                    fontSize = 11.sp,
                                    fontFamily = QuicksandFontFamily,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text("Bisa Edit", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("Dapat menambah & mengubah stiker", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                },
                                onClick = {
                                    onPermissionChange("CAN_EDIT")
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text("Lihat Saja", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("Hanya dapat membaca & melihat", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                },
                                onClick = {
                                    onPermissionChange("CAN_VIEW")
                                    expanded = false
                                }
                            )
                        }
                    }

                    IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Outlined.PersonRemove, "Hapus", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
