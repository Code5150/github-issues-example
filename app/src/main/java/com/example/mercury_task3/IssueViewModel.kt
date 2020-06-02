package com.example.mercury_task3

import androidx.lifecycle.*
import com.сode5150.mercury_task3_network.data.Issue
import com.сode5150.mercury_task3_network.GithubApiInterface


class IssueViewModel : ViewModel() {

    val issuesData: LiveData<List<Issue>?> get() = _issuesData

    val error: MutableLiveData<String?> get() = _error

    companion object {
        private val _issuesData: MutableLiveData<List<Issue>?> = liveData {
            emit(_getIssuesList())
        } as MutableLiveData<List<Issue>?>

        private val _error: MutableLiveData<String?> =
            MutableLiveData(null)

        private val apiService =
            GithubApiInterface()

        private suspend fun _getIssuesList(): List<Issue>? {
            var result: List<Issue>? = null
            try {
                result = apiService.getIssues()
                if (_error.value != null) _error.postValue(null)

            } catch (e: Exception) {
                _error.postValue(e.message)
            }
            return result
        }
    }

    private suspend fun getIssuesList(): List<Issue>? {
        return _getIssuesList()
    }

    suspend fun updateIssuesList() {
        _issuesData.postValue(getIssuesList())
    }
}
