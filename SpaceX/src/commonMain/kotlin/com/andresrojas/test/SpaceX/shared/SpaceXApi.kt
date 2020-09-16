package com.andresrojas.test.SpaceX.shared

import com.andresrojas.test.SpaceX.RocketLaunch
import com.andresrojas.test.SpaceX.cache.Database
import com.andresrojas.test.SpaceX.cache.DatabaseDriverFactory
import com.andresrojas.test.SpaceX.network.SpaceXApi

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class) suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}