package com.example.blog.controller

import com.example.blog.model.BlogRecord
import com.example.blog.service.BlogRecordService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/blog_records")
class BlogRecordController(private val blogRecordService: BlogRecordService) {
    @GetMapping
    fun findAllPosts(): List<BlogRecord>{
        return blogRecordService.findAll()
    }

    @GetMapping("/subscriptions/{id}")
    fun findPostsBySubscribedUsers(@PathVariable id: UUID): List<BlogRecord>{
        return blogRecordService.findPostsBySubscribedUsers(id)
    }

    @GetMapping("/my/{id}")
    fun findPostsByUser(@PathVariable id: UUID): List<BlogRecord>{
        return blogRecordService.findPostsByUser(id)
    }

    @GetMapping("/recommended/{id}")
    fun findRecommendedPostsForUser(@PathVariable id: UUID): List<BlogRecord>{
        return blogRecordService.findRecommendedPostsForUser(id)
    }

    @GetMapping("/find/{word}")
    fun findPostByPartOfContentAndTag(@PathVariable word: String): List<BlogRecord>{
        return blogRecordService.getPostByPartOfContentAndTag(word)
    }

    @PutMapping("/{id}")
    fun editPost(@PathVariable id: UUID, @RequestBody blogRecord: BlogRecord): BlogRecord?{
        return blogRecordService.edit(id, blogRecord)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): Boolean {
        return blogRecordService.delete(id)
    }

    @PostMapping("/{id}")
    fun add(@RequestBody blogRecord: BlogRecord): Boolean {
        return blogRecordService.add(blogRecord)
    }
}