package com.codestracture.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.codestracture.databinding.TripDetailsViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TripDetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: TripDetailsViewBinding by lazy {
        TripDetailsViewBinding.inflate(LayoutInflater.from(context), null, true)
    }

    private var rootViewClickListener: (View) -> Unit? = {}

    init {
        addView(binding.root)
    }

    @ModelProp
    fun handleState(state: String) {
        binding.tvData.text = state
        binding.root.setOnClickListener { rootViewClickListener.invoke(it) }
    }

    @ModelProp(options = [ModelProp.Option.DoNotHash])
    fun setRootClickListener(listener: (View) -> Unit) {
        rootViewClickListener = listener
    }
}