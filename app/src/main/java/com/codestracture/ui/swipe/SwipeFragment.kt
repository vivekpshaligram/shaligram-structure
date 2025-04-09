package com.codestracture.ui.swipe

import android.graphics.drawable.TransitionDrawable
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.codestracture.R
import com.codestracture.databinding.FragmentSwipeBinding
import com.codestracture.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SwipeFragment : BaseFragment<FragmentSwipeBinding, SwipeViewModel>() {

    override val layoutId: Int = R.layout.fragment_swipe

    override val viewModel: SwipeViewModel by viewModels()

    private lateinit var adapter: ViewPagerAdapter

    private val list = listOf(
        ImageModelWithText(
            "Lorem ipsum",
            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content. Lorem ipsum may be used as a",
            R.drawable.img_1
        ),
        ImageModelWithText(
            "Lorem ipsum",
            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content. Lorem ipsum may be used as a",
            R.drawable.img_2
        ),
        ImageModelWithText(
            "Lorem ipsum",
            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content. Lorem ipsum may be used as a",
            R.drawable.ima_3
        ),
        ImageModelWithText(
            "Lorem ipsum",
            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content. Lorem ipsum may be used as a",
            R.drawable.img_1
        ),
        ImageModelWithText(
            "Lorem ipsum",
            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content. Lorem ipsum may be used as a",
            R.drawable.img_2
        ),
        ImageModelWithText(
            "Lorem ipsum",
            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content. Lorem ipsum may be used as a",
            R.drawable.ima_3
        ),
        ImageModelWithText(
            "Lorem ipsum",
            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content. Lorem ipsum may be used as a",
            R.drawable.img_1
        ),
        ImageModelWithText(
            "Lorem ipsum",
            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content. Lorem ipsum may be used as a",
            R.drawable.img_2
        ),
        ImageModelWithText(
            "Lorem ipsum",
            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content. Lorem ipsum may be used as a",
            R.drawable.ima_3
        )
    )

    override fun observeEvents() {
    }

    override fun initView() {
        adapter = ViewPagerAdapter(list)
        binding.viewPager.adapter = adapter
        binding.indicator.attachToPager(binding.viewPager)

        // Change background image when ViewPager page changes
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                crossFadeBackground(list[position].resId)
            }
        })

        // Initially set background image to the first image
        binding.backgroundImageView.setImageResource(list.first().resId)
    }

    private fun crossFadeBackground(imageResId: Int) {
        val currentDrawable = binding.backgroundImageView.drawable
        val transitionDrawable = TransitionDrawable(
            arrayOf(
                currentDrawable,
                resources.getDrawable(imageResId, null)
            )
        )
        binding.backgroundImageView.setImageDrawable(transitionDrawable)
        transitionDrawable.startTransition(500) // Adjust duration as needed
    }
}

data class ImageModelWithText(
    val title: String,
    val subTitle: String,
    var resId: Int
)