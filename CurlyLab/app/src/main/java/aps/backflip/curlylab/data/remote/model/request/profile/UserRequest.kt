package aps.backflip.curlylab.data.remote.model.request.profile

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val username: String,
    val language: String = "ru"
)