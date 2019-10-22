package com.systemchecker.main.db


import com.systemchecker.main.model.Repo
import com.systemchecker.main.utils.LiveRealmData
import com.systemchecker.main.utils.asLiveData
import io.realm.Case
import io.realm.Realm
import io.realm.Sort

class RepoDao(val realm: Realm) {
    fun insertRepos(items: ArrayList<Repo>) {
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(items)
        realm.commitTransaction()
    }

    fun loadRepositories(owner: String): LiveRealmData<Repo> {
        return realm.where(Repo::class.java)
            .equalTo("owner.login", owner, Case.INSENSITIVE)
            .findAll()
            .asLiveData()
    }
}