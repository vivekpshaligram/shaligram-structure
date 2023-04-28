package com.codestracture.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.codestracture.data.dialog.IDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    abstract val viewModel: VM

    protected lateinit var binding: B

    abstract val layoutId: Int

    abstract fun observeEvents()
    abstract fun initView()

    private lateinit var navController: NavController

    @Inject
    lateinit var iDialog: IDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.vm, viewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeEvents()
            }
        }

        initView()
    }

    protected fun NavDirections.navigateTo() =
        navController.navigateSafe(this)

    private fun NavController.navigateSafe(
        direction: NavDirections
    ) {
        val resId = direction.actionId
        val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
        if (action != null && currentDestination?.id != action.destinationId) navigate(direction)
    }

    protected fun navigateTo(resId: Int) {
        navController.navigate(resId)
    }
}