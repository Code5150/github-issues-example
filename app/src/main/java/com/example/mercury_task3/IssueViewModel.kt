package com.example.mercury_task3

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.сode5150.mercury_task3.network.data.Issue
import com.сode5150.mercury_task3.repository.IssueRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IssueViewModel : ViewModel() {

    private lateinit var repository: IssueRepository

    fun initRepository(context: Context) {
        repository = IssueRepository(context)
    }

    val issuesData: LiveData<List<Issue>?> get() = _issuesData

    private val _issuesData: MutableLiveData<List<Issue>?> = MutableLiveData(null)

    private val _error: MutableLiveData<String?> =
        MutableLiveData(null)

    val error: LiveData<String?> get() = _error

    val selectedPos: MutableLiveData<Int> = MutableLiveData(RecyclerView.NO_POSITION)

    private suspend fun getIssuesList(): List<Issue>? {
        var result: List<Issue>? = null
        try {
            result = repository.getIssuesFromGithub()
            if (_error.value != null) _error.postValue(null)
        } catch (e: Exception) {
            try {
                result = repository.getListFromDb()
            }
            catch (e: Exception) {
                _error.postValue(e.message)
            }
        } finally {
            selectedPos.postValue(RecyclerView.NO_POSITION)
            return result
        }
    }

    suspend fun updateIssuesList() {
        _issuesData.postValue(getIssuesList())
    }

    fun setListFromDbValues(){
        CoroutineScope(Dispatchers.IO).launch {
            _issuesData.postValue(repository.getListFromDb())
        }
    }
}
