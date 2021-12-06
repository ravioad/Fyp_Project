package com.example.jarvishome.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jarvishome.R
import com.example.jarvishome.databinding.ActivityLoginBinding
import com.example.jarvishome.makeGone
import com.example.jarvishome.makeVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var mAuth: FirebaseAuth? = null
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        binding.signUpTextView.setOnClickListener(this)
        binding.signInButton.setOnClickListener(this)
    }

    private fun showLoading() {
        binding.progressBar.makeVisible()
        binding.signInButton.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.makeGone()
        binding.signInButton.isEnabled = true
    }

    private fun login(email: String, password: String) {
        showLoading()
        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
            hideLoading()
            if (task.isSuccessful) {
                Log.e("TAG", "signInWithEmail:success")
                val user = mAuth?.currentUser
                updateUI(user)
            } else {
                Log.w("TAG", "signInWithEmail:failure", task.exception)
                Toast.makeText(
                    this@LoginActivity, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
                updateUI(null)
            }
        }
    }

    private fun validateFields(): Boolean {
        var result = true
        val isEmailValid =
            android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString())
                .matches()
        if (!isEmailValid) {
            binding.emailEditText.error = "Please enter valid email!"
            result = false
        }
        if (binding.passwordEditText.text.toString() == "") {
            binding.passwordEditText.error = "Please enter valid password!"
            result = false
        }
        return result
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth?.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.signUpTextView -> {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            R.id.signInButton -> {
                if (validateFields()) {
                    login(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    )
                }
            }
        }
    }
}