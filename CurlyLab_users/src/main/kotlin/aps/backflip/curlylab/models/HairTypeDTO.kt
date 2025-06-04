package aps.backflip.curlylab.models

import kotlinx.serialization.Serializable

@Serializable
data class HairTypeRequest(
    val porosity: String? = null,
    val isColored: Boolean? = null,
    val thickness: String? = null
)

@Serializable
data class HairTypeResponse(
    val porosity: String,
    val isColored: Boolean,
    val thickness: String
)