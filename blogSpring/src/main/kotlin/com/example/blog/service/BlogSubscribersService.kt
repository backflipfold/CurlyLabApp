package com.example.blog.service

import com.example.blog.model.BlogSubscribers
import com.example.blog.repository.BaseInterfaceRepository
import com.example.blog.repository.BlogSubscribersRepository
import org.springframework.stereotype.Service
import java.util.*

interface BlogSubscribersService : BaseInterfaceRepository<BlogSubscribers> {
    fun readAllSubscriptionsForUser(userId: UUID): Int
    fun readAllSubscribersForAuthor(authorId: UUID): Int
    fun findIdByAuthorAndSubscriber(authorId: UUID, subscriberId: UUID): BlogSubscribers?
}

@Service
class BlogSubscribersServiceImpl(private val blogSubscribersRepository: BlogSubscribersRepository) : BlogSubscribersService {
    override fun findAll(): List<BlogSubscribers> {
        return blogSubscribersRepository.findAll()
    }

    override fun read(id: UUID): BlogSubscribers? {
        return blogSubscribersRepository.read(id)
    }

    override fun add(entity: BlogSubscribers): Boolean {
        return blogSubscribersRepository.add(entity)
    }

    override fun delete(id: UUID): Boolean {
        return blogSubscribersRepository.delete(id)
    }

    override fun edit(id: UUID, entity: BlogSubscribers): BlogSubscribers? {
        return blogSubscribersRepository.edit(id, entity)
    }

    override fun readAllSubscriptionsForUser(userId: UUID): Int{
        return blogSubscribersRepository.readAllSubscriptionsForUser(userId)
    }

    override fun readAllSubscribersForAuthor(authorId: UUID): Int{
        return blogSubscribersRepository.readAllSubscribersForAuthor(authorId)
    }

    override fun findIdByAuthorAndSubscriber(authorId: UUID, subscriberId: UUID): BlogSubscribers? {
        return blogSubscribersRepository.findIdByAuthorAndSubscriber(authorId, subscriberId)
    }
}
