package br.com.alarm.app.host

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import br.com.alarm.app.R
import br.com.alarm.app.databinding.ActivityHostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHostBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onInitView()
    }

    private fun onInitView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostContainer) as NavHostFragment

        navController = navHostFragment.navController


        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.alarm_nav_graph -> {
                    navController.navigate(R.id.alarm_nav_graph)
                    true
                }

                R.id.timer_up_nav_graph -> {
                    true
                }

                R.id.timer_down_nav_graph -> {
                    true
                }

                R.id.settings_nav_graph -> {
                    true
                }

                else -> false
            }
        }
    }
}