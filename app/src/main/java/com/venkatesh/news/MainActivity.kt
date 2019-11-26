package com.venkatesh.news

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.venkatesh.news.adapters.NewsListAdapter
import com.venkatesh.news.network.State
import com.venkatesh.news.viewmodels.NewsListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: NewsListViewModel
    private lateinit var newsListAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this)
            .get(NewsListViewModel::class.java)

        initAdapter()
        initState()
    }

    private fun initAdapter() {
        newsListAdapter = NewsListAdapter(this) { viewModel.retry() }
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = newsListAdapter
        viewModel.newsList.observe(this, Observer {
            newsListAdapter.submitList(it)
        })
    }

    private fun initState() {
        txtError.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer { state ->
            progressBar.visibility = if (viewModel.listIsEmpty() && state == State.LOADING)
                View.VISIBLE
            else
                View.GONE

            txtError.visibility = if (viewModel.listIsEmpty() && state == State.ERROR)
                View.VISIBLE
            else
                View.GONE

            if (!viewModel.listIsEmpty()) {
                newsListAdapter.setState(state ?: State.DONE)
            }
        })
    }
}
