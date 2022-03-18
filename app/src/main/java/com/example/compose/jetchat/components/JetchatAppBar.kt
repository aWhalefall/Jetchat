package com.example.compose.jetchat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.jetchat.R
import com.example.compose.jetchat.theme.JetchatTheme
import com.example.compose.jetchat.theme.elevatedSurface


@Composable
fun JetchatAppBar(
    modifier: Modifier = Modifier, onNavIconPressed: () -> Unit = { },
    title: @Composable RowScope.() -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    //此条是半透明的，但高程叠加不适用于半透明颜色。相反，我们从不透明颜色手动计算提升的表面颜色，
// 然后应用我们的 alpha。

    val backgroundColor = MaterialTheme.colors.elevatedSurface(3.dp)
    Column(Modifier.background(backgroundColor.copy(alpha = 0.95f))) {
        TopAppBar(
            modifier = modifier,
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            contentColor = MaterialTheme.colors.onSurface,
            actions = actions, title = {
                Row {
                    title()  // https://issuetracker.google.com/168793068
                }
            }, navigationIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_jetchat),
                    contentDescription = stringResource(id = R.string.back),
                    modifier = Modifier
                        .clickable(onClick = onNavIconPressed)
                        .padding(horizontal = 16.dp)
                )
            }
        )
        Divider()
    }

}

@Preview
@Composable
fun JetchatAppBarPreview() {
    JetchatTheme {
        JetchatAppBar(title = { Text("Preview!") })
    }
}