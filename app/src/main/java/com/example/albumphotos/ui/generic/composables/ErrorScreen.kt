package com.example.albumphotos.ui.generic.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.albumphotos.R
import com.example.albumphotos.ui.theme.AlbumPhotosTheme
import com.example.albumphotos.ui.theme.Spacing

@Composable
fun FullScreenError(
    onClickRetry: (() -> Unit)?,
    modifier: Modifier = Modifier,
) = Surface(
    modifier = modifier.fillMaxSize()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_error_outline),
            tint = MaterialTheme.colorScheme.error,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
        )
        Text(
            text = stringResource(R.string.error_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(top = Spacing.x2),
        )
        onClickRetry?.let { onRetry ->
            Button(
                onClick = onRetry,
                modifier = Modifier.padding(top = Spacing.x2),
            ) {
                Text(
                    text = stringResource(R.string.error_button),
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ErrorPreview() {
    AlbumPhotosTheme {
        FullScreenError(onClickRetry = {})
    }
}
