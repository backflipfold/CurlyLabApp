package aps.backflip.curlylab.data.remote.model.request.profile

import kotlinx.serialization.Serializable

@Serializable
data class HairTypeRequest(
    val porosity: String? = null,
    val isColored: Boolean? = null,
    val thickness: String? = null
)