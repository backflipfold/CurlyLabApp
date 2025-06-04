package com.example.blog.repository

import com.example.blog.model.BlogSubscribers
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement
import java.util.UUID

interface BlogSubscribersRepository : BaseInterfaceRepository<BlogSubscribers> {
    fun readAllSubscriptionsForUser(userId: UUID): Int
    fun readAllSubscribersForAuthor(authorId: UUID): Int
    fun findIdByAuthorAndSubscriber(authorId: UUID, subscriberId: UUID): BlogSubscribers?
}

@Repository
class BlogSubscribersRepositoryImpl(private val jdbcTemplate: JdbcTemplate) : BlogSubscribersRepository {
    private val blogSubscribersSql = """
        SELECT bs.blog_subscribers_id, 
               a.user_id AS author_id,
               s.user_id AS subscriber_id,
        FROM blog_subscribers bs
    """.trimIndent()

    private val blogSubscriberByIdSql = """
        SELECT bs.blog_subscribers_id, 
               a.user_id AS author_id,
               s.user_id AS subscriber_id,
        FROM blog_subscribers bs
        WHERE bs.blog_subscribers_id = ?
    """.trimIndent()

    override fun findAll(): List<BlogSubscribers> {
        return jdbcTemplate.query(blogSubscribersSql) { rs, _ ->
            BlogSubscribers(
                blogSubscribersId = rs.getObject("blog_subscribers_id", UUID::class.java),
                authorId = rs.getObject("author_id", UUID::class.java),
                subscriberId = rs.getObject("subscriber_id", UUID::class.java)
            )
        }
    }

    override fun read(id: UUID): BlogSubscribers? {
        return jdbcTemplate.query(blogSubscriberByIdSql, arrayOf(id)) { rs, _ ->
            BlogSubscribers(
                blogSubscribersId = rs.getObject("blog_subscribers_id", UUID::class.java),
                authorId = rs.getObject("author_id", UUID::class.java),
                subscriberId = rs.getObject("subscriber_id", UUID::class.java),
            )
        }.firstOrNull()
    }

    //подписка
    override fun add(entity: BlogSubscribers): Boolean {
        val sql = """INSERT INTO blog_subscribers (blog_subscribers_id, author_id, subscriber_id) 
                 VALUES (?, ?, ?)""".trimIndent()
        val generatedKeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setObject(1, entity.blogSubscribersId)
            ps.setObject(2, entity.authorId)
            ps.setObject(3, entity.subscriberId)
            ps
        }, generatedKeyHolder)

        return generatedKeyHolder.keyList.isNotEmpty()
    }


    override fun delete(id: UUID): Boolean {
        val sql = "DELETE FROM blog_subscribers WHERE blog_subscribers_id = ?"
        val rowsAffected = jdbcTemplate.update(sql, id)
        return rowsAffected > 0
    }

    override fun edit(id: UUID, entity: BlogSubscribers): BlogSubscribers? {
        val sql = """
        UPDATE blog_subscribers 
        SET author_id = ?, subscriber_id = ? 
        WHERE blog_subscribers_id = ?
    """.trimIndent()

        val rowsAffected = jdbcTemplate.update(sql, entity.authorId, entity.subscriberId, id)

        return if (rowsAffected > 0) {
            entity
        } else {
            null
        }
    }

    //получение всех подписок
    override fun readAllSubscriptionsForUser(userId: UUID): Int {
        val sql = "SELECT bs.blog_subscribers_id, bs.author_id, " +
                "bs.subscriber_id " +
                "FROM blog_subscribers bs " +
                "WHERE bs.subscriber_id = ?"

        return jdbcTemplate.query(sql, arrayOf(userId)) { rs, _ ->
            rs.getObject("author_id", UUID::class.java)
        }.size
    }

    //получение подписчиков
    override fun readAllSubscribersForAuthor(authorId: UUID): Int {
        val sql = """
        SELECT bs.blog_subscribers_id, bs.subscriber_id
        FROM blog_subscribers bs
        WHERE bs.author_id = ?
    """

        return jdbcTemplate.query(sql, arrayOf(authorId)) { rs, _ ->
            rs.getObject("subscriber_id", UUID::class.java)
        }.size
    }

    override fun findIdByAuthorAndSubscriber(authorId: UUID, subscriberId: UUID): BlogSubscribers? {
        val id = jdbcTemplate.query(blogSubscriberByAuthorAndSubscriberSql, arrayOf(authorId, subscriberId)) { rs, _ ->
            rs.getObject("blog_subscribers_id", UUID::class.java)
        }.firstOrNull()
        return if (id == null) {
            null
        } else {
            BlogSubscribers(
                blogSubscribersId = id,
                authorId = authorId,
                subscriberId = subscriberId
            )
        }
    }

    private val blogSubscriberByAuthorAndSubscriberSql = """
        SELECT bs.blog_subscribers_id, 
               bs.author_id,
               bs.subscriber_id
        FROM blog_subscribers bs
        WHERE author_id = ? AND subscriber_id = ?
    """.trimIndent()

}