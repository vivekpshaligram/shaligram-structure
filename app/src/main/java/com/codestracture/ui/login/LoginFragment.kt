package com.codestracture.ui.login

import androidx.fragment.app.viewModels
import com.codestracture.R
import com.codestracture.databinding.FragmentLoginBinding
import com.codestracture.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override val layoutId: Int = R.layout.fragment_login

    override val viewModel: LoginViewModel by viewModels()

    override fun observeEvents() {
        viewModel.viewModelEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is LoginViewModelEvent.LoginSuccess -> {
                    gotoHomeScreen()
                }
                is LoginViewModelEvent.LoginError -> {
                    showLoginErrorDialog(event.message)

                }
            }
        }
    }

    override fun initView() {

    }

    private fun showLoginErrorDialog(message: String) {
        iDialog.showDialog(
            message = message,
            cancelable = true,
            positiveButtonId = R.string.btn_login,
            positiveClick = {
                gotoHomeScreen()
            })
    }

    private fun gotoHomeScreen() {
        LoginFragmentDirections
            .actionLoginFragmentToHomeFragment()
            .navigateTo()
    }

    private fun callLogin() {
        //viewModel.preformLoginWithUserData()
        /*viewModel.preformLoginWithLiveData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Loading -> Log.d("MyTag", "Loading")
                is ApiResult.Success -> {
                    Log.d("MyTag", "Success")
                }
                is ApiResult.Error -> {
                    Log.d("MyTag", "Error")
                }
            }
        }*/
    }
}