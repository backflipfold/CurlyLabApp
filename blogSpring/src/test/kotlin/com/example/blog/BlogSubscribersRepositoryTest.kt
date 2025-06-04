package com.example.blog

import com.example.blog.model.BlogSubscribers
import com.example.blog.repository.BlogSubscribersRepositoryImpl
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.support.GeneratedKeyHolder
import java.sql.Connection
import java.sql.PreparedStatement
import java.util.*

class BlogSubscribersRepositoryTest {

    private val jdbcTemplate: JdbcTemplate = mock()
    private val repository = BlogSubscribersRepositoryImpl(jdbcTemplate)

    @Test
    fun `add should return true when insert is successful`() {
        val entity = BlogSubscribers(
            blogSubscribersId = UUID.randomUUID(),
            authorId = UUID.randomUUID(),
            subscriberId = UUID.randomUUID()
        )

        val keyHolder = GeneratedKeyHolder()

        // Явно указываем тип PreparedStatementCreator
        whenever(jdbcTemplate.update(any<PreparedStatementCreator>(), any<GeneratedKeyHolder>())).thenAnswer { invocation ->
            val providedKeyHolder = invocation.getArgument<GeneratedKeyHolder>(1)
            // Подделываем, что ключ был сгенерирован
            providedKeyHolder.keyList.add(mapOf("blog_subscribers_id" to UUID.randomUUID()))
            1 // возвращаем, что одна строка была вставлена
        }

        val result = repository.add(entity)

        assertTrue(result)
    }
}
