package com.сode5150.mercury_task3.background_work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.сode5150.mercury_task3.database.db.IssuesDB
import com.сode5150.mercury_task3.network.GithubApiInterface

class UpdateWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val database = IssuesDB.getIssuesDB(applicationContext)
        val apiService = GithubApiInterface()
        try {
            val list = apiService.getIssues()
            database?.let {
                synchronized(it) {
                    with(it.entityDAO()) {
                        list.forEach { issue -> this.insertIssue(issue) }
                    }
                }
            }
            println("DB background update succeeded")
            return Result.success()
        } catch (e: Exception) {
            println("DB background update failed, retrying soon")
            return Result.retry()
        }
    }
}