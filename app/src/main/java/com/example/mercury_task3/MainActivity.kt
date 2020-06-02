package com.example.mercury_task3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repoIssuesRecyclerView: RecyclerView
    private lateinit var textView: TextView
    private lateinit var issueViewModel: IssueViewModel

    companion object {
        const val CLICKED_ISSUE_POS: String = "CLICKED_ISSUE_POS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repoIssuesRecyclerView = findViewById(R.id.repoIssuesRecyclerView)
        repoIssuesRecyclerView.layoutManager = LinearLayoutManager(this)
        repoIssuesRecyclerView.setHasFixedSize(true)

        textView = findViewById(R.id.textView)

        issueViewModel = ViewModelProvider(this).get(IssueViewModel::class.java)

        val adapter = IssueListRecyclerAdapter { pos ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(CLICKED_ISSUE_POS, pos)
            startActivity(intent)
        }
        repoIssuesRecyclerView.adapter = adapter

        issueViewModel.issuesData.observe(this, Observer {
            if (it != null) {
                adapter.setItems(it)
                setRecyclerVisibility(it.size)
            }
        })

        issueViewModel.error.observe(this, Observer {
            if (it != null) {
                setLabelVisibility(it)
            }
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

    @SuppressLint("SetTextI18n")
    private fun setLabelVisibility(err: String) {
        textView.text = getString(R.string.err) + "\n$err"
        if (textView.visibility == View.INVISIBLE) textView.visibility = View.VISIBLE
        if (repoIssuesRecyclerView.visibility == View.VISIBLE) repoIssuesRecyclerView.visibility =
            View.INVISIBLE
    }
}
