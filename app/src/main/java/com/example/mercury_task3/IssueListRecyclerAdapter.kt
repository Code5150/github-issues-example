package com.example.mercury_task3

import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.issue_item_card.view.*
import com.сode5150.mercury_task3.network.data.Issue

class IssueListRecyclerAdapter(
    private val callbackFun: (Int) -> Unit,
    private val orientation: Int,
    private val selectedPos: MutableLiveData<Int>
) : RecyclerView.Adapter<IssueListRecyclerAdapter.ItemHolder>() {
    private var items: List<Issue> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.issue_item_card, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.issueNum.text = items[position].number.toString()
        holder.username.text = items[position].user.login
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (selectedPos.value == position) {
                holder.card.setBackgroundColor(Color.LTGRAY)
            } else {
                holder.card.setBackgroundColor(Color.WHITE)
            }
        }
    }

    fun setItems(newItems: List<Issue>, oldItems: List<Issue> = items) {
        val diffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(oldItems, newItems)
        )
        if (oldItems.isNotEmpty()) {
            if (selectedPos.value!! >= 0) {
                val pos = selectedPos.value!!
                selectedPos.value = diffResult.convertOldPositionToNew(selectedPos.value!!)
                notifyItemChanged(pos)
            }
        }
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ItemDiffCallback(
        private val oldList: List<Issue>,
        private val newList: List<Issue>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].number == newList[newItemPosition].number
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }

    inner class ItemHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        val issueNum: TextView = v.issueNum
        val username: TextView = v.username
        val card: CardView = v.selectableCard

        override fun onClick(view: View?) {
            val prevPos = selectedPos.value
            selectedPos.value = adapterPosition

            prevPos?.let { notifyItemChanged(it) }
            selectedPos.value?.let { notifyItemChanged(it) }

            callbackFun(adapterPosition)
        }

        init {
            v.setOnClickListener(this)
        }
    }
}