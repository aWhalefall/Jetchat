package com.example.compose.jetchat.conversation

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.*


@Composable
fun BackPressHandler(onBackPressed:()->Unit){

     //① 在效应中引用 onBackPressed，在效应中引用某个值，该效应在值改变时不应重启
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallBack = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               currentOnBackPressed
            }

        }
    }

    val backDispatcher = LocalBackPressedDispatcher.current

    //② 需要清理的效应
    DisposableEffect(backDispatcher){
        backDispatcher.addCallback(backCallBack)

        onDispose {
            backCallBack.remove()
        }
    }

}

val LocalBackPressedDispatcher = staticCompositionLocalOf<OnBackPressedDispatcher>{
    error("No Back Dispatcher provided")
}