package com.example.blog.service

import com.example.blog.model.BlogRecord
import com.example.blog.repository.BaseInterfaceRepository
import com.example.blog.repository.BlogRecordRepository
import org.springframework.stereotype.Service
import java.util.*

interface BlogRecordService : BaseInterfaceRepository<BlogRecord> {
    fun findPostsByUser(userId: UUID): List<BlogRecord>
    fun findPostsBySubscribedUsers(subscriberId: UUID): List<BlogRecord>
    fun findRecommendedPostsForUser(userId: UUID): List<BlogRecord>
    fun getPostsByTags(tags: List<String>): List<BlogRecord>
    fun getPostByPartOfContent(word: String): List<BlogRecord>
    fun getPostByPartOfContentAndTag(word: String): List<BlogRecord>
}

@Service
class BlogRecordServiceImpl(private val blogRecordRepository: BlogRecordRepository) : BlogRecordService {
    override fun findPostsByUser(userId: UUID): List<BlogRecord> {
        return blogRecordRepository.findPostsByUser(userId)
    }

    override fun findPostsBySubscribedUsers(subscriberId: UUID): List<BlogRecord> {
        return blogRecordRepository.findPostsBySubscribedUsers(subscriberId)
    }

    override fun findRecommendedPostsForUser(userId: UUID): List<BlogRecord> {
        return blogRecordRepository.findRecommendedPostsForUser(userId)
    }

    override fun getPostsByTags(tags: List<String>): List<BlogRecord> {
        return blogRecordRepository.getPostsByTags(tags)
    }

    override fun getPostByPartOfContent(word: String): List<BlogRecord> {
        return blogRecordRepository.getPostByPartOfContent(word)
    }

    override fun getPostByPartOfContentAndTag(word: String): List<BlogRecord> {
        return blogRecordRepository.getPostByPartOfContentAndTag(word)
    }

    override fun findAll(): List<BlogRecord> {
        return blogRecordRepository.findAll()
    }

    override fun read(id: UUID): BlogRecord? {
        return blogRecordRepository.read(id)
    }

    override fun add(entity: BlogRecord): Boolean {
        return blogRecordRepository.add(entity)
    }

    override fun delete(id: UUID): Boolean {
        return blogRecordRepository.delete(id)
    }

    override fun edit(id: UUID, entity: BlogRecord): BlogRecord? {
        return blogRecordRepository.edit(id, entity)
    }


}