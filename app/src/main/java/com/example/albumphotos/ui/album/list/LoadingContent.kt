package com.example.albumphotos.ui.album.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.albumphotos.ui.theme.Shapes
import com.example.albumphotos.ui.theme.Spacing
import com.valentinilk.shimmer.shimmer

@Composable
internal fun LoadingContent(
    modifier: Modifier = Modifier,
) = Column(modifier = modifier.verticalScroll(rememberScrollState())) {
    repeat(LOADING_ITEM_COUNT) {
        LoadingItem(
            modifier = Modifier
                .padding(horizontal = Spacing.x1)
                .padding(top = Spacing.x1),
        )
    }
}

@Composable
private fun LoadingItem(
    modifier: Modifier = Modifier
) = Card(
    modifier = modifier
        .clip(shape = Shapes.medium),
    shape = Shapes.medium,
) {
    val shimmerColor = MaterialTheme.colorScheme.secondary
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(Spacing.x2)
            .shimmer(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .border(
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape,
                    width = 1.dp,
                )
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Spacer(
                modifier = Modifier
                    .size(35.dp, 60.dp)
                    .background(color = shimmerColor)
            )
        }
        Spacer(
            modifier = Modifier
                .padding(start = Spacing.x2)
                .height(20.dp)
                .fillMaxWidth()
                .background(color = shimmerColor)
        )
    }
}

private const val LOADING_ITEM_COUNT = 8
