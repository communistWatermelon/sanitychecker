package com.melonhead.sanitychecker

import android.util.Log
import org.json.JSONObject

/**
 * serversanity
 * Created by jake on 2017-11-15, 2:56 PM
 */
data class JSONObjects(var success: List<JSONObject>, var failed: List<JSONObject>)

class Server(private vararg val retroFitApiList: APICall<*>) {
    val TAG = Server::class.java.simpleName

    suspend fun test(): JSONObjects {
        val success = mutableListOf<JSONObject>()
        val failed = mutableListOf<JSONObject>()
        for (api in retroFitApiList) {
            val (successful, json) = api.test()
            Log.d(TAG, "Test was ${ if (successful) "successful" else "failure" }")

            // if the test failed, stop running
            if (successful) {
                success.add(json)
            } else {
                failed.add(json)
            }
        }
        return JSONObjects(success, failed)
    }
}