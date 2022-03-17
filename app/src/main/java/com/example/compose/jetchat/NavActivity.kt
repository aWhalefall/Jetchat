package com.example.compose.jetchat

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.view.WindowCompat
import com.example.compose.jetchat.components.JetchatScaffold
import com.example.compose.jetchat.conversation.BackPressHandler
import com.example.compose.jetchat.conversation.LocalBackPressedDispatcher
import com.google.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.launch

/**
 * author：yangweichao@reworldgame.com
 * data: 2022/3/17 18:54
 *  一个返回键需要使用超过三个 附带效应api 这不繁琐吗？
 */
class NavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            //透传onBackPressed类
            ProvideWindowInsets(consumeWindowInsets = false) {
                CompositionLocalProvider(LocalBackPressedDispatcher provides this.onBackPressedDispatcher) {

                    //draw snackBar
                    val scaffoldState = rememberScaffoldState()
                    val drawerOpen = true

                    if (drawerOpen) {
                        // Open drawer and reset state in VM.
                        LaunchedEffect(Unit){
                            scaffoldState.drawerState.open()
                        }

                    }
                    // Open drawer and reset state in VM.
                    val scope = rememberCoroutineScope()
                    if (scaffoldState.drawerState.isOpen) {
                        BackPressHandler {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    }
                    //
                    JetchatScaffold(
                        scaffoldState

                    )

                }
            }
        }
    }

}