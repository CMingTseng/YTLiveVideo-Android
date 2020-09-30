package com.skdev.ytlivevideo.model.youtubeApi.liveBroadcast.requests

import android.util.Log
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.skdev.ytlivevideo.model.youtubeApi.liveBroadcast.LiveBroadcastItem
import com.skdev.ytlivevideo.model.youtubeApi.liveBroadcast.YouTubeLiveBroadcastRequest
import com.skdev.ytlivevideo.util.Config
import kotlinx.coroutines.*
import java.io.IOException

object FetchAllLiveEvents {

     suspend fun runAsync(credential: GoogleAccountCredential, state: BroadcastState?, broadcastId: String? = null) : List<LiveBroadcastItem>? =
        withContext(Dispatchers.IO) {
            try {
                val list = fetchAllLiveEvents(credential, state, broadcastId)
                list?.forEach{it.state = state}
                Log.d(TAG, list.toString())
                return@withContext list
            } catch (e: IOException) {
                Log.e(TAG, "Failed fetch all live events:", e)
                val message = e.cause?.message ?: "Error while fetching live events with '${state.toString()}' state"
                throw IOException(message)
            }
        }

    private fun fetchAllLiveEvents(credential: GoogleAccountCredential, state: BroadcastState?, broadcastId: String?) : List<LiveBroadcastItem>? {
        Log.d(TAG, "getLiveEventRequest")
        val transport: HttpTransport = NetHttpTransport()
        val jsonFactory: JsonFactory = GsonFactory()
        val youtube = YouTube.Builder(transport, jsonFactory, credential)
            .setApplicationName(Config.APP_NAME)
            .build()
        val listItems = YouTubeLiveBroadcastRequest.getLiveEvents(youtube, state, broadcastId)
        Log.d(TAG, "My current list broadcasts: $listItems")
        return listItems
    }

    private val TAG: String = FetchAllLiveEvents::class.java.name
}
