package br.com.alarm.app.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import br.com.alarm.app.R
import br.com.alarm.app.host.HostActivity

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() {

    abstract val bindingInflater: (LayoutInflater) -> VB
    private var viewBinding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    val binding: VB get() = viewBinding as VB
    abstract val viewModel: BaseViewModel

    /**
     * @author L-Mosca - lucasbvmosca@gmail.com - https://github.com/L-Mosca.
     *
     * <p>This function called into BottomSheetDialogFragment.onViewCreated lifecycle.
     *
     * Use this to fetch initial data and setup layout (click events, recycler view, adapters...)</p>
     */
    abstract fun initViews()

    /**
     * @author L-Mosca - lucasbvmosca@gmail.com - https://github.com/L-Mosca.
     *
     * <p>This function called into BottomSheetDialogFragment.onViewCreated lifecycle.
     *
     * Use this to observer viewModel streams) </p>
     */
    abstract fun initObservers()

    /**
     * @author L-Mosca - lucasbvmosca@gmail.com - https://github.com/L-Mosca.
     *
     * <p>This function called into BottomSheetDialogFragment.onCreate view lifecycle.
     *
     * Use this to get bundle arguments passed by this instance invocation) </p>
     */
    abstract fun getBundleArguments()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = bindingInflater.invoke(inflater)
        return viewBinding?.root
    }

    /**
     * Set default animation style and appearance.
     * In this case all popups has width and height as MATCH_PARENT.
     * The best practice that i can create for now is this layout hierarchy:
     * ViewGroup -> CardView.
     *
     * Set a card view elevation and handle the first view group click listener (its most normal that his listener call dialog.dismiss())
     *
     */
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getBundleArguments()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    fun setBackNavigation(onBackPressed: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback {
            onBackPressed.invoke()
        }
    }

    fun restartApplication() {
        val intent = Intent(requireActivity(), HostActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        requireActivity().startActivity(intent)
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}