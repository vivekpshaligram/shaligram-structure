package com.codestracture.ui.video

import android.net.Uri
import android.util.Log
import android.widget.MediaController
import androidx.fragment.app.viewModels
import at.aau.itec.android.mediaplayer.UriSource
import com.codestracture.R
import com.codestracture.databinding.FragmentVideoScreenBinding
import com.codestracture.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoPlayerScreeenFragement : BaseFragment<FragmentVideoScreenBinding, VideoPlayerScreeenViewModel>() {

    override val layoutId: Int = R.layout.fragment_video_screen

    override val viewModel: VideoPlayerScreeenViewModel by viewModels()

    private var mMediaPlayerControl: MediaController.MediaPlayerControl? = null
    private var mMediaController: MediaController? = null

    override fun initView() {
        mMediaPlayerControl = binding.mVideoView
        mMediaController = MediaController(context)
        mMediaController?.setAnchorView(binding.mVideoView)
        mMediaController?.setMediaPlayer(mMediaPlayerControl)

        binding.mVideoView.setOnPreparedListener {
            Log.d("MyTag", "setOnPreparedListener")
            binding.mVideoView.start()
        }
        val uri = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")

        val uriSource = UriSource(context, uri)
        Log.d("MyTag", "uriSource:$uriSource")
        Log.d("MyTag", "uriSource:${uriSource.videoExtractor}")
        // binding.mGLVideoView.setVideoSource(uriSource)

        /*Utils.uriToMediaSourceAsync(requireContext(), uri, object : Utils.MediaSourceAsyncCallbackHandler {
            override fun onMediaSourceLoaded(mediaSource: MediaSource?) {
                Log.e("MyTag", "onMediaSourceLoaded")
                binding.mGLVideoView.setVideoSource(mediaSource)
            }

            override fun onException(e: Exception?) {
                Log.e("MyTag", "error loading video", e)
            }
        })*/
    }

    override fun observeEvents() {
    }
}