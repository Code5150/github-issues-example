package com.example.mercury_task3

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.сode5150.mercury_task3_network.data.Issue

class DetailsActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data = intent.getParcelableExtra<Issue>(MainActivity.CLICKED_ISSUE_POS)

        val numberText: TextView = findViewById(R.id.number)
        val createdText: TextView = findViewById(R.id.created)
        val updatedText: TextView = findViewById(R.id.updated)
        val closedText: TextView = findViewById(R.id.deleted)
        val titleText: TextView = findViewById(R.id.issueTitle)
        val bodyText: TextView = findViewById(R.id.issueBody)

        numberText.text = getString(R.string.number, data.number)
        createdText.text = getString(R.string.created_at) + " ${data.createdAt}"
        updatedText.text = getString(R.string.updated_at) + " ${data.updatedAt.toString()}"
        closedText.text = getString(R.string.closed_at) + " ${data.closedAt.toString()}"
        titleText.text = data.title
        bodyText.text = data.body
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
