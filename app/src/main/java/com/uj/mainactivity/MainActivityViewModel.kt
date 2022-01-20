package com.uj.mainactivity

import android.animation.ObjectAnimator
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uj.mainactivity.appinfoscanner.AppInfoScanner
import com.uj.mainactivity.appinfoscanner.ScanResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class MainActivityViewModel():ViewModel() {




    private val appInfoScanner:AppInfoScanner=AppInfoScanner()
     private  val _appScanResult: MutableStateFlow<ScanResult> = MutableStateFlow(ScanResult.NotCompletedScan())


    val appScanResult:StateFlow<ScanResult> =_appScanResult.asStateFlow()

    fun dropScannResults(){
        _appScanResult.value=ScanResult.NotCompletedScan()
    }

    fun scanForInstalledApps(androidContext: Context){
     CoroutineScope(viewModelScope.coroutineContext+Dispatchers.Default)
         .launch {
             _appScanResult.value=ScanResult.ScannInProgress()
             _appScanResult.value=appInfoScanner.performScan(androidContext)
         _appScanResult.value=appInfoScanner.performScan(androidContext)}

    }









}