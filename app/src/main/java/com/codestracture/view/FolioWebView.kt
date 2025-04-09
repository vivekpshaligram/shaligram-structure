package com.codestracture.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.ActionMode
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.core.view.GestureDetectorCompat
import com.codestracture.R

class FolioWebView : WebView {

    private lateinit var uiHandler: Handler
    private var displayMetrics: DisplayMetrics? = null
    private var density: Float = 0.toFloat()

    private var direction: String = "VERTICAL"
    private var pageWidthCssDp: Int = 0
    private var pageWidthCssPixels: Float = 0.toFloat()

    private var totalPages = 0
    private var currentPage = 1

    private var gestureDetector: GestureDetectorCompat? = null
    private var lastGestureType: LastGestureType? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun init() {
        gestureDetector = GestureDetectorCompat(context, GestureListener())
        uiHandler = Handler(Looper.getMainLooper())
        displayMetrics = resources.displayMetrics
        density = displayMetrics!!.density
    }

    fun setTotalPages(totalPages: Int) {
        this.totalPages = totalPages
    }

    fun setDirection(direction: String) {
        this.direction = direction
    }

    @JavascriptInterface
    fun getDirection() = direction

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        pageWidthCssDp = Math.ceil((measuredWidth / density).toDouble()).toInt()
        pageWidthCssPixels = pageWidthCssDp * density
    }

    fun getScrollXPixelsForPage(page: Int): Int {
        return Math.ceil((page * pageWidthCssPixels).toDouble()).toInt()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (direction == "HORIZONTAL") {
            computeHorizontalScroll(event)
        } else {
            computeVerticalScroll(event)
        }
    }

    private fun computeHorizontalScroll(event: MotionEvent?): Boolean {
        if (event == null)
            return false

        // Rare condition in fast scrolling
        if (gestureDetector == null)
            return false

        val gestureReturn = gestureDetector!!.onTouchEvent(event)
        if (gestureReturn)
            return true

        val superReturn = super.onTouchEvent(event)

        if (event.action == MotionEvent.ACTION_UP) {
            if (lastGestureType == LastGestureType.OnScroll || lastGestureType == LastGestureType.OnFling) {
                Log.d("MyTag", "-> onTouchEvent -> takeOverScrolling = true, " + "lastGestureType = " + lastGestureType)
                // takeOverScrolling = true
            } else if (lastGestureType == LastGestureType.OnSingleTapUp) {
                val x = event.x
                val y = event.y
                if (x < (width * 0.5) && y > (height * 0.6)) {
                    Log.d("MyTag", "OnSingleTapUp -> Next")
                    // also check if item we set if valid or not i.e (getCurrentItem() - 1) > 0
                    if (((totalPages - currentPage) - 1) >= 0) {
                        uiHandler.post {
                            currentPage += 1
                            val scrollX = getScrollXPixelsForPage((totalPages - currentPage))
                            scrollTo(scrollX, 0)
                        }
                    }
                } else if (x > (width * 0.5) && y > (height * 0.6)) {
                    Log.d("MyTag", "OnSingleTapUp -> Previous")
                    // also check if item we set if valid or not i.e (getCurrentItem() + 1) < maxChildCount
                    if (((totalPages - currentPage) + 1) <= totalPages) {
                        uiHandler.post {
                            currentPage -= 1
                            val scrollX = getScrollXPixelsForPage((totalPages - currentPage))
                            scrollTo(scrollX, 0)
                        }
                    }
                } else {
//                    uiHandler?.post {
//                        folioWebView?.folioBaseFragment?.toggleSystemUI()
//                    }
                }
            }
            lastGestureType = null
        }

        return superReturn
    }

    private fun computeVerticalScroll(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun startActionMode(callback: ActionMode.Callback?, type: Int): ActionMode? {
        val modifiedMode = super.startActionMode(callback, type)
        val menu = modifiedMode.menu
        menu.clear()
        modifiedMode.menuInflater.inflate(R.menu.text_selection, menu)
        menu.findItem(R.id.menu_copy).setOnMenuItemClickListener { item: MenuItem ->
            loadUrl("javascript:onTextSelectionItemClicked(" + item.itemId + ")")
            false
        }
        menu.findItem(R.id.menu_share).setOnMenuItemClickListener { item: MenuItem ->
            loadUrl("javascript:onTextSelectionItemClicked(" + item.itemId + ")")
            false
        }
        return modifiedMode
    }

    @JavascriptInterface
    fun onTextSelectionItemClicked(id: Int, selectedText: String) {
        Log.d("MyTag", "onTextSelectionItemClicked::$id : $selectedText")
    }

    private enum class LastGestureType {
        OnSingleTapUp, OnLongPress, OnFling, OnScroll
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent): Boolean {
            super@FolioWebView.onTouchEvent(e)
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            // Log.d(LOG_TAG, "-> onSingleTapUp");
            lastGestureType = LastGestureType.OnSingleTapUp
            return false
        }

        override fun onLongPress(e: MotionEvent) {
            super.onLongPress(e)
            // Log.d(LOG_TAG, "-> onLongPress -> " + e);
            lastGestureType = LastGestureType.OnLongPress
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            // Log.v(LOG_TAG, "-> onScroll -> e1 = " + e1 + ", e2 = " + e2 + ", distanceX = " + distanceX + ", distanceY = " + distanceY);
            lastGestureType = LastGestureType.OnScroll
            return false
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            // Log.d(LOG_TAG, "-> onFling -> e1 = " + e1 + ", e2 = " + e2 + ", velocityX = " + velocityX + ", velocityY = " + velocityY);
            lastGestureType = LastGestureType.OnFling
            val deltaX = e2.x - e1!!.x
            val deltaY = e2.y - e1.y
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (Math.abs(deltaX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (deltaX > 0) {
                        Log.d("MyTag", "onSwipeRight")
                        // onSwipeRight()
                    } else {
                        Log.d("MyTag", "onSwipeLeft")
                        // onSwipeLeft()
                    }
                    return true
                }
            }
            return false
        }
    }
}