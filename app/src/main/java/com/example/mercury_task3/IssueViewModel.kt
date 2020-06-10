package com.example.mercury_task3

import android.content.Context
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.сode5150.mercury_task3_network.data.Issue
import com.сode5150.mercury_task3_network.GithubApiInterface
import com.сode5150.task3_database.db.IssuesDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class IssueViewModel : ViewModel() {

    private var database: IssuesDB? = null

    fun initDatabase(context: Context) {
        database = IssuesDB.getIssuesDB(context)
    }

    val issuesData: LiveData<List<Issue>?> get() = _issuesData

    private val _issuesData: MutableLiveData<List<Issue>?> = MutableLiveData(null)

    private val _error: MutableLiveData<String?> =
        MutableLiveData(null)

    val error: LiveData<String?> get() = _error

    val selectedPos: MutableLiveData<Int> = MutableLiveData(RecyclerView.NO_POSITION)

    private val apiService =
        GithubApiInterface()

    private suspend fun getIssuesList(): List<Issue>? {
        var result: List<Issue>? = null
        try {
            result = apiService.getIssues()
            saveListToDb(result)
            if (_error.value != null) _error.postValue(null)
        } catch (e: Exception) {
            try {
                result = getListFromDb()
            }
            catch (e: Exception) {
                _error.postValue(e.message)
            }
        } finally {
            selectedPos.postValue(RecyclerView.NO_POSITION)
            return result
        }
    }

    private fun getListFromDb(): List<Issue>?{
        var result: List<Issue>? = null
        database?.let {
            synchronized(it) {
                with(it.entityDAO()) {
                    result = this.getAllIssues()
                    if (_error.value != null) _error.postValue(null)
                }
            }
        }
        return result
    }

    private fun saveListToDb(list: List<Issue>){
        database?.let {
            synchronized(it) {
                with(it.entityDAO()) {
                    list.let{ res -> res.forEach { issue -> this.insertIssue(issue) }}
                }
            }
        }
    }

    suspend fun updateIssuesList() {
        _issuesData.postValue(getIssuesList())
    }

    fun setListFromDbValues(){
        CoroutineScope(Dispatchers.IO).launch {
            _issuesData.postValue(getListFromDb())
        }
    }
}
