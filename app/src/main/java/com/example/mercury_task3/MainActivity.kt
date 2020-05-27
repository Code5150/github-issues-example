package com.example.mercury_task3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repoIssuesRecyclerView: RecyclerView
    private lateinit var issueViewModel: IssueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repoIssuesRecyclerView = findViewById(R.id.repoIssuesRecyclerView)
        repoIssuesRecyclerView.layoutManager = LinearLayoutManager(this)
        repoIssuesRecyclerView.setHasFixedSize(true)

        val issueOnClickFun = {

        }

        issueViewModel = ViewModelProviders.of(this).get(IssueViewModel::class.java)

        val adapter = IssueListRecyclerAdapter(issueOnClickFun)
        repoIssuesRecyclerView.adapter = adapter

        issueViewModel.issuesData.observe(this, Observer {
            adapter.setItems(it)
        })

        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipeContainer)
        swipeRefreshLayout.setOnRefreshListener {
            GlobalScope.launch {
                swipeRefreshLayout.isRefreshing = true
                issueViewModel.updateIssuesList()
                swipeRefreshLayout.isRefreshing = false
            }
        }

    }
}
