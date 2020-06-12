package com.сode5150.mercury_task3.background_work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.сode5150.mercury_task3.repository.IssueRepository
import kotlinx.coroutines.Job

class UpdateWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val repo = IssueRepository(applicationContext)
            repo.getIssuesFromGithub()?.let { repo.saveListToDb(it) }
            Log.d(logTag, "DB background update succeeded")
            return Result.success()
        } catch (e: Exception) {
            Log.d(logTag, "DB background update failed and will be retried")
            return Result.retry()
        }
    }
    companion object{
        private const val logTag: String = "UPDATE_WORKER"
    }
}