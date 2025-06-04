package com.example.blog.repository

import com.example.blog.model.BlogRecord
import com.example.blog.model.UserHairType
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

interface BlogRecordRepository : BaseInterfaceRepository<BlogRecord> {
    fun findPostsByUser(userId: UUID): List<BlogRecord>
    fun findPostsBySubscribedUsers(subscriberId: UUID): List<BlogRecord>
    fun findRecommendedPostsForUser(userId: UUID): List<BlogRecord>
    fun getPostsByTags(tags: List<String>): List<BlogRecord>
    fun getPostByPartOfContent(word: String): List<BlogRecord>
    fun getPostByPartOfContentAndTag(word: String): List<BlogRecord>
}

@Repository
class BlogRecordRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : BlogRecordRepository {
    val objectMapper = jacksonObjectMapper()

    private val findAllSql = """
        SELECT br.record_id, br.user_id AS author_id, br.content, br.created_at, br.tags 
        FROM blog_records br 
        ORDER BY br.created_at DESC
        LIMIT 100
""".trimIndent()

    override fun findAll(): List<BlogRecord> {
        return jdbcTemplate.query(findAllSql) { rs, _ ->
            val tagsJson = rs.getString("tags")
            val tags: List<String> = objectMapper.readValue(tagsJson)

            BlogRecord(
                recordId = rs.getObject("record_id", UUID::class.java),
                userId = rs.getObject("author_id", UUID::class.java),
                content = rs.getString("content"),
                createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
                tags = tags
            )
        }
    }

    private val readSql = """
    SELECT br.record_id, br.user_id AS author_id, br.content, br.created_at, br.tags 
    FROM blog_records br 
    WHERE br.record_id = ?
""".trimIndent()

    override fun read(id: UUID): BlogRecord? {
        return jdbcTemplate.query(readSql, arrayOf(id)) { rs, _ ->
            val tagsJson = rs.getString("tags")
            val tags: List<String> = objectMapper.readValue(tagsJson)

            BlogRecord(
                recordId = rs.getObject("record_id", UUID::class.java),
                userId = rs.getObject("author_id", UUID::class.java),
                content = rs.getString("content"),
                createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
                tags = tags
            )
        }.firstOrNull()
    }

    private val addSql = """
    INSERT INTO blog_records (record_id, user_id, content, created_at, tags) 
    VALUES (?, ?, ?, ?, ?::jsonb)
""".trimIndent()

    //создание поста
    override fun add(entity: BlogRecord): Boolean {
        val objectMapper = ObjectMapper()
        val tagsJson = objectMapper.writeValueAsString(entity.tags)

        val rowsAffected = jdbcTemplate.update(addSql) { preparedStatement ->
            preparedStatement.setObject(1, entity.recordId)
            preparedStatement.setObject(2, entity.userId)
            preparedStatement.setString(3, entity.content)
            preparedStatement.setObject(4, entity.createdAt)

            preparedStatement.setObject(5, tagsJson)
        }

        return if (rowsAffected > 0) {
            read(entity.recordId) != null
        } else {
            false
        }
    }

    private val deleteSql = """
    DELETE FROM blog_records WHERE record_id = ?
""".trimIndent()

    //удаление поста
    override fun delete(id: UUID): Boolean {
        val rowsAffected = jdbcTemplate.update(deleteSql, id)
        return rowsAffected > 0
    }

    private val editSql = """
    UPDATE blog_records 
    SET content = ?, tags = ?::jsonb 
    WHERE record_id = ?
""".trimIndent()

    override fun edit(id: UUID, entity: BlogRecord): BlogRecord? {
        val objectMapper = ObjectMapper()
        val tagsJson = objectMapper.writeValueAsString(entity.tags)

        val rowsAffected = jdbcTemplate.update(editSql) { preparedStatement ->
            preparedStatement.setString(1, entity.content)
            preparedStatement.setObject(2, tagsJson)
            preparedStatement.setObject(3, id)
        }

        return if (rowsAffected > 0) {
            read(id)
        } else {
            null
        }
    }

    private val findPostsByUserSql = """
    SELECT br.record_id, br.user_id AS author_id, br.content, br.created_at, br.tags 
    FROM blog_records br
    WHERE br.user_id = ?
    ORDER BY br.created_at DESC
""".trimIndent()

