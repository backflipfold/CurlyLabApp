package com.example.blog.model

import java.util.*

data class UserHairType(
    val porosity: String,
    val isColored: Boolean,
    val thickness: String,
    val userId: UUID
)