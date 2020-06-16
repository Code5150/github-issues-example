package com.example.mercury_task3

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.сode5150.mercury_task3.network.data.Issue
import com.сode5150.mercury_task3.network.data.STATE_CLOSED
import com.сode5150.mercury_task3.network.data.STATE_OPEN
import com.сode5150.mercury_task3.repository.IssueRepository

class IssueViewModel : ViewModel() {

    private lateinit var repository: IssueRepository

    private val getListCallback = { list: List<Issue>? ->
        origData.postValue(list)
        _issuesData.postValue(filterListByIssueState(list))
    }

    fun initRepository(context: Context) {
        repository = IssueRepository(context, getListCallback)
    }

    val issuesData: LiveData<List<Issue>?> get() = _issuesData

    private val _issuesData: MutableLiveData<List<Issue>?> = MutableLiveData(null)

    private val origData: MutableLiveData<List<Issue>?> = MutableLiveData(null)

    private val _error: MutableLiveData<String?> =
        MutableLiveData(null)

    val error: LiveData<String?> get() = _error

    val selectedPos: MutableLiveData<Int> = MutableLiveData(RecyclerView.NO_POSITION)

    private enum class FilterStates {
        OPEN, CLOSED, ALL
    }

    private val selectedFilterOption: MutableLiveData<FilterStates> =
        MutableLiveData(FilterStates.ALL)

    suspend fun updateIssuesList() {
        try {
            repository.getData()
            if (_error.value != null) _error.postValue(null)
        } catch (e: Exception) {
            if (_issuesData.value != null) _error.postValue(null)
            else _error.postValue(e.message)
        } finally {
            selectedPos.postValue(RecyclerView.NO_POSITION)
        }
    }

    private fun filterListByIssueState(list: List<Issue>?): List<Issue>? {
        var result: List<Issue>? = null
        when (selectedFilterOption.value) {
            FilterStates.ALL -> result = list
            FilterStates.OPEN -> result = list?.filter { issue -> issue.state == STATE_OPEN }
            FilterStates.CLOSED -> result = list?.filter { issue -> issue.state == STATE_CLOSED }
        }
        return result
    }

    fun setAll() {
        selectedFilterOption.value = FilterStates.ALL
        _issuesData.postValue(origData.value)
    }

    fun setOpen() {
        selectedFilterOption.value = FilterStates.OPEN
        _issuesData.postValue(filterListByIssueState(origData.value))
    }

    fun setClosed() {
        selectedFilterOption.value = FilterStates.CLOSED
        _issuesData.postValue(filterListByIssueState(origData.value))
    }
}
