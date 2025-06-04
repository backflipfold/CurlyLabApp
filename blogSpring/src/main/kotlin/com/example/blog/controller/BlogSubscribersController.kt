package com.example.blog.controller

import com.example.blog.model.BlogSubscribers
import com.example.blog.service.BlogSubscribersService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/blog_subscribers")
class BlogSubscribersController(private val blogSubscribersService: BlogSubscribersService) {
    @GetMapping
    fun getAllBlogSubscribers(): List<BlogSubscribers>?{
        return blogSubscribersService.findAll()
    }

    @GetMapping("/subscriptions/{id}")
    fun readAllSubscriptionsForUser(@PathVariable id: UUID): Int{
        return blogSubscribersService.readAllSubscriptionsForUser(id)
    }

    @GetMapping("/subscribers/{id}")
    fun readAllSubscribersForAuthor(@PathVariable id: UUID): Int{
        return blogSubscribersService.readAllSubscribersForAuthor(id)
    }

    @PostMapping
    fun add(@RequestBody blogSubscribers: BlogSubscribers): Boolean {
        return blogSubscribersService.add(blogSubscribers)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): Boolean {
        return blogSubscribersService.delete(id)
    }

    @PostMapping("/get")
    fun get(@RequestBody blogSubscribers: BlogSubscribers): BlogSubscribers?{
        return blogSubscribersService.findIdByAuthorAndSubscriber(subscriberId = blogSubscribers.subscriberId, authorId =  blogSubscribers.authorId)
    }
}