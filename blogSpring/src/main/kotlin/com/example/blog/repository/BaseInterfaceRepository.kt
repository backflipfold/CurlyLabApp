package com.example.blog.repository

import java.util.*

interface BaseInterfaceRepository<T> {
    fun findAll(): List<T>
    fun read(id: UUID): T?
    fun add(entity: T): Boolean
    fun delete(id: UUID): Boolean
    fun edit(id: UUID, entity: T): T?
}