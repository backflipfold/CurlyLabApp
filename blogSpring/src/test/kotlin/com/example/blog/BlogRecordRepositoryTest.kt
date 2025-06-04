package com.example.blog

import com.example.blog.model.BlogRecord
import com.example.blog.repository.BlogRecordRepositoryImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.jdbc.core.JdbcTemplate
import java.time.OffsetDateTime
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.RowMapper
import java.util.*

class BlogRecordRepositoryTest {

    private val jdbcTemplate = mock<JdbcTemplate>()
    private val repository = BlogRecordRepositoryImpl(jdbcTemplate)

    @Test
    fun `add should return true if insert is successful`() {
        val record = BlogRecord(
            recordId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            content = "Test content",
            createdAt = OffsetDateTime.now(),
            tags = listOf("test", "blog")
        )

        whenever(jdbcTemplate.update(any(), any<PreparedStatementSetter>())).thenReturn(1)
        whenever(jdbcTemplate.query(any<String>(), any<Array<Any>>(), any<RowMapper<BlogRecord>>())).thenReturn(listOf(record))

        val result = repository.add(record)

        assertTrue(result)
        verify(jdbcTemplate).update(any(), any<PreparedStatementSetter>())
    }

    @Test
    fun `delete should return false if no rows affected`() {
        val id = UUID.randomUUID()
        whenever(jdbcTemplate.update(any<String>(), eq(id))).thenReturn(0)

        val result = repository.delete(id)

        assertFalse(result)
    }
}
