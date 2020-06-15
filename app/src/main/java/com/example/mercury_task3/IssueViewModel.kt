package com.example.mercury_task3

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.RecyclerView
import com.сode5150.mercury_task3.network.data.Issue
import com.сode5150.mercury_task3.network.data.STATE_CLOSED
import com.сode5150.mercury_task3.network.data.STATE_OPEN
import com.сode5150.mercury_task3.repository.IssueRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IssueViewModel : ViewModel() {

    private lateinit var repository: IssueRepository

    private val getListCallback = { list: List<Issue>? -> _issuesData.postValue(list) }

    fun initRepository(context: Context) {
        repository = IssueRepository(context, getListCallback)
    }

    val issuesData: LiveData<List<Issue>?> get() = _issuesData

    private val _issuesData: MutableLiveData<List<Issue>?> = MutableLiveData(null)

    private val _error: MutableLiveData<String?> =
        MutableLiveData(null)

    val error: LiveData<String?> get() = _error

    val selectedPos: MutableLiveData<Int> = MutableLiveData(RecyclerView.NO_POSITION)

    suspend fun updateIssuesList() {
        try {
            repository.getData()
            if (_error.value != null) _error.postValue(null)
        } catch (e: Exception) {
            _error.postValue(e.message)
        } finally {
            selectedPos.postValue(RecyclerView.NO_POSITION)
        }
    }
}
