package com.example.mercury_task3

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.сode5150.mercury_task3.background_work.UpdateWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var repoIssuesRecyclerView: RecyclerView
    private lateinit var textView: TextView
    private lateinit var issueViewModel: IssueViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var detailsFragment: DetailsFragment? = null

    companion object {
        const val CLICKED_ISSUE: String = "CLICKED_ISSUE"
        const val TASK_ID: String = "UPDATE_ISSUES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repoIssuesRecyclerView = findViewById(R.id.repoIssuesRecyclerView)
        repoIssuesRecyclerView.layoutManager = LinearLayoutManager(this)
        repoIssuesRecyclerView.setHasFixedSize(true)

        textView = findViewById(R.id.textView)

        swipeRefreshLayout = findViewById(R.id.swipeContainer)

        issueViewModel = ViewModelProvider(this).get(IssueViewModel::class.java).apply {
            initDatabase(this@MainActivity)
        }
        if (issueViewModel.issuesData.value == null) {
            issueViewModel.setListFromDbValues()
            refreshList()
        }

        val work = PeriodicWorkRequestBuilder<UpdateWorker>(15, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .addTag(TASK_ID)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            TASK_ID,
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )

        //Adapter callback
        val onClickFun = { pos: Int ->
            initFragment(pos)
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                val intent = Intent(this, DetailsActivity::class.java)
                intent.putExtra(CLICKED_ISSUE, issueViewModel.issuesData.value!![pos])
                startActivity(intent)
            } else {
                replaceFragment()
            }
        }

        //Adapter initialization
        val adapter = IssueListRecyclerAdapter(
            onClickFun,
            resources.configuration.orientation,
            issueViewModel.selectedPos
        )
        repoIssuesRecyclerView.adapter = adapter

        //Clicked issue fragment initialization
        if (issueViewModel.selectedPos.value != RecyclerView.NO_POSITION &&
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        ) {
            issueViewModel.selectedPos.value?.let {
                initFragment(it)
                replaceFragment()
                repoIssuesRecyclerView.smoothScrollToPosition(it)
            }
        }

        //Setting data observers
        issueViewModel.issuesData.observe(this, Observer {
            if (it != null) {
                adapter.setItems(it)
                setRecyclerVisibility(it.size)
            }
        })

        issueViewModel.error.observe(this, Observer {
            if (it != null) {
                setErrorLabel(it)
            }
        })

        //Swipe refresh initialization
        swipeRefreshLayout.setOnRefreshListener {
            detailsFragment?.let {
                supportFragmentManager.beginTransaction().apply {
                    remove(it)
                    commit()
                }
            }
            refreshList()
        }
    }

    private fun setRecyclerVisibility(size: Int) {
        if (size > 0) {
            if (textView.visibility == View.VISIBLE) textView.visibility = View.INVISIBLE
            if (repoIssuesRecyclerView.visibility == View.INVISIBLE) repoIssuesRecyclerView.visibility =
                View.VISIBLE
        } else {
            textView.text = getString(R.string.no_opened_issues)
            if (textView.visibility == View.INVISIBLE) textView.visibility = View.VISIBLE
            if (repoIssuesRecyclerView.visibility == View.VISIBLE) repoIssuesRecyclerView.visibility =
                View.INVISIBLE
        }
    }

    private fun setErrorLabel(err: String) {
        textView.text = getString(R.string.err, err)
        if (textView.visibility == View.INVISIBLE) textView.visibility = View.VISIBLE
        if (repoIssuesRecyclerView.visibility == View.VISIBLE) repoIssuesRecyclerView.visibility =
            View.INVISIBLE
    }

    private fun initFragment(pos: Int) {
        val selected = Bundle()
        selected.putParcelable(CLICKED_ISSUE, issueViewModel.issuesData.value!![pos])
        detailsFragment = DetailsFragment()
        detailsFragment?.let {
            it.arguments = selected
        }
    }

    private fun replaceFragment() {
        detailsFragment?.let {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentDetails, it)
                commit()
            }
        }
    }

    private fun refreshList() {
        CoroutineScope(Dispatchers.IO).launch {
            swipeRefreshLayout.isRefreshing = true
            issueViewModel.updateIssuesList()
            swipeRefreshLayout.isRefreshing = false
        }
    }
}
