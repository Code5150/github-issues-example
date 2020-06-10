package com.сode5150.mercury_task3.database.dao

import androidx.room.*
import com.сode5150.mercury_task3.network.data.ISSUES_TABLE_NAME
import com.сode5150.mercury_task3.network.data.Issue

@Dao
interface EntityDAO {
    @Query("SELECT * FROM $ISSUES_TABLE_NAME ORDER BY number DESC")
    fun getAllIssues(): List<Issue>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIssue(issue: Issue)

    @Update
    fun updateIssue(issue: Issue)

    @Delete
    fun deleteIssue(issue: Issue)
}