package aps.backflip.curlylab.models

import org.junit.Assert.*
import org.junit.Test

class HairTypeRequestTest {

    @Test
    fun testNullableFields() {
        val request = HairTypeRequest()
        assertNull(request.porosity)
        assertNull(request.isColored)
        assertNull(request.thickness)
    }

    @Test
    fun testNonNullValues() {
        val request = HairTypeRequest(
            porosity = "Низкая",
            isColored = true,
            thickness = "Толстые"
        )

        assertEquals("Низкая", request.porosity)
        assertTrue(request.isColored!!)
        assertEquals("Толстые", request.thickness)
    }
}
