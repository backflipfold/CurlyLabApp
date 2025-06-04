package aps.backflip.curlylab.presentation.blogrecords.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import aps.backflip.curlylab.R
import aps.backflip.curlylab.domain.model.blog.BlogRecord
import aps.backflip.curlylab.ui.theme.AccentPink
import aps.backflip.curlylab.ui.theme.DarkGreen
import aps.backflip.curlylab.ui.theme.LightBeige
import aps.backflip.curlylab.ui.theme.LightPink
import aps.backflip.curlylab.ui.theme.PurpleGrey80
import coil.compose.AsyncImage
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BlogRecordCard(
    navController: NavController,
    blogRecord: BlogRecord,
    modifier: Modifier = Modifier,
    isAuthor: Boolean,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(LightBeige)
            .padding(12.dp)
            .clickable {
                if (!isAuthor) {
                    navController.navigate("notmyprofile/${blogRecord.userId}")
                }
            }
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(LightPink),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = R.drawable.curly_wave,
                        contentDescription = "User Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = blogRecord.userName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (isAuthor) {
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = DarkGreen.copy(alpha = 0.7f)
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .zIndex(1f)
                                .background(PurpleGrey80)
                        ) {
                            DropdownMenuItem(onClick = {
                                onEdit()
                                expanded = false
                            },
                                text = {
                                    Text(
                                        "Редактировать",
                                        color = Color.White
                                    )
                                })
                            DropdownMenuItem(onClick = {
                                onDelete()
                                expanded = false
                            },
                                text = {
                                    Text(
                                        "Удалить",
                                        color = Color.White
                                    )
                                })
                        }
                    }
                }

            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = blogRecord.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                blogRecord.tags.forEach { tag ->
                    AssistChip(
                        onClick = {},//TODO
                        label = {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = AccentPink.copy(alpha = 0.2f),
                            labelColor = AccentPink
                        ),
                        border = null,
                        modifier = Modifier.height(24.dp)
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                val formattedDate =
                    blogRecord.createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}




