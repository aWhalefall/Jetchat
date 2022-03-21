package com.example.compose.jetchat.conversation

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.jetchat.FunctionalityNotAvailablePopup
import com.example.compose.jetchat.R
import com.example.compose.jetchat.components.JetchatAppBar
import com.example.compose.jetchat.data.exampleUiState
import com.example.compose.jetchat.theme.JetchatTheme
import com.example.compose.jetchat.theme.elevatedSurface
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch

/**
 * author：yangweichao@reworldgame.com
 * data: 2022/3/18 14:48
 * 交流界面
 */

@Composable
fun ConversationContent(
    uiState: ConversationUiState,
    navigateToProfile: (String) -> Unit,
    modifier: Modifier = Modifier,
    onNavIconPressed: () -> Unit = { }
) {

    val authorMe = stringResource(id = R.string.author_me)
    val timeNow = stringResource(id = R.string.now)

    val scrollState = rememberLazyListState()
    //附带效应,在组合函数之外发生状态变化
    //效应是一种可组合函数，该函数不会发出界面，并且在组合完成后产生附带效应。
    val scope = rememberCoroutineScope()

    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                Messages(
                    messages = uiState.messages,
                    navigateToProfile = navigateToProfile,
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState
                )
                // UserInput()
            }

            // Channel name bar floats above the messages
            ChannelNameBar(
                channelName = uiState.channelName,
                channelMembers = uiState.channelMembers, onNavIconPressed = onNavIconPressed,
                modifier = Modifier.statusBarsPadding()
            )
        }

    }
}

@Composable
fun UserInput() {
    // TODO("Not yet implemented")
}

const val ConversationTestTag = "ConversationTestTag"

@Composable
fun Messages(
    messages: List<Message>,
    navigateToProfile: (String) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState
) {
    val scope = rememberCoroutineScope()
    Box(modifier = modifier) {
        val authorMe = stringResource(id = R.string.author_me)
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            contentPadding = rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.statusBars,
                additionalTop = 90.dp
            ), modifier = Modifier
                .testTag(ConversationTestTag)
                .fillMaxSize()
        ) {
            for (index in messages.indices) {
                val prevAuthor = messages.getOrNull(index - 1)?.author
                val nextAuthor = messages.getOrNull(index + 1)?.author

                val content = messages[index]
                val isFirstMessageByAuthor = prevAuthor != content.author
                val isLastMessageByAuthor = nextAuthor != content.author
                //为简单起见硬编码日分隔符
                if (index == messages.size - 1) {
                    item {
                        DayHeader("20 Aug")
                    }
                } else if (index == 2) {
                    item {
                        DayHeader("Today")
                    }
                }
                item {
                    Message(
                        onAuthorClick = { name -> navigateToProfile(name) },
                        msg = content,
                        isUserMe = content.author == authorMe,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor
                    )
                }

            }

        }
        //当用户滚动超过阈值时，会显示跳转到底部按钮。转换为像素
        val jumpthreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()
        }
        //如果第一个可见项目不是第一个或偏移量大于阈值，则显示按钮
        val jumpToBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0 ||
                        scrollState.firstVisibleItemScrollOffset > jumpthreshold
            }
        }
        JumpToBottom(enabled = jumpToBottomButtonEnabled, onClicked = {
            scope.launch {
                scrollState.animateScrollToItem(0)
            }
        }, modifier = Modifier.align(Alignment.BottomCenter))
    }

}

@Composable
fun Message(
    onAuthorClick: (String) -> Unit,
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.secondary
    }

    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier

    Row(modifier = spaceBetweenAuthors) {
        if (isLastMessageByAuthor) {
            Image(
                painter = painterResource(id = msg.authorImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clickable { onAuthorClick }
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(
                        3.dp, MaterialTheme.colors.surface,
                        CircleShape
                    )
                    .clip(CircleShape)
                    .align(Alignment.Top),
            )
        } else {
            // Space under avatar
            Spacer(modifier = Modifier.width(74.dp))
        }
        AuthorAndTextMessage(
            msg = msg,
            isFirstMessageByAuthor,
            isLastMessageByAuthor,
            authorClicked = onAuthorClick,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )

    }

}

