package testscanner.mainactivity

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.uj.mainactivity.MainActivityViewModel
import com.uj.testscanner.R
import com.uj.testscanner.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var viewModelFactory:ViewModelProvider.Factory
    val viewModel:MainActivityViewModel by viewModels ()

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.menuButton.setOnClickListener {showPopup(binding.root) }
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



   private fun SwitchCompat.checkDayOrNight(){
        val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when(isNightTheme){
            Configuration.UI_MODE_NIGHT_YES -> isChecked=true
            Configuration.UI_MODE_NIGHT_NO ->isChecked=false
        }

    }


    private fun showPopup(view: View) {
        val popup = PopupMenu(this, binding.menuButton)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_main_activity, popup.menu)
        popup.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.exit_app_menu_item->{
                    finishAffinity()
                    System.exit(0)
                    true
                }
                else->false
            }
        }
        popup.show()
    }




}