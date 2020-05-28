package com.example.mercury_task3

import androidx.lifecycle.*
import com.example.mercury_task3.network.data.Issue
import com.example.mercury_task3.network.GithubApiInterface


class IssueViewModel : ViewModel() {
    private val _issuesData: MutableLiveData<ArrayList<Issue>> = liveData {
        emit(getIssuesList())
    } as MutableLiveData<ArrayList<Issue>>

    val issuesData: LiveData<ArrayList<Issue>> get() = _issuesData
    
    private val _error: MutableLiveData<Boolean> = liveData { emit(false) } as MutableLiveData<Boolean>
    val error: MutableLiveData<Boolean> get() = _error

    companion object {
        private val apiService = GithubApiInterface()

        private suspend fun getIssuesList(): ArrayList<Issue> {
            return apiService.getIssues() as ArrayList<Issue>
        }
    }

    /*private suspend fun testList(): ArrayList<Issue>{
        val result = ArrayList<Issue>()
        delay(500)
        for(i in 1..10){
            result += Issue(i, "User${(Math.random()*10 + i).roundToInt()}", "27-05-2020", "Issue ${Math.random()*10 + 2*i}")
        }
        return result
    }*/

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