@Composable
fun AuthorAndTextMessage(
    msg: Message,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    authorClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (isLastMessageByAuthor) {
            AuthorNameTimesStamp(msg)
        }
        ChatItemBubble(msg, isFirstMessageByAuthor, authorClicked = authorClicked)
        if (isFirstMessageByAuthor) {
            // Last bubble before next author
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Between bubbles
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

}

@Composable
fun AuthorNameTimesStamp(msg: Message) {
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {

        Text(
            text = msg.author,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .alignBy(
                    LastBaseline
                )
                .paddingFrom(LastBaseline, after = 8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        //alpha(ContentAlpha.medium) 区别
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = msg.timestamp,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
    }
}

@Composable
fun ChatItemBubble(msg: Message,
                   lastMessageByAuthor: Boolean,
                   authorClicked: (String) -> Unit) {
    val backgroundBubbleColor = if (MaterialTheme.colors.isLight) {
        Color(0xFFF5F5F5)
    } else {
        MaterialTheme.colors.elevatedSurface(elevation = 2.dp)
    }

    val bubbleShape = if (lastMessageByAuthor) LastChatBubbleShape else ChatBubbleShape

    Column {
        Surface(color = backgroundBubbleColor, shape = bubbleShape) {
            ClickableMessage(messages = msg, authorClicked = authorClicked)
        }
    }
    msg.image?.let {
        Spacer(modifier = Modifier.padding(4.dp))
        Surface(color = backgroundBubbleColor, shape = bubbleShape) {
            Image(
                painter = painterResource(id = it),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(160.dp),
                contentDescription = stringResource(id = R.string.attached_image)
            )
        }
    }

}

@Composable
fun ClickableMessage(messages: Message, authorClicked: (String) -> Unit) {
    val uriHandler = LocalUriHandler.current
    val styleMessage = messageFormatter(text = messages.content)
    ClickableText(text = styleMessage, onClick = {
        styleMessage.getStringAnnotations(start = it, end = it).firstOrNull()?.let { annotation ->
            when (annotation.tag) {
                SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                else ->
                    Unit
            }
        }
    })
}

private val ChatBubbleShape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp)
private val LastChatBubbleShape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 8.dp)

@Composable
fun DayHeader(dayString: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        DayHeaderLine()
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = dayString,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.overline
            )

        }
        DayHeaderLine()
    }
}

@Composable
fun RowScope.DayHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
    )
}

@Preview
@Composable
fun channelBarPrev() {
    JetchatTheme {
        ChannelNameBar(channelName = "composers", channelMembers = 52)
    }
}

@Composable
fun ChannelNameBar(
    channelName: String, channelMembers: Int,
    modifier: Modifier = Modifier,
    onNavIconPressed: () -> Unit = { }
) {
    var functionalityNotAvailablePopupShown by remember {
        mutableStateOf(false)
    }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }

    JetchatAppBar(modifier = modifier,
        onNavIconPressed = onNavIconPressed,
        title = {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = channelName, style = MaterialTheme.typography.subtitle1)
                // Number of members
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = stringResource(R.string.members, channelMembers),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }, actions = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                // Search icon
                Icon(
                    imageVector = Icons.Outlined.Search,
                    modifier = Modifier
                        .clickable(onClick = {
                            functionalityNotAvailablePopupShown = true
                        })
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                        .height(24.dp), contentDescription = stringResource(id = R.string.search)
                )
            }
            // Info icon
            Icon(
                imageVector = Icons.Outlined.Info,
                modifier = Modifier
                    .clickable(onClick = {
                        functionalityNotAvailablePopupShown = true
                    })
                    .padding(horizontal = 12.dp, vertical = 16.dp)
                    .height(24.dp), contentDescription = stringResource(id = R.string.info)
            )
        }
    )

}

@Preview
@Composable
fun DayHeaderPrev() {
    DayHeader("Aug 6")
}

@Preview
@Composable
fun ConversationPrev() {
    ConversationContent(uiState = exampleUiState, {}, modifier = Modifier, {})
}


private val JumpToBottomThreshold = 56.dp

private fun ScrollState.atBottom(): Boolean = value == 0
