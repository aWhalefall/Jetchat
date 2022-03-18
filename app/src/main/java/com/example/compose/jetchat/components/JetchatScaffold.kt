package com.example.compose.jetchat.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import com.example.compose.jetchat.theme.JetchatTheme


@Composable
fun JetChatScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onProfileClicked: (String) -> Unit,
    onChatClicked: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    JetchatTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                JetChatDrawer(
                    onProfileClicked = onProfileClicked,
                    onChatClicked = onChatClicked
                )
            }, content = content
        )
    }

}