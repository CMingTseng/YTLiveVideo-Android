package com.skdev.ytlivevideo.ui.router

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.skdev.ytlivevideo.model.youtubeApi.liveBroadcast.LiveBroadcastItem
import com.skdev.ytlivevideo.ui.broadcastPreview.BroadcastPreview

@SuppressLint("StaticFieldLeak")
object Router {

    var startActivityType: StartActivity? = null
    var currentContext: Context? = null

    enum class StartActivity {
        EVENT_PREVIEW {
            override fun run() {
            }
            override fun run(params: Any) {
                openEventPreview(params)
            }
        };

        abstract fun run()
        abstract fun run(params: Any)
    }

    init {
        println("init completed")
    }

    fun startActivityIfNeeded() {
        if (startActivityType != null) {
            startActivityType!!.run()
            startActivityType = null
        }
    }

    // Actions
    private fun openEventPreview(params: Any) {
        if (currentContext == null) {
            return
        }
        val broadcastItem = params as? LiveBroadcastItem
        if (broadcastItem != null) {
            val intent = Intent(currentContext!!, BroadcastPreview::class.java)
            intent.putExtra("broadcastId", broadcastItem.id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            currentContext!!.startActivity(intent)
        }
    }
}