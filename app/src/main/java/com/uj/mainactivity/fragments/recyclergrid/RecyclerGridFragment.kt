package com.uj.mainactivity.fragments.recyclergrid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.FragmentNavigatorExtras

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.uj.mainactivity.MainActivityViewModel
import com.uj.mainactivity.appinfoscanner.ScanResult
import com.uj.mainactivity.model.AppInfo

import com.uj.testscanner.databinding.FragmentRecyclerGridBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RecyclerGridFragment : Fragment() {
    companion object {
        const val TAG = "RecyclerGridFragment"
    }

    private val viewModel: MainActivityViewModel by activityViewModels()
    private var _binding: FragmentRecyclerGridBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         Log.d(TAG, "onCreate!!!!")
        _binding = FragmentRecyclerGridBinding.inflate(inflater, container, false)

        binding.scanResultRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = AppsRecyclerAdapter ( onclickItemCallback = adapterCallback)
                .also { it.listenForAppInfo()
                }
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    private val adapterCallback:(Int)->Unit ={position:Int ->

              findNavController()
                  .navigate(
                      RecyclerGridFragmentDirections.actionRecyclerGridFragmentToDetailsFragment(
                          position
                      ))
    }




    private fun AppsRecyclerAdapter.listenForAppInfo() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.appScanResult.collect {
                    Log.d(TAG, "initializeRecyclerView: Another scan result obtained be RCView")
                    if (it is ScanResult.CompletedScanResult) {
                        this@listenForAppInfo.updateDataset(it.content)
                    }
                }
            }

        }
    }
}


