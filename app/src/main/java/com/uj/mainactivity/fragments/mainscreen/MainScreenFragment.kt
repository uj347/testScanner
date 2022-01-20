package com.uj.mainactivity.fragments.mainscreen

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.Interpolator
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.uj.mainactivity.MainActivityViewModel

import com.uj.mainactivity.appinfoscanner.ScanResult
import com.uj.testscanner.R
import com.uj.testscanner.databinding.FragmentMainScreenBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect


class MainScreenFragment : Fragment() {


    companion object {
        const val TAG = "MainScreenFragment"
    }

    private var _binding: FragmentMainScreenBinding? = null

    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()

    private var bigButtonAnimator:ObjectAnimator?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        setScanListener()

        bigButtonAnimator = ObjectAnimator.ofPropertyValuesHolder(binding.bigScanButton,
                    PropertyValuesHolder.ofFloat("scaleX", 1f, 1.1f),
                    PropertyValuesHolder.ofFloat("scaleY", 1f, 1.1f)).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            doOnCancel {
                binding.bigScanButton.apply {
                    scaleX=1f
                    scaleY=1f
                }
            }
        }

        binding.bigScanButton.setOnClickListener {
            performScan()
        }
        return binding.root
    }





private fun startButtonAnimation()= bigButtonAnimator?.start()

private fun stopButtonAnimation()=bigButtonAnimator?.cancel()



    private fun setScanListener(){
    lifecycleScope.launch {
            val scanListenerScope=this
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.dropScannResults()
               viewModel.appScanResult.collect{ scanResult ->
                   Log.d(TAG, "next scanResult:${scanResult::class.simpleName}")
                   when (scanResult) {
                       is ScanResult.CompletedScanResult -> {
                           Log.d(TAG, "performScan: Scan result is completedScan result")

                           MainScreenFragmentDirections
                               .actionMainScreenFragment2ToRecyclerGridFragment().let {
                                   Log.d(
                                       TAG,
                                       "We have completed scan with size of ${scanResult.content.size}, next step is navigate"
                                   )
                                   findNavController().navigate(it)
                                   stopButtonAnimation()
                                   scanListenerScope.cancel()
                               }
                       }

                       is ScanResult.NotCompletedScan -> {
                           Log.d(TAG, "performScan: Ignoring Noot completed scanresult")
                           //do noth
                       }
                       is ScanResult.ScannInProgress->{
                           Log.d(TAG, "performScan: Ignoring Scan in progress scanresult")
                           startButtonAnimation()
                       }
                   }
               }
            }
        }
    }


    private fun performScan() {
        lifecycleScope.launch{
            withResumed {
                Log.d(TAG, "launching scan")
                viewModel.scanForInstalledApps(requireContext())
            }
        }
    }

}







