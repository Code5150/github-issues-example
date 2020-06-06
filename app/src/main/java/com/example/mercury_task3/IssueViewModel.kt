package com.example.mercury_task3

import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.сode5150.mercury_task3_network.data.Issue
import com.сode5150.mercury_task3_network.GithubApiInterface

class IssueViewModel : ViewModel() {

    val issuesData: LiveData<List<Issue>?> get() = _issuesData

    private val _issuesData: MutableLiveData<List<Issue>?> = liveData {
        emit(getIssuesList())
    } as MutableLiveData<List<Issue>?>

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
            if (_error.value != null) _error.postValue(null)

        } catch (e: Exception) {
            _error.postValue(e.message)
        }
        selectedPos.postValue(RecyclerView.NO_POSITION)
        return result
    }

    suspend fun updateIssuesList() {
        _issuesData.postValue(getIssuesList())
    }
}
