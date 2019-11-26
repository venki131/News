package com.venkatesh.news.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.venkatesh.news.models.News
import com.venkatesh.news.network.State
import com.venkatesh.news.view_holders.ListFootViewHolder
import com.venkatesh.news.view_holders.NewsViewHolder

class NewsListAdapter(context: Context, private val retry: () -> Unit) :
    PagedListAdapter<News, RecyclerView.ViewHolder>(NewsDiffCallBack) {

    private var mContext : Context = context
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2
    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE)
            NewsViewHolder.create(parent, mContext)
        else ListFootViewHolder.create(
            retry,
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as NewsViewHolder).bind(getItem(position))
        else
            (holder as ListFootViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount())
            DATA_VIEW_TYPE
        else
            FOOTER_VIEW_TYPE
    }

    companion object {
        val NewsDiffCallBack = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}