    override fun findPostsByUser(userId: UUID): List<BlogRecord> {
        return jdbcTemplate.query(findPostsByUserSql, arrayOf(userId)) { rs, _ ->
            val tagsJson = rs.getString("tags")
            val tags: List<String> = objectMapper.readValue(tagsJson)

            BlogRecord(
                recordId = rs.getObject("record_id", UUID::class.java),
                userId = rs.getObject("author_id", UUID::class.java),
                content = rs.getString("content"),
                createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
                tags = tags
            )
        }
    }

    private val findPostsBySubscribedUsersSql = """
    SELECT br.record_id, br.user_id AS author_id, br.content, br.created_at, br.tags 
    FROM blog_records br
    JOIN blog_subscribers bs ON br.user_id = bs.author_id
    WHERE bs.subscriber_id = ?
    ORDER BY br.created_at DESC
    LIMIT 100
""".trimIndent()

    override fun findPostsBySubscribedUsers(subscriberId: UUID): List<BlogRecord> {
        return jdbcTemplate.query(findPostsBySubscribedUsersSql, arrayOf(subscriberId)) { rs, _ ->
            val tagsJson = rs.getString("tags")
            val tags: List<String> = objectMapper.readValue(tagsJson)

            BlogRecord(
                recordId = rs.getObject("record_id", UUID::class.java),
                userId = rs.getObject("author_id", UUID::class.java),
                content = rs.getString("content"),
                createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
                tags = tags
            )
        }
    }

    override fun findRecommendedPostsForUser(userId: UUID): List<BlogRecord> {
        val hairType = getUserHairType(userId)
        if (hairType != null) {
            val tags = listOf(
                when (hairType.isColored) {
                    true -> "окрашенные"
                    else -> "неокрашенные"
                },
                hairType.thickness,
                hairType.porosity
            )
            return getPostsByTags(tags)
        } else {
            return findAll()
        }
    }

    private val getPostsByTagsSql = """
        SELECT br.record_id, br.user_id AS author_id, br.content, br.created_at, br.tags 
        FROM blog_records br
        WHERE br.tags @> ?::jsonb
        ORDER BY br.created_at DESC
        LIMIT 100
""".trimIndent()

    override fun getPostsByTags(tags: List<String>): List<BlogRecord> {
        val tagsJson = objectMapper.writeValueAsString(tags)

        return jdbcTemplate.query(getPostsByTagsSql, arrayOf(tagsJson)) { rs, _ ->
            val tagsJsonFromDb = rs.getString("tags")
            val tagsList: List<String> = objectMapper.readValue(tagsJsonFromDb)

            BlogRecord(
                recordId = rs.getObject("record_id", UUID::class.java),
                userId = rs.getObject("author_id", UUID::class.java),
                content = rs.getString("content"),
                createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
                tags = tagsList
            )
        }
    }

    private val findPostsByContentSql = """
    SELECT br.record_id, br.user_id AS author_id, br.content, br.created_at, br.tags 
    FROM blog_records br
    WHERE br.content ILIKE ?
    ORDER BY br.created_at DESC
""".trimIndent()

    override fun getPostByPartOfContent(word: String): List<BlogRecord> {
        val searchWord = "%$word%"
        return jdbcTemplate.query(findPostsByContentSql, arrayOf(searchWord)) { rs, _ ->
            val tagsJson = rs.getString("tags")
            val tags: List<String> = objectMapper.readValue(tagsJson)
            BlogRecord(
                recordId = rs.getObject("record_id", UUID::class.java),
                userId = rs.getObject("author_id", UUID::class.java),
                content = rs.getString("content"),
                createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
                tags = tags
            )
        }
    }

    override fun getPostByPartOfContentAndTag(word: String): List<BlogRecord> {
        val listFindInTags = getPostsByTags(listOf(word))
        val listFindInContent = getPostByPartOfContent(word)
        return listFindInContent + listFindInTags
    }


    private val readUserHairTypeSql = """
        SELECT uht.user_id, uht.is_colored, uht.porosity, uht.thickness 
        FROM user_hair_types uht 
        WHERE uht.user_id = ?
    """.trimIndent()

    private fun getUserHairType(userId: UUID): UserHairType? {
        return jdbcTemplate.query(readUserHairTypeSql, arrayOf(userId)) { rs, _ ->
            UserHairType(
                userId = rs.getObject("user_id", UUID::class.java),
                isColored = rs.getBoolean("is_colored"),
                porosity = rs.getString("porosity"),
                thickness = rs.getString("thickness")
            )
        }.firstOrNull()
    }
}
