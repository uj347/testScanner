package com.uj.mainactivity.appinfoscanner

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.uj.mainactivity.model.AppInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DateTimeException


class AppInfoScanner  constructor(private val loadUnnamedPackages:Boolean=false) {
companion object{
    const val TAG="AppInfoScanner"
}


    suspend fun performScan(androidContext: Context):ScanResult.CompletedScanResult{
            val scanResult:ScanResult.CompletedScanResult
            Log.d(TAG, "performScan: In launch")

            androidContext.packageManager.apply{
                getInstalledApplications(PackageManager.GET_META_DATA)

                    .filter {
                       when(loadUnnamedPackages){
                           true->true
                           false->{
                               it.loadLabel(this)!=null
                           }
                       }
                    }
                    .map{
                        val time = getPackageInfo(it.packageName,0).firstInstallTime
                        AppInfo(it,
                            it.loadIcon(this),
                            it.loadLabel(this),
                            time
                        )

                    }
                    .let {
                        Log.d(TAG, "Emmiting successfull result")
                        scanResult=ScanResult.CompletedScanResult(it)
                }
            }
        return scanResult

    }


}


sealed interface ScanResult {
    class CompletedScanResult(val content: List<AppInfo>) : ScanResult
    class NotCompletedScan : ScanResult
    class ScannInProgress:ScanResult
}