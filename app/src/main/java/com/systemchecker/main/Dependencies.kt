package com.systemchecker.main

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.systemchecker.main.retrofit.LiveDataCallAdapterFactory
import com.systemchecker.main.retrofit.WebService
import io.realm.Realm
import io.realm.RealmObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Dependencies {

    private val gson = GsonBuilder().setExclusionStrategies(object : ExclusionStrategy {
        override fun shouldSkipField(f: FieldAttributes): Boolean {
            return f.declaringClass == RealmObject::class.java
        }

        override fun shouldSkipClass(clazz: Class<*>): Boolean {
            return false
        }
    }).create()

    private var webService: WebService = Retrofit.Builder()
        //TODO: Update Api URL
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(
            LiveDataCallAdapterFactory
                ()
        )
        .build()
        .create(WebService::class.java)

    fun getRetrofit(): WebService {
        return webService
    }

    fun getDatabase(): Realm {
        return Realm.getDefaultInstance()
    }
}