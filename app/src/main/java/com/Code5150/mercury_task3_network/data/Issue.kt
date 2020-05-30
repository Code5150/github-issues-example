package com.Code5150.mercury_task3_network.data

data class Issue(
    val body: String,
    val closed_at: String?,
    val created_at: String,
    val number: Int,
    val title: String,
    val updated_at: String?,
    val user: User
)