package com.example.compose.jetchat.profile

import androidx.annotation.DrawableRes
import com.example.compose.jetchat.data.meProfile


data class ProfileScreenState(
    val userId: String, @DrawableRes val photo: Int?,
    val name: String,
    val status: String,
    val displayName: String,
    val position: String,
    val twitter: String = "",
    val timeZone: String?,
    val commonChannels: String?,
) {
    fun isMe() = userId == meProfile.userId
}