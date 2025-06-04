package aps.backflip.curlylab.presentation.products.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import aps.backflip.curlylab.ui.theme.AccentPink
import aps.backflip.curlylab.ui.theme.DarkGreen
import aps.backflip.curlylab.ui.theme.LightBeige
import aps.backflip.curlylab.ui.theme.BrightPink
import aps.backflip.curlylab.ui.theme.Golos
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun TagFilterSheet(
    selectedTags: Map<String, String?>,
    onTagClick: (String, String) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(AccentPink.copy(alpha = 0.1f))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FilterGroup(
            title = "Пористость",
            options = listOf("Сильная", "Средняя", "Низкая"),
            selected = selectedTags["Пористость"],
            onClick = { onTagClick("Пористость", it) }
        )
        FilterGroup(
            title = "Толщина",
            options = listOf("Толстые", "Тонкие"),
            selected = selectedTags["Толщина"],
            onClick = { onTagClick("Толщина", it) }
        )
        FilterGroup(
            title = "Окрашенность",
            options = listOf("Да", "Нет"),
            selected = selectedTags["Окрашенность"],
            onClick = { onTagClick("Окрашенность", it) }
        )
    }
}

@Composable
private fun FilterGroup(
    title: String,
    options: List<String>,
    selected: String?,
    onClick: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontFamily = Golos),
            color = DarkGreen
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { tag ->
                val isSelected = selected == tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) BrightPink else LightBeige)
                        .clickable { onClick(tag) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .weight(1f)
                        .wrapContentWidth()
                ) {
                    Text(
                        text = tag,
                        color = if (isSelected) Color.White else DarkGreen,
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = Golos),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

