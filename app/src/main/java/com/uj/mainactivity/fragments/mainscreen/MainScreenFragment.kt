package com.uj.mainactivity.fragments.mainscreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        setScanListener()
        binding.bigScanButton.setOnClickListener {performScan()}
        return binding.root
    }




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
                                   scanListenerScope.cancel()
                               }
                       }

                       is ScanResult.NotCompletedScan -> {
                           Log.d(TAG, "performScan: Ignoring Noot completed scanresult")
                           //do noth
                       }
                       is ScanResult.ScannInProgress->{
                           Log.d(TAG, "performScan: Ignoring Scan in progress scanresult")
                           //Do noth, mb animation later
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







