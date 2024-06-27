package com.example.movieapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.movieapplication.R
import com.example.movieapplication.presentation.viewModel.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by inject()
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        lifecycleScope.launch {
            if (viewModel.getLogin()){
                navController.navigate(R.id.homeFragment)
            } else {
                navController.navigate(R.id.loginFragment)
            }
        }
    }
}