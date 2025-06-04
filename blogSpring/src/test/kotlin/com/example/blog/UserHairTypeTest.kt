package com.example.blog

import com.example.blog.model.UserHairType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class UserHairTypeTest {

    @Test
    fun `should create UserHairType with correct values`() {
        val userId = UUID.randomUUID()
        val type = UserHairType(
            porosity = "low",
            isColored = true,
            thickness = "medium",
            userId = userId
        )

        assertEquals("low", type.porosity)
        assertTrue(type.isColored)
        assertEquals("medium", type.thickness)
        assertEquals(userId, type.userId)
    }
}
