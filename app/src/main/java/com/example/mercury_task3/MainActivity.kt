package com.example.mercury_task3

import android.annotation.SuppressLint
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repoIssuesRecyclerView: RecyclerView
    private lateinit var textView: TextView
    private lateinit var issueViewModel: IssueViewModel
    private var detailsFragment: DetailsFragment? = null

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

        val onClickFun = { pos: Int ->
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                val intent = Intent(this, DetailsActivity::class.java)
                intent.putExtra(CLICKED_ISSUE_POS, issueViewModel.issuesData.value!![pos])
                startActivity(intent)
            } else {
                val selected = Bundle()
                detailsFragment = DetailsFragment()
                selected.putParcelable(CLICKED_ISSUE_POS, issueViewModel.issuesData.value!![pos])
                detailsFragment!!.arguments = selected
                this.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentDetails, detailsFragment!!)
                    commit()
                }
                Unit
            }
        }

        val adapter = IssueListRecyclerAdapter(onClickFun, resources.configuration.orientation)

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
            detailsFragment?.let {
                supportFragmentManager.beginTransaction().apply {
                    remove(it)
                    commit()
                }
            }
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
        textView.text = getString(R.string.err, err)
        if (textView.visibility == View.INVISIBLE) textView.visibility = View.VISIBLE
        if (repoIssuesRecyclerView.visibility == View.VISIBLE) repoIssuesRecyclerView.visibility =
            View.INVISIBLE
    }
}
