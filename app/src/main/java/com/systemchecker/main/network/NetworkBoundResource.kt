package com.systemchecker.main.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MediatorLiveData
import io.realm.RealmModel
import io.realm.RealmResults
import androidx.lifecycle.LiveData
import com.systemchecker.main.utils.LiveRealmData

abstract class NetworkBoundResource<Model : RealmModel, RequestType> @MainThread
constructor() {

    private val result = MediatorLiveData<Resource<RealmResults<Model>>>()

    init {
        val dbSource = loadFromDb()

        result.addSource(dbSource) { resultType ->
            result.removeSource(dbSource)
            if (shouldFetch(resultType)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { rT -> result.value = Resource.success(rT) }
            }
        }
    }

    fun fetchFromNetwork(dbSource: LiveRealmData<Model>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { resultType ->
            result.value = Resource.loading(resultType)
        }

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            if (response!!.isSuccessful) {
                processResponse(response)?.let { saveCallResult(it) }
                mainThread {
                    // we specially request a new live data,
                    // otherwise we will get immediately last cached value,
                    // which may not be updated with latest results received from network.
                    result.addSource(
                        loadFromDb()
                    ) { resultType -> result.value = Resource.success(resultType) }
                }
            } else {
                onFetchFailed()
                result.addSource(
                    dbSource
                ) { resultType ->
                    result.value = response.errorMessage?.let { Resource.error(it, resultType) }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData(): LiveData<Resource<RealmResults<Model>>> {
        return result
    }

    @WorkerThread
    protected fun processResponse(response: ApiResponse<RequestType>): RequestType? {
        return response.body
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: RealmResults<Model>?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveRealmData<Model>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

}