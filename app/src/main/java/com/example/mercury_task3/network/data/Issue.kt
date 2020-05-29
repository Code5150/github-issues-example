package com.example.mercury_task3.network.data

data class Issue(
    val body: String,
    val closed_at: String?,
    val created_at: String,
    val number: Int,
    val title: String,
    val updated_at: String?,
    val user: User
)