package com.android.project.tukang.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.project.tukang.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icon.alpha = 0f
        binding.icon.animate().setDuration(1500).alpha(1f)

        binding.created.alpha = 0f
        binding.created.animate().setDuration(1500).alpha(1f)

        binding.eric.alpha = 0f
        binding.eric.animate().setDuration(1500).alpha(1f).withEndAction {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}