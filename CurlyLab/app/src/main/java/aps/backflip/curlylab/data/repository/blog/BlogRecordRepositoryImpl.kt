package aps.backflip.curlylab.data.repository.blog

import aps.backflip.curlylab.domain.repository.blog.BlogRecordRepository
import aps.backflip.curlylab.domain.model.blog.BlogRecord
import aps.backflip.curlylab.data.remote.api.ApiService
import aps.backflip.curlylab.data.remote.model.request.blog.BlogRecordRequest
import java.util.UUID
import javax.inject.Inject

class BlogRecordRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : BlogRecordRepository {
    override suspend fun getPostsBySubscribedUsers(subscriberId: UUID): List<BlogRecord> {
        return apiService.getPostsBySubscribedUsers(subscriberId).map { it.toDomain() }
    }

    override suspend fun getRecommendedPostsForUser(subscriberId: UUID): List<BlogRecord> {
        return apiService.getRecommendedPostsForUser(subscriberId).map { it.toDomain() }
    }

    override suspend fun getRecommendedPosts(): List<BlogRecord> {
        return apiService.getRecommendedPosts().map { it.toDomain() }
    }

    override suspend fun findPosts(word: String): List<BlogRecord> {
        return apiService.findPosts(word).map { it.toDomain() }
    }

    override suspend fun findPostsByUser(userId: UUID): List<BlogRecord> {
        return apiService.findPostsByUser(userId).map { it.toDomain() }
    }

    override suspend fun editPost(recordId: UUID, blogRecord: BlogRecordRequest): BlogRecord?{
        return apiService.editPost(recordId, blogRecord)?.toDomain()
    }

    override suspend fun deletePost(recordId: UUID): Boolean{
        return apiService.deletePost(recordId)
    }

    override suspend fun addPost(blogRecord: BlogRecordRequest): Boolean {
        return apiService.addPost(blogRecord)
    }
}