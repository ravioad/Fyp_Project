package com.example.jarvishome.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jarvishome.R
import com.example.jarvishome.databinding.ActivitySignUpBinding
import com.example.jarvishome.makeGone
import com.example.jarvishome.makeVisible
import com.example.jarvishome.models.User
import com.example.jarvishome.showToastShort
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    private var mAuth: FirebaseAuth? = null
    private var databaseRef: DatabaseReference? = null
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseRef = FirebaseDatabase.getInstance().getReference("Users")
        mAuth = FirebaseAuth.getInstance()
        binding.signUpButton.setOnClickListener(this)
    }

    private fun signUp(email: String, password: String) {
        showLoading()
        mAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                hideLoading()
                if (task.isSuccessful) {
                    Log.e("TAG", "createUserWithEmail:success")
                    val user = User(binding.nameEditText.text.toString(), email)
                    databaseRef?.child(mAuth?.currentUser?.uid.toString())?.setValue(user)
                        ?.addOnCompleteListener { taskAdd ->
                            if (taskAdd.isSuccessful) {
                                updateUI(mAuth?.currentUser)
                            } else {
                                showToastShort("User signUp add failed!")
                            }
                        }
                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@SignUpActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }


    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        } else {
            showToastShort("User is null!")
        }
    }

    private fun showLoading() {
        binding.progressBar.makeVisible()
        binding.signUpButton.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.makeGone()
        binding.signUpButton.isEnabled = true
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
        if (binding.nameEditText.text.toString() == "") {
            binding.nameEditText.error = "Please enter valid name!"
            result = false
        }
        if (binding.passwordEditText.text.toString() == "") {
            binding.passwordEditText.error = "Please enter valid password!"
            result = false
        }
        return result
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.signUpButton -> {
                if (validateFields()) {
                    signUp(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    )
                }
            }
        }
    }
}