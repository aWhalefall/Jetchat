package com.example.compose.jetchat.profile

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.compose.jetchat.data.colleagueProfile
import com.example.compose.jetchat.data.meProfile


class ProfileViewModel : ViewModel() {
    private var userId: String = ""
    private val _userData = MutableLiveData<ProfileScreenState>()
    val userData: LiveData<ProfileScreenState> = _userData

    fun setUserId(newUserId: String?) {
        if (newUserId != userId) {
            userId = newUserId ?: meProfile.userId
        }
        _userData.value = if (userId == meProfile.userId || userId == meProfile.displayName) {
            meProfile
        } else {
            colleagueProfile
        }
    }


}


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