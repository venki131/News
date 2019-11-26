package com.venkatesh.news.view_holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.venkatesh.news.R
import com.venkatesh.news.network.State
import kotlinx.android.synthetic.main.item_list_footer.view.*

class ListFootViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status: State?) {
        itemView.progressBar.visibility =
            if (status == State.LOADING) View.VISIBLE else View.INVISIBLE
        itemView.txtError.visibility = if (status == State.ERROR) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): ListFootViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_footer, parent, false)
            view.txtError.setOnClickListener { retry() }
            return ListFootViewHolder(view)
        }
    }
}