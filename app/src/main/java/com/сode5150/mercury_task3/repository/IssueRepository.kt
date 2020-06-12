package com.сode5150.mercury_task3.repository

import android.content.Context
import com.сode5150.mercury_task3.database.converters.IssueEntityConverter
import com.сode5150.mercury_task3.database.db.IssuesDB
import com.сode5150.mercury_task3.network.GithubApiInterface
import com.сode5150.mercury_task3.network.data.Issue

class IssueRepository {
    companion object {
        private var database: IssuesDB? = null

        fun initDatabase(context: Context) {
            database = IssuesDB.getIssuesDB(context)
        }

        private val apiService = GithubApiInterface()

        suspend fun getIssuesFromGithub(): List<Issue>? {
            val result = apiService.getIssues()
            saveListToDb(result)
            return result
        }

        fun saveListToDb(list: List<Issue>) {
            database?.let {
                synchronized(it) {
                    with(it.entityDAO()) {
                        list.let { res ->
                            res.forEach { issue ->
                                this.insertIssue(
                                    IssueEntityConverter.toIssueEntity(issue)
                                )
                            }
                        }
                    }
                }
            }
        }

        fun getListFromDb(): List<Issue>? {
            var result: List<Issue>? = null
            database?.let {
                synchronized(it) {
                    with(it.entityDAO()) {
                        result = this.getAllIssues()
                            .map { entity -> IssueEntityConverter.fromIssueEntity(entity) }
                    }
                }
            }
            return result
        }

    }
}
