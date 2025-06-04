package org.example

import org.example.models.Favorites
import org.example.models.Products
import org.example.models.Reviews
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val dbHost = System.getenv("DB_HOST") ?: "localhost"
        val dbPort = System.getenv("DB_PORT") ?: "5432"
        val dbName = System.getenv("DB_NAME") ?: "application"
        val dbUser = System.getenv("DB_USER") ?: "postgres"
        val dbPassword = System.getenv("DB_PASSWORD")
            ?: throw IllegalArgumentException("DB_PASSWORD environment variable is required")

        Database.connect(
            url = "jdbc:postgresql://$dbHost:$dbPort/$dbName",
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Products, Reviews, Favorites)
        }
    }
}