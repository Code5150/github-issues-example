package com.example.mercury_task3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.сode5150.mercury_task3_network.data.Issue

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment() : Fragment() {
    private var selectedIssue: Issue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedIssue = it.getParcelable(MainActivity.CLICKED_ISSUE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_details, container, false)

        val numberText: TextView = rootView.findViewById(R.id.number)
        val createdText: TextView = rootView.findViewById(R.id.created)
        val updatedText: TextView = rootView.findViewById(R.id.updated)
        val closedText: TextView = rootView.findViewById(R.id.deleted)
        val titleText: TextView = rootView.findViewById(R.id.issueTitle)
        val bodyText: TextView = rootView.findViewById(R.id.issueBody)

        selectedIssue?.let {
            numberText.text = getString(R.string.number, it.number)
            createdText.text = getString(R.string.created_at, it.createdAt)
            updatedText.text = getString(R.string.updated_at, it.updatedAt.toString())
            closedText.text = getString(R.string.closed_at, it.closedAt.toString())
            titleText.text = it.title
            bodyText.text = it.body
        }

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance(selected: Issue?) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MainActivity.CLICKED_ISSUE, selected)
                }
            }
    }
}