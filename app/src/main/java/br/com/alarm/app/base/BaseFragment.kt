package br.com.alarm.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import br.com.alarm.app.R
import com.google.android.material.snackbar.Snackbar

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    abstract val bindingInflater: (LayoutInflater) -> VB
    private var viewBinding: ViewBinding? = null

    val binding: VB
        get() = viewBinding as VB
    private var currentToast: Toast? = null
    private var currentSnackBar: Snackbar? = null

    abstract val viewModel: BaseViewModel
    abstract fun initViews()
    abstract fun initObservers()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = bindingInflater.invoke(inflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            onErrorMessage(it)
        }


        initObservers()
        initViews()
    }

    open fun onErrorMessage(it: Any?) {
        when (it) {
            is Int -> showShortToast(it)
            is String -> showShortToast(it)
        }
    }

    private fun showShortToast(@StringRes stringResId: Int) {
        showShortToast(getString(stringResId))
    }

    fun showLongToast(@StringRes stringResId: Int) {
        showLongToast(getString(stringResId))
    }

    fun showShortToast(message: String) {
        showToast(message, Toast.LENGTH_SHORT)
    }

    fun showLongSnackBar(message: String) {
        showSnackBar(message, Snackbar.LENGTH_LONG)
    }

    fun showShortSnackBar(message: String) {
        showSnackBar(message, Snackbar.LENGTH_SHORT)
    }

    fun showLongToast(message: String) {
        showToast(message, Toast.LENGTH_LONG)
    }

    fun setBackNavigation(onBackPressed: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback {
            onBackPressed.invoke()
        }
    }


    private fun showToast(message: String, duration: Int) {
        currentToast?.cancel()
        currentToast = Toast.makeText(context, message, duration)
        currentToast?.show()
    }

    private fun showSnackBar(message: String, duration: Int) {
        currentSnackBar?.dismiss()
        currentSnackBar =
            Snackbar.make(binding.root, message, duration)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.purple_200))
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        currentSnackBar?.show()
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

}