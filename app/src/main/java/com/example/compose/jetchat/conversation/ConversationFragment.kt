package com.example.compose.jetchat.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.compose.jetchat.MainViewModel
import com.example.compose.jetchat.R
import com.example.compose.jetchat.data.exampleUiState
import com.example.compose.jetchat.theme.JetchatTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ViewWindowInsetObserver
import com.google.accompanist.insets.navigationBarsPadding

/**
 * author：yangweichao@reworldgame.com
 * data: 2022/3/18 14:23
 * 会话
 */
class ConversationFragment : Fragment() {

    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ComposeView(inflater.context).apply {
        layoutParams = LayoutParams(
            MATCH_PARENT,
            MATCH_PARENT
        )
        //使用这个视图创建一个 ViewWindowInsetObserver，然后调用 start() 开始监听。
        // 返回 WindowInsets 实例，允许我们在下面的内容中将其提供给 AmbientWindowInsets
        val windowInsets = ViewWindowInsetObserver(this)
            //我们使用 `windowInsetsAnimationsEnabled` 参数来启用动画插图支持。
            // 这允许我们的 `ConversationContent` 在进入和退出屏幕时使用屏幕键盘 (IME) 进行动画处理。
            .start(windowInsetsAnimationsEnabled = true)
        setContent {
            CompositionLocalProvider(
                LocalBackPressedDispatcher provides requireActivity().onBackPressedDispatcher,
                LocalWindowInsets provides windowInsets
            ) {
                JetchatTheme {
                    ConversationContent(
                        uiState = exampleUiState,
                        navigateToProfile = { user ->
                        // Click callback
                        val bundle = bundleOf("userId" to user)
                        findNavController().navigate(R.id.nav_profile, bundle)
                    }, onNavIconPressed = {
                        activityViewModel.openDrawer()
                    }, modifier = Modifier.navigationBarsPadding(bottom = false))

                }
            }

        }


    }

}
