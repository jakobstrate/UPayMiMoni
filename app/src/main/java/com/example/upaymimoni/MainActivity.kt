package com.example.upaymimoni

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.upaymimoni.presentation.navigation.AppNavHost
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

const val EMULATOR_HOST = "10.0.2.2"

//const val EMULATOR_HOST = "10.126.69.219"
//const val EMULATOR_HOST = "192.168.1.241"
const val AUTH_EMULATOR_PORT = 9099
const val FIRESTORE_EMULATOR_PORT = 8080

const val FIREBASE_STORAGE_PORT = 9199

class MainActivity : ComponentActivity() {
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureFirebaseEmulator()
        requestNotificationPermission()

        enableEdgeToEdge()
        setContent {
            UPayMiMoniTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    AppNavHost(navController)
                }
            }
        }
    }

    /**
     * This configures firebase auth to run on a emulator.
     *
     * An emulator has to be setup and running on <EMULATOR_HOST>:<EMULATOR_PORT> for this to work.
     */
    private fun configureFirebaseEmulator() {
        FirebaseApp.initializeApp(this)
        Firebase.auth.useEmulator(EMULATOR_HOST, AUTH_EMULATOR_PORT)
        Firebase.firestore.useEmulator(EMULATOR_HOST, FIRESTORE_EMULATOR_PORT)
        FirebaseStorage.getInstance().useEmulator(EMULATOR_HOST, FIREBASE_STORAGE_PORT)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermissions = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermissions) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}
