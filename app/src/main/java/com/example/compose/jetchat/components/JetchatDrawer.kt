package com.example.compose.jetchat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.jetchat.R
import com.example.compose.jetchat.data.colleagueProfile
import com.example.compose.jetchat.data.meProfile
import com.example.compose.jetchat.theme.JetchatTheme
import com.google.accompanist.insets.statusBarsHeight

@Composable
fun ColumnScope.JetChatDrawer(
    onProfileClicked: (String) -> Unit,
    onChatClicked: (String) -> Unit
) {

    Spacer(modifier = Modifier.statusBarsHeight())
    DrawerHeader()
    Divider()
    DrawerItemHeader("Chats")
    ChatItem("composers", true) { onChatClicked("composers") }
    ChatItem("droidcon-nyc", false) { onChatClicked("droidcon-nyc") }
    DrawerItemHeader("Recent Profiles")
    ProfileItem("Ali Conors (you)", meProfile.photo) {
        onProfileClicked(meProfile.userId)
    }
    ProfileItem("Taylor Brooks", colleagueProfile.photo) {
        onProfileClicked(colleagueProfile.userId)
    }
}

@Composable
fun ProfileItem(text: String, profilePic: Int?, onProfileClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onProfileClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            val widthPaddingModifier = Modifier
                .padding(8.dp)
                .size(24.dp)
            if (profilePic != null) {
                Image(
                    painter = painterResource(id = profilePic),
                    contentDescription = null,
                    modifier = widthPaddingModifier.then(Modifier.clip(CircleShape)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Spacer(modifier = widthPaddingModifier)
            }
            Text(
                text,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(8.dp)
            )

        }

    }
}

@Composable
fun ChatItem(text: String, selected: Boolean, onChatClicked: () -> Unit) {

    val background = if (selected) {
        Modifier.background(MaterialTheme.colors.primary.copy(alpha = 0.08f))
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .then(background)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onChatClicked), verticalAlignment = Alignment.CenterVertically
    ) {
        val iconTint = if (selected) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_jetchat),
            tint = iconTint,
            modifier = Modifier.padding(8.dp),
            contentDescription = null
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text,
                style = MaterialTheme.typography.body2,
                color = if (selected) MaterialTheme.colors.primary else LocalContentColor.current,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

}

@Composable
fun DrawerItemHeader(text: String) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = text,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun DrawerHeader() {
    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_jetchat),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.jetchat_logo), contentDescription = null,
            Modifier.padding(start = 8.dp)
        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun SplashScreenView() {
    JetchatTheme {
        Surface {
            Column {
                JetChatDrawer(onProfileClicked = {}, onChatClicked = {})
            }
        }
    }
}


