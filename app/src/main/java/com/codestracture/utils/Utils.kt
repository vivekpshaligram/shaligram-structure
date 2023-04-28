package com.codestracture.utils

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import at.aau.itec.android.mediaplayer.MediaSource
import at.aau.itec.android.mediaplayer.UriSource
import at.aau.itec.android.mediaplayer.dash.AdaptationLogic
import at.aau.itec.android.mediaplayer.dash.DashSource
import at.aau.itec.android.mediaplayer.dash.SimpleRateBasedAdaptationLogic

object Utils {

    fun uriToMediaSource(context: Context?, uri: Uri): MediaSource? {
        var source: MediaSource? = null
        if (uri.toString().endsWith(".mpd")) {
            val adaptationLogic: AdaptationLogic
            adaptationLogic = SimpleRateBasedAdaptationLogic()
            source = DashSource(context, uri, adaptationLogic)
        } else {
            source = UriSource(context, uri)
        }
        return source
    }

    fun uriToMediaSourceAsync(
        context: Context,
        uri: Uri,
        callback: MediaSourceAsyncCallbackHandler
    ) {
        val loadingTask = LoadMediaSourceAsyncTask(context, callback)
        try {
            loadingTask.execute(uri).get()
        } catch (e: Exception) {
            Log.e("MyTag", e.message, e)
        }
    }

    private class LoadMediaSourceAsyncTask(
        private val mContext: Context,
        private val mCallbackHandler: MediaSourceAsyncCallbackHandler
    ) : AsyncTask<Uri, Void?, MediaSource?>() {
        private var mMediaSource: MediaSource? = null
        private var mException: java.lang.Exception? = null

        override fun onPostExecute(mediaSource: MediaSource?) {
            if (mException != null) {
                mCallbackHandler.onException(mException)
            } else {
                mCallbackHandler.onMediaSourceLoaded(mMediaSource)
            }
        }

        override fun doInBackground(vararg params: Uri): MediaSource? {
            return try {
                mMediaSource = uriToMediaSource(mContext, params[0])
                mMediaSource
            } catch (e: java.lang.Exception) {
                mException = e
                null
            }
        }
    }

    interface MediaSourceAsyncCallbackHandler {
        fun onMediaSourceLoaded(mediaSource: MediaSource?)
        fun onException(e: java.lang.Exception?)
    }
}