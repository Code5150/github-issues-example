package com.сode5150.mercury_task3_network.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class Issue(
    val body: String,
    @SerializedName("closed_at")
    val closedAt: Date?,
    @SerializedName("created_at")
    val createdAt: Date,
    val number: Int,
    val title: String,
    @SerializedName("updated_at")
    val updatedAt: Date?,
    val user: User
)