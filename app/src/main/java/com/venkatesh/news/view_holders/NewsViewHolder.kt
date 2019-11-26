package com.venkatesh.news.view_holders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.venkatesh.news.R
import com.venkatesh.news.models.News
import kotlinx.android.synthetic.main.item_news.view.*

class NewsViewHolder(view: View, val context : Context) : RecyclerView.ViewHolder(view) {

    fun bind(news: News?) {
        if (news != null) {
            itemView.txtNewsTitle.text = news.title
            //Picasso.get().load(news.image).into(itemView.imgNewsBanner)
            Glide.with(context)
                .load(news.image)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(itemView.imgNewsBanner)
        }
    }

    companion object {
        fun create(parent: ViewGroup, context: Context): NewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_news, parent, false)

            return NewsViewHolder(view, context = context)
        }
    }
}