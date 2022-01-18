package testscanner.mainactivity

import android.content.ContentValues.TAG
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uj.mainactivity.MainActivityViewModel
import com.uj.testscanner.R
import com.uj.testscanner.databinding.ActivityMainBinding
import com.uj.testscanner.databinding.FragmentMainScreenBinding



class MainActivity : AppCompatActivity() {

    lateinit var viewModelFactory:ViewModelProvider.Factory
    val viewModel:MainActivityViewModel by viewModels ()

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.customSwitch.apply {
            checkDayOrNight()

            this.setOnCheckedChangeListener {switch,chkd->

            when(chkd){
                true->{AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)}
                false->{AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)}
                }
            }
        }
    }



    fun SwitchCompat.checkDayOrNight(){
        val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when(isNightTheme){
            Configuration.UI_MODE_NIGHT_YES -> isChecked=true
            Configuration.UI_MODE_NIGHT_NO ->isChecked=false
        }

    }

}