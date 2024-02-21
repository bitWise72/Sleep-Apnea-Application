package com.example.sleepapnea.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Identity
import android.util.Log
import android.widget.ImageButton
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.sleepapnea.R
import com.example.sleepapnea.presentation.sign_in.GoogleSignInClient
import com.example.sleepapnea.presentation.sign_in.SignInViewModel
import com.example.sleepapnea.presentation.sign_in.SignInViewModelFactory
import com.example.sleepapnea.presentation.sign_in.SignInstate
import com.example.sleepapnea.sign_in_Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val googleSignInClient by lazy {
        GoogleSignInClient(
            context=applicationContext,
            oneTapClient = com.google.android.gms.auth.api.identity.Identity
                .getSignInClient(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val google_sign_in_button= findViewById<ImageButton>(R.id.google_sign_in)

        val signInViewModel= ViewModelProvider(this,
            SignInViewModelFactory())[SignInViewModel::class.java]

        val fragment= supportFragmentManager.findFragmentById(R.id.login_fragment_container) as sign_in_Fragment
        supportFragmentManager.beginTransaction()
           .replace(R.id.login_fragment_container,fragment)
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()

        val signInLauncher= registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) {

            if (it.resultCode== RESULT_OK){
                lifecycleScope.launch {
                    val signInResult= it.data?.let { intent ->
                        googleSignInClient.getSignInResultFromIntent(intent)
                    }

                    signInViewModel.getState(signInResult).observe(this@LoginActivity
                    ) {state->
                        if (state.isSignedInSuccessful){
                            // add user to database
                        }
                    }

                }
            }

        }

        google_sign_in_button.setOnClickListener{
            lifecycleScope.launch {
                val signInIntentSender= googleSignInClient.signIn()
                Log.d("TAG_1", "onCreate: clicked")
                signInLauncher.launch(IntentSenderRequest.Builder(
                    signInIntentSender ?: return@launch
                ).build())
            }
        }

    }

    override fun onStart() {
        super.onStart()
        var currentUser= googleSignInClient.getSignedInUser()
    }
}