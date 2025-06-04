package aps.backflip.curlylab.models

import com.benasher44.uuid.uuid4
import kotlinx.datetime.Clock
import org.junit.Assert.*
import org.junit.Test

class UserResponseTest {

    @Test
    fun testUserResponseFields() {
        val id = uuid4()
        val now = Clock.System.now()

        val user = UserResponse(
            id = id,
            username = "curlygirl",
            language = "ru",
            createdAt = now,
            imageUrl = "http://img.com",
            imageFileId = "img123"
        )

        assertEquals("curlygirl", user.username)
        assertEquals("ru", user.language)
        assertEquals("http://img.com", user.imageUrl)
        assertEquals("img123", user.imageFileId)
    }
}
