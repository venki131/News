package com.venkatesh.news.data_source

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.venkatesh.news.BuildConfig
import com.venkatesh.news.models.News
import com.venkatesh.news.network.NetworkService
import com.venkatesh.news.network.State
import com.venkatesh.news.utils.Constants
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class NewsDataSource(
    private val networkService: NetworkService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, News>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, News>
    ) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getNews(
                Constants.SPORTS,
                BuildConfig.API_KEY,
                1,
                params.requestedLoadSize
            )
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(response.news, null, 2)
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action {
                            loadInitial(params, callback)
                        })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getNews(
                Constants.SPORTS,
                BuildConfig.API_KEY,
                params.key,
                params.requestedLoadSize
            )
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(response.news, params.key + 1)
                    }, {
                        updateState(State.ERROR)
                        setRetry(Action {
                            loadAfter(params, callback)
                        })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {

    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    private fun setRetry(action: Action) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }
}