package com.uj.mainactivity.fragments.recyclergrid

import android.media.browse.MediaBrowser
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uj.mainactivity.model.AppInfo
import com.uj.testscanner.R
import com.uj.testscanner.databinding.RecyclerHolderBinding

class AppsRecyclerAdapter(

    var dataSet:List<AppInfo> = listOf(),
    val onclickItemCallback:(index:Int,
                             iconView:ImageView?)->Unit
):RecyclerView.Adapter<AppsRecyclerAdapter.AppInfoViewHolder>() {

companion object{
    const val TAG="RVAdapter"
}

    fun updateDataset(newDataSet:List<AppInfo>){
        dataSet=newDataSet
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoViewHolder {

        val viewHolderView=RecyclerHolderBinding
            .inflate(LayoutInflater.from(parent.context),parent,false).root
        Log.d(TAG, "onCreateViewHolder: parent is:$parent   viewholderview is: $viewHolderView ")
        return AppInfoViewHolder(viewHolderView, onclickItemCallback)
    }

    override fun onBindViewHolder(holder: AppInfoViewHolder, position: Int) {
       holder.applyAppinfo(dataSet.get(position))
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class AppInfoViewHolder(itemView:View,
                            private val dataSetCallBack:(index:Int,
                                                           iconView:ImageView?)->Unit):
        RecyclerView.ViewHolder(itemView),View.OnClickListener{
        var appInfo:AppInfo?=null
        val icon:ImageView
        val appName:TextView
        init {
            icon =itemView.findViewById(R.id.recycler_app_icon_view)
            appName=itemView.findViewById(R.id.recycler_app_name_field)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) =dataSetCallBack(adapterPosition,icon)

        fun applyAppinfo(newAppInfo: AppInfo){
            appInfo=newAppInfo
            icon.setImageDrawable(appInfo?.iconDrawable)
            appName.setText(appInfo?.name)
        }
        }
}