package aps.backflip.curlylab.domain.repository.blog

import aps.backflip.curlylab.data.remote.model.request.blog.BlogRecordRequest
import aps.backflip.curlylab.domain.model.blog.BlogRecord
import java.util.UUID

interface BlogRecordRepository {
    suspend fun getPostsBySubscribedUsers(subscriberId: UUID): List<BlogRecord>
    suspend fun getRecommendedPostsForUser(subscriberId: UUID): List<BlogRecord>
    suspend fun getRecommendedPosts(): List<BlogRecord>
    suspend fun findPosts(word: String): List<BlogRecord>
    suspend fun findPostsByUser(userId: UUID): List<BlogRecord>
    suspend fun editPost(recordId: UUID, blogRecord: BlogRecordRequest): BlogRecord?
    suspend fun deletePost(recordId: UUID): Boolean
    suspend fun addPost(blogRecord: BlogRecordRequest): Boolean
}