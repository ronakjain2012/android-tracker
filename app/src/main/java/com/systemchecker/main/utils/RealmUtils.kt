@file:JvmName("RealmUtils")

package com.systemchecker.main.utils

import com.systemchecker.main.db.RepoDao


import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * Created by ahmedrizwan on 9/18/17.
 * Helper Extension methods for Realm
 */
fun Realm.repoDao(): RepoDao = RepoDao(this)

fun <T : RealmModel> RealmResults<T>.asLiveData() = LiveRealmData(this)