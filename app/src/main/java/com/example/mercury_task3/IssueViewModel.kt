package com.example.mercury_task3

import androidx.lifecycle.*
import com.Code5150.mercury_task3_network.data.Issue
import com.Code5150.mercury_task3_network.GithubApiInterface


class IssueViewModel : ViewModel() {

    val issuesData: LiveData<ArrayList<Issue>> get() = _issuesData

    val error: MutableLiveData<Boolean> get() = _error

    companion object {
        private val _issuesData: MutableLiveData<ArrayList<Issue>> = liveData {
            emit(apiService.getIssues() as ArrayList<Issue>)
        } as MutableLiveData<ArrayList<Issue>>

        private val _error: MutableLiveData<Boolean> = liveData { emit(false) } as MutableLiveData<Boolean>

        private val apiService =
            GithubApiInterface()
    }

    private suspend fun getIssuesList(): ArrayList<Issue> {
        return apiService.getIssues() as ArrayList<Issue>
    }

    suspend fun updateIssuesList(){
        try {
            _issuesData.postValue(getIssuesList())
            if(_error.value != null){
                if(_error.value!!) _error.postValue(false)
            }
        }
        catch(e: Exception){
            _error.postValue(true)
        }
    }
}
