package com.example.mercury_task3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.issue_item_card.view.*
import com.example.mercury_task3.network.data.Issue

class IssueListRecyclerAdapter(
    private val callbackFun: () -> Unit
) : RecyclerView.Adapter<IssueListRecyclerAdapter.ItemHolder>() {
    private var items: ArrayList<Issue> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.issue_item_card, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.issueNum.text = items[position].number.toString()
        holder.username.text = items[position].user.login
    }

    fun setItems(newItems: ArrayList<Issue>){
        items = newItems
        notifyDataSetChanged()
    }

    inner class ItemHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        var issueNum: TextView = v.issueNum
        var username: TextView = v.username

        override fun onClick(view: View?) = callbackFun()

        init {
            v.setOnClickListener(this)
        }
    }
}