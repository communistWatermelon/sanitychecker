package com.melonhead.sanitychecker

import retrofit2.Call

/**
 * serversanity
 * Created by jake on 2017-11-15, 2:30 PM
 */


object SanityChecker {
    suspend fun runTests(vararg calls: APICall<*>): JSONObjects {
        val jsonObjs = JSONObjects(emptyList(), emptyList())

        for (call in calls) {
            val server = Server(call)
            val (success, failed) = server.test()
            jsonObjs.success += success
            jsonObjs.failed += failed
        }

        return jsonObjs
    }
}

infix fun <T> Call<T>.pair(type: Class<T>) = RetroFitApi(this, type)