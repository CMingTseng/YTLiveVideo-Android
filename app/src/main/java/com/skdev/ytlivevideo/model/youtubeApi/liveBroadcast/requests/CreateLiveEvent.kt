package com.skdev.ytlivevideo.model.youtubeApi.liveBroadcast.requests

import android.app.Activity
import android.util.Log
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.skdev.ytlivevideo.R
import com.skdev.ytlivevideo.model.youtubeApi.liveBroadcast.LiveBroadcastItem
import com.skdev.ytlivevideo.model.youtubeApi.liveBroadcast.YouTubeLiveBroadcastRequest
import com.skdev.ytlivevideo.ui.mainScene.view.MainActivity
import com.skdev.ytlivevideo.util.ProgressDialog
import kotlinx.coroutines.*
import java.io.IOException

object CreateLiveEvent  {
    fun createLiveEventAsync(context: Activity, credential: GoogleAccountCredential, name: String, description: String) : Deferred<List<LiveBroadcastItem>?> =
        CoroutineScope(Dispatchers.IO).async() {
            val list = runRequest(credential, name, description)
            return@async list
        }

    private fun runRequest(credential: GoogleAccountCredential,
                                name: String,
                                description: String) : List<LiveBroadcastItem>? {
        val transport: HttpTransport = NetHttpTransport()
        val jsonFactory: JsonFactory = GsonFactory()
        val youtube = YouTube.Builder(transport, jsonFactory, credential)
                .setApplicationName(MainActivity.APP_NAME)
                .build()

        Log.d("${Thread.currentThread()}", "Current thread")

        YouTubeLiveBroadcastRequest.createLiveEvent(youtube, description, name)
        try {
            val listItems = YouTubeLiveBroadcastRequest.getLiveEvents(youtube)
            Log.d(TAG, "Current my list broadcasts: $listItems")
            return listItems
        } catch (e: IOException) {
            Log.e(TAG, e.localizedMessage)
        }
        return null
    }

    private val TAG: String = CreateLiveEvent::class.java.name
}
