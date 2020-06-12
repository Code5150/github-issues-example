package com.сode5150.mercury_task3.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

const val ISSUES_TABLE_NAME: String = "issues"
@Entity(tableName = ISSUES_TABLE_NAME)
data class IssueEntity (
    @PrimaryKey val number: Int,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "updated_at") val updatedAt: Date?,
    @ColumnInfo(name = "closed_at") val closedAt: Date?,
    val title: String,
    val body: String,
    val login: String
)