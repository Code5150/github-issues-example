package com.сode5150.mercury_task3.database.dao

import androidx.room.*
import com.сode5150.mercury_task3.database.entities.ISSUES_TABLE_NAME
import com.сode5150.mercury_task3.database.entities.IssueEntity

@Dao
interface EntityDAO {
    @Query("SELECT * FROM $ISSUES_TABLE_NAME ORDER BY number DESC")
    fun getAllIssues(): List<IssueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIssue(issue: IssueEntity)

    @Update
    fun updateIssue(issue: IssueEntity)

    @Delete
    fun deleteIssue(issue: IssueEntity)
}