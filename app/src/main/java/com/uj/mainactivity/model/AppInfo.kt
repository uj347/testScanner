package com.uj.mainactivity.model

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


data class AppInfo(
val androidApplicationInfo: ApplicationInfo,
val iconDrawable:Drawable,
private val label:CharSequence?,
private val installMillisDate:Long
) {
    val name:String
        get() = label?.toString()?:androidApplicationInfo.packageName

    val targetSdk:Int
        get() = androidApplicationInfo.targetSdkVersion

    val packageName
        get() = androidApplicationInfo.packageName


    val appSizeBytes by lazy { androidApplicationInfo.getAppSizeInBytes() }

    val appSizeMbytes =appSizeBytes.bytesToMbytes()

    val installDate:String
        get() = installMillisDate.millisToDate()

    private fun ApplicationInfo.getAppSizeInBytes():Long {
        return File(publicSourceDir).length()
    }

    private fun Long.bytesToMbytes():String{
       val df=DecimalFormat("#.###")
        return df.format (this.toFloat()/1_048_576)+" MB"
    }

    private fun Long.millisToDate():String{
        val dateFormat=SimpleDateFormat("dd/MM/yyyy")
       val calendar= Calendar.getInstance().also { it.timeInMillis = this }
        return dateFormat.format(calendar.time)
    }



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppInfo

        if (name != other.name) return false
        if (packageName != other.packageName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (packageName?.hashCode() ?: 0)
        return result
    }





}