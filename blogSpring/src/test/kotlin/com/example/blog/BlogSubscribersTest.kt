package com.example.blog

import com.example.blog.model.BlogSubscribers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class BlogSubscribersTest {

    @Test
    fun `should create BlogSubscribers correctly`() {
        val id = UUID.randomUUID()
        val authorId = UUID.randomUUID()
        val subId = UUID.randomUUID()

        val subscriber = BlogSubscribers(id, authorId, subId)

        assertEquals(id, subscriber.blogSubscribersId)
        assertEquals(authorId, subscriber.authorId)
        assertEquals(subId, subscriber.subscriberId)
    }
}
