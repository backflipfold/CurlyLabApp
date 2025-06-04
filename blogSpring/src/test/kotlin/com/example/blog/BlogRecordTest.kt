package com.example.blog

import com.example.blog.model.BlogRecord
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.util.*

class BlogRecordTest {

    @Test
    fun `should create BlogRecord with correct values`() {
        val recordId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val content = "My curly hair routine"
        val tags = listOf("curly", "routine")
        val createdAt = OffsetDateTime.now()

        val record = BlogRecord(
            recordId = recordId,
            userId = userId,
            content = content,
            createdAt = createdAt,
            tags = tags
        )

        assertEquals(recordId, record.recordId)
        assertEquals(userId, record.userId)
        assertEquals(content, record.content)
        assertEquals(tags, record.tags)
        assertEquals(createdAt, record.createdAt)
    }
}
