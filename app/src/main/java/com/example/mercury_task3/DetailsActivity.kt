package com.example.mercury_task3

import android.content.res.Configuration
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.сode5150.mercury_task3_network.data.Issue
import java.text.SimpleDateFormat

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) finish()

        setContentView(R.layout.activity_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data = intent.getParcelableExtra<Issue>(MainActivity.CLICKED_ISSUE)

        val numberText: TextView = findViewById(R.id.number)
        val createdText: TextView = findViewById(R.id.created)
        val updatedText: TextView = findViewById(R.id.updated)
        val closedText: TextView = findViewById(R.id.deleted)
        val titleText: TextView = findViewById(R.id.issueTitle)
        val bodyText: TextView = findViewById(R.id.issueBody)

        data?.let {
            val dateFormat = SimpleDateFormat(getString(R.string.date_format))

            numberText.text = getString(R.string.number, it.number)
            createdText.text = getString(R.string.created_at, dateFormat.format(it.createdAt))
            updatedText.text = getString(
                R.string.updated_at, if (it.updatedAt == null) getString(R.string.null_date) else {
                    dateFormat.format(it.updatedAt)
                }
            )
            closedText.text = getString(
                R.string.closed_at, if (it.closedAt == null) getString(R.string.null_date) else {
                    dateFormat.format(it.closedAt)
                }
            )
            titleText.text = it.title
            bodyText.text = it.body
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
