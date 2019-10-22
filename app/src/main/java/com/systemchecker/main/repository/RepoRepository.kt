package com.systemchecker.main.repository

import androidx.lifecycle.LiveData
import com.systemchecker.main.db.RepoDao
import com.systemchecker.main.model.Repo
import com.systemchecker.main.network.ApiResponse
import com.systemchecker.main.network.NetworkBoundResource
import com.systemchecker.main.network.Resource
import com.systemchecker.main.retrofit.WebService
import com.systemchecker.main.utils.LiveRealmData
import com.systemchecker.main.utils.RateLimiter
import io.realm.RealmResults
import java.util.concurrent.TimeUnit

class RepoRepository(val repoDao: RepoDao, val webService: WebService) {
    val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadRepos(owner: String): LiveData<Resource<RealmResults<Repo>>> {
        return object : NetworkBoundResource<Repo, List<Repo>>() {
            override fun saveCallResult(item: List<Repo>) {
                repoDao.insertRepos(ArrayList(item))
            }

            override fun shouldFetch(data: RealmResults<Repo>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(owner)
            }

            override fun loadFromDb(): LiveRealmData<Repo> {
                return repoDao.loadRepositories(owner)
            }

            override fun createCall(): LiveData<ApiResponse<List<Repo>>> {
                return webService.getRepos(owner)
            }

            override fun onFetchFailed() {
                repoListRateLimit.reset(owner)
            }
        }.asLiveData()
    }


}