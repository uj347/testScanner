package com.uj.mainactivity.fragments.mainscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.uj.mainactivity.MainActivityViewModel
import com.uj.mainactivity.appinfoscanner.ScanResult
import com.uj.testscanner.R
import com.uj.testscanner.databinding.FragmentDetailsBinding
import com.uj.testscanner.databinding.FragmentRecyclerGridBinding


class DetailsFragment : Fragment() {

    private val viewModel:MainActivityViewModel by activityViewModels()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val arguments:DetailsFragmentArgs by navArgs()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentDetailsBinding.inflate(inflater,container,false)
        binding.backButton.setOnClickListener{
                findNavController().
                navigate(DetailsFragmentDirections.actionDetailsFragmentToRecyclerGridFragment())
        }
        val rootView=binding.root



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.appScanResult.value.let {
                if (it is ScanResult.CompletedScanResult){
                    it.content.get(arguments.appInfoIndex).let{appInfo->
                        appIconView.setImageDrawable(appInfo.iconDrawable)
                        appNameText.setText(appInfo.name)
                        targetSdkTextField.setText(appInfo.targetSdk.toString())
                        sizeTextField.setText(appInfo.appSizeMbytes.toString())
                        installDateTextField.setText(appInfo.installDate)
                    }
                }
            }
        }

    }


}