package com.example.mercury_task3

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import java.time.LocalDate
import kotlin.math.roundToInt


class IssueViewModel : ViewModel() {
    private val _issuesData: MutableLiveData<ArrayList<Issue>> = liveData {
        emit(testList())
    } as MutableLiveData<ArrayList<Issue>>

    val issuesData: LiveData<ArrayList<Issue>> get() = _issuesData

    private suspend fun testList(): ArrayList<Issue>{
        val result = ArrayList<Issue>()

        delay(500)

        for(i in 1..10){
            result += Issue(i, "User${(Math.random()*10 + i).roundToInt()}", "27-05-2020", "Issue ${Math.random()*10 + 2*i}")
        }

        return result
    }

    suspend fun updateIssuesList(){
        _issuesData.postValue(testList())
    }

    fun getIssuesList(): MutableLiveData<ArrayList<Issue>> {
        val issueList = ArrayList<Issue>()


        val result = MutableLiveData<ArrayList<Issue>>()
        result.value = issueList
        return result
    }
}
