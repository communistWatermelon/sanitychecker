package com.melonhead.sanitychecker

class Server(private val api: APICall<*>) {
    suspend fun test(): ApiResult {
        val (successful, json) = api.test()
        return ApiResult(api.call.request().url().toString(), successful, json)
    }
}