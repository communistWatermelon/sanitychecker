package com.melonhead.sanitychecker

import android.util.Log

class Server(private val api: APICall<*>) {
    val TAG = Server::class.java.simpleName

    suspend fun test(): ApiResult {
        val (successful, json) = api.test()
        Log.d(TAG, "Test was ${ if (successful) "successful" else "failure" }")
        return ApiResult(api.call.request().url().toString(), successful, json)
    }
}