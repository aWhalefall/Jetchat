package com.example.compose.jetchat.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.jetchat.FunctionalityNotAvailablePopup
import com.example.compose.jetchat.R
import com.example.compose.jetchat.components.AnimatingFabContent
import com.example.compose.jetchat.components.JetchatAppBar
import com.example.compose.jetchat.components.baselineHeight
import com.example.compose.jetchat.data.colleagueProfile
import com.example.compose.jetchat.data.meProfile
import com.example.compose.jetchat.theme.JetchatTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import java.util.function.DoublePredicate
import kotlin.math.max


/**
 * author：yangweichao@reworldgame.com
 * data: 2022/3/21 11:19
 * 用户详情界面类
 */

@Composable
fun ProfileError() {
    Text(stringResource(R.string.profile_error))
}


@Composable
fun ProfileScreen(userData: ProfileScreenState, onNavIconPressed: () -> Unit = { }) {
    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup {
            functionalityNotAvailablePopupShown = false
        }
    }
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize()) {
        JetchatAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            onNavIconPressed = onNavIconPressed,
            title = {},
            actions = {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    // More icon
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        modifier = Modifier
                            .clickable(onClick = { functionalityNotAvailablePopupShown = true })
                            .padding(horizontal = 12.dp, vertical = 16.dp)
                            .height(24.dp),
                        contentDescription = stringResource(id = R.string.more_options)
                    )
                }
            })
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    ProfileHeader(scrollState, userData, this@BoxWithConstraints.maxHeight)

                    UserInfoFields(userData, this@BoxWithConstraints.maxHeight)
                }
            }
            ProfileFab(
                extended = scrollState.value == 0,
                userIsMe = userData.isMe(), modifier = Modifier.align(Alignment.BottomEnd),
                onFabClicked = {
                    functionalityNotAvailablePopupShown = true
                })
        }
    }
}

@Composable
fun ProfileFab(
    extended: Boolean,
    userIsMe: Boolean,
    modifier: Modifier = Modifier,
    onFabClicked: () -> Unit = {}
) {
    //防止在合成期间执行多个调用
    key(userIsMe) {
        FloatingActionButton(
            onClick = onFabClicked,
            modifier = modifier
                .padding(16.dp)
                .navigationBarsPadding()
                .height(48.dp)
                .widthIn(48.dp),
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ) {
            AnimatingFabContent(
                icon = {
                    Icon(
                        imageVector = if (userIsMe) {
                            Icons.Outlined.Create
                        } else {
                            Icons.Outlined.Chat
                        }, contentDescription = stringResource(
                            id = if (userIsMe) {
                                R.string.edit_profile
                            } else {
                                R.string.message
                            }
                        )
                    )
                }, text = {
                    Text(
                        text = stringResource(
                            id = if (userIsMe) {
                                R.string.edit_profile
                            } else {
                                R.string.message
                            }
                        )
                    )
                }, extended = extended
            )

        }
    }


}

@Composable
fun UserInfoFields(userData: ProfileScreenState, containerHeight: Dp) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        NameAndPosition(userData)


        ProfileProperty(stringResource(R.string.display_name), userData.displayName)

        ProfileProperty(stringResource(R.string.status), userData.status)

        ProfileProperty(stringResource(R.string.twitter), userData.twitter, isLink = true)

        userData.timeZone?.let {
            ProfileProperty(stringResource(R.string.timezone), userData.timeZone)
        }
        //添加一个始终显示部分（320.dp）字段列表的分隔符，无论设备如何，以便始终将一些内容留在顶部
        Spacer(Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
fun ProfileProperty(lable: String, value: String, isLink: Boolean = false) {

    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Divider()
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = lable,
                modifier = Modifier.baselineHeight(24.dp),
                style = MaterialTheme.typography.caption
            )
        }
        val style = if (isLink) {
            MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary)
        } else {
            MaterialTheme.typography.body1
        }

        Text(text = value, modifier = Modifier.baselineHeight(24.dp), style = style)

    }


}


@Composable
fun Position(userData: ProfileScreenState, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(text = userData.position, modifier = Modifier, style = MaterialTheme.typography.body1)
    }
}

@Composable
fun NameAndPosition(userData: ProfileScreenState) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Name(userData, modifier = Modifier.baselineHeight(32.dp))
        Position(
            userData, modifier = Modifier
                .padding(bottom = 20.dp)
                .baselineHeight(24.dp)
        )

    }
}

@Composable
private fun Name(userData: ProfileScreenState, modifier: Modifier = Modifier) {
    Text(
        text = userData.name,
        modifier = modifier,
        style = MaterialTheme.typography.h5
    )
}

@Composable
fun ProfileHeader(
    scrollState: ScrollState,
    userData: ProfileScreenState,
    containerHeight: Dp
) {
    val offset = (scrollState.value / 2)
    val offsetDp = with(LocalDensity.current) { offset.toDp() }
    userData.photo?.let {
        Image(
            modifier = Modifier
                .heightIn(max = containerHeight / 2)
                .fillMaxWidth()
                .padding(top = offsetDp),
            painter = painterResource(id = it),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }

}

@Preview(widthDp = 640, heightDp = 360)
@Composable
fun ConvPreviewLandscapeMeDefault() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        JetchatTheme {
            ProfileScreen(meProfile)
        }
    }
}

@Preview(widthDp = 360, heightDp = 480)
@Composable
fun ConvPreviewPortraitMeDefault() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        JetchatTheme {
            ProfileScreen(meProfile)
        }
    }
}

@Preview(widthDp = 360, heightDp = 480)
@Composable
fun ConvPreviewPortraitOtherDefault() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        JetchatTheme {
            ProfileScreen(colleagueProfile)
        }
    }
}

@Preview
@Composable
fun ProfileFabPreview() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        JetchatTheme {
            ProfileFab(extended = true, userIsMe = false)
        }
    }
}
