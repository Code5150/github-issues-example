package com.сode5150.mercury_task3.background_work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.сode5150.mercury_task3.repository.IssueRepository

class UpdateWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        try {
            IssueRepository.getIssuesFromGithub()?.let { IssueRepository.saveListToDb(it) }
            println("DB background update succeeded")
            return Result.success()
        } catch (e: Exception) {
            println("DB background update failed, retrying soon")
            return Result.retry()
        }
    }
}