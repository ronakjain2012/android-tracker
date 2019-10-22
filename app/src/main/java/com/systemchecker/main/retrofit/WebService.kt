package com.systemchecker.main.retrofit

import androidx.lifecycle.LiveData
import com.systemchecker.main.model.Repo
import com.systemchecker.main.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface WebService {

    @GET("users/{login}/repos")
    fun getRepos(@Path("login") login: String): LiveData<ApiResponse<List<Repo>>>

}