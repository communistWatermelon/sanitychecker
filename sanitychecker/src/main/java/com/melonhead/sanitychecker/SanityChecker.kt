package com.melonhead.sanitychecker

import org.json.JSONObject
import retrofit2.Call

/**
 * serversanity
 * Created by jake on 2017-11-15, 2:30 PM
 */
data class ApiResult(var url: String, var success: Boolean, var json: JSONObject) {
    fun cleanOutput(): String {
        return "// ========= $url =========\n ${json.toString(1)}\n"
    }
}

object SanityChecker {
    suspend fun runTests(vararg calls: APICall<*>): List<ApiResult> {
        return runTests(listOf(*calls))
    }

    suspend fun runTests(callList: List<APICall<*>>): List<ApiResult> {
        return callList.map { Server(it).test() }
    }
}

infix fun <T> Call<T>.bindTo(type: Class<T>) = RetroFitApi(this, type)