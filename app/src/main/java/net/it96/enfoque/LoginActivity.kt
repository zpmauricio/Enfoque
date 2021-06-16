package net.it96.enfoque

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import net.it96.enfoque.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 423
    }

    private val TAG = "LoginActivity"
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        login()
    }

    private fun login() {

        val providers = arrayListOf(
//            AuthUI.IdpConfig.EmailBuilder().build(),
//            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build() //,
//            AuthUI.IdpConfig.FacebookBuilder().build(),
//            AuthUI.IdpConfig.TwitterBuilder().build()
        )
        binding.btnLogin.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(false)
                    .build(),
                RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                Log.i(TAG, "***MZP*** user: ${user?.email}")
                startActivity(Intent(this, ProjectActivity::class.java))
                finish()
            } else {
//                if(response == null) {
//                    finish()
//                }
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                if(response?.error?.errorCode == ErrorCodes.DEVELOPER_ERROR) {
                    Toast.makeText(this,
                        "Ocurrio un error ${response.error!!.message.toString()}",
                        Toast.LENGTH_LONG).show()
                    Toast.makeText(this,
                        "response.getError()?.getErrorCode() ${response.getError()?.getErrorCode()}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}