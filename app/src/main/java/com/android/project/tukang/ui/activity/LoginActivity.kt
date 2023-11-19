package com.android.project.tukang.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.android.project.tukang.databinding.ActivityLoginBinding
import com.android.project.tukang.ui.viewmodel.LoginViewModel
import com.android.project.tukang.ui.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this@LoginActivity)
        setupAction()
    }

    private fun setupAction() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()

            when{
                email.isEmpty() -> {
                    binding.textFieldUsername.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.textFieldPassword.error = "Masukkan password"
                }
                else -> {
                    viewModel.login(binding.txtEmail.text.toString())
                }
            }
        }

        viewModel.account.observe(this) {
            if (it != null) {
                if (binding.txtPassword.text.toString() == it.passwd) {
                    startActivity(Intent(this, DashboardActivity::class.java))
                } else {
                    binding.txtPassword.error = "Password tidak sesuai"
                }
            } else {
                binding.txtEmail.error = "Username tidak ditemukan"
            }
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = LoginViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}