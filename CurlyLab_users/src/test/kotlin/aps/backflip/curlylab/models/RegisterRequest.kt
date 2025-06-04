package aps.backflip.curlylab.models

import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class RegisterRequestTest {

    @Test
    fun testSerializationAndDeserialization() {
        val original = RegisterRequest(
            username = "curlygirl",
            email = "curly@example.com",
            password = "secure123"
        )

        val json = Json.encodeToString(RegisterRequest.serializer(), original)
        val decoded = Json.decodeFromString(RegisterRequest.serializer(), json)

        assertEquals(original, decoded)
    }

    @Test
    fun testDefaultLanguageValue() {
        val request = RegisterRequest(
            username = "test",
            email = "test@mail.com",
            password = "pass"
        )

        assertEquals("ru", request.language)
    }
}
