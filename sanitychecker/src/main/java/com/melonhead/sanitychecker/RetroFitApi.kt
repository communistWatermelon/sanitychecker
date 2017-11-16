package com.melonhead.sanitychecker

import android.util.Log
import com.squareup.moshi.Moshi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import ru.gildor.coroutines.retrofit.awaitResponse
import java.io.IOException
import java.lang.reflect.Type


/**
 * serversanity
 * Created by jake on 2017-11-15, 2:55 PM
 */
data class DataResult(val successful: Boolean, val json: JSONObject)

abstract class APICall<T>(val call: Call<T>, val type: Type) {
    abstract suspend fun test(): DataResult
}

class RetroFitApi<T>(private val api: Call<T>, type: Type) : APICall<T>(api, type) {
    private var moshi = Moshi.Builder().build()
    private val jsonAdapter = moshi.adapter<T>(type)

    override suspend fun test(): DataResult {
        return try {
            val result = api.awaitResponse()
            val successful = wasSuccessful(result)
            val json = if (successful) {
                val json = jsonAdapter.toJson(result.body())
                JSONObject(json)
            } else {
                JSONObject(result.errorBody()?.string() ?: "")
            }

            DataResult(successful, json)
        } catch (e: IOException) {
            e.printStackTrace()
            DataResult(false, JSONObject(""))
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            DataResult(false, JSONObject(""))
        }
    }

    private fun <T> wasSuccessful(response: Response<T>?): Boolean {
        response?.body() ?: return false
        if (!response.isSuccessful) return false
        if (response.errorBody() != null) return false
        return response.body() != null
    }
}

