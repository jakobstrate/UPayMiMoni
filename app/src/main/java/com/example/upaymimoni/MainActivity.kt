package com.example.upaymimoni

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.upaymimoni.di.ApplicationModule
import com.example.upaymimoni.di.expenseModule
import com.example.upaymimoni.presentation.ui.ExpenseDetailScreen
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            UPayMiMoniTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val TEST_EXPENSE_ID = "mock1"

                    NavHost(navController = navController, startDestination = "expense_detail/$TEST_EXPENSE_ID") {
// 1. Expense Detail Screen Composable
                        composable(
                            "expense_detail/{expenseId}",
                            arguments = listOf(navArgument("expenseId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            // Retrieve the expenseId from the navigation arguments
                            val expenseId = backStackEntry.arguments?.getString("expenseId") ?: TEST_EXPENSE_ID

                            ExpenseDetailScreen(
                                expenseId = expenseId,
                                // Since this is the only screen, onBackClick will just close the app (or navigate nowhere)
                                onBackClick = { /* No-op or finish Activity */ }
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UPayMiMoniTheme {
        Greeting("Android")
    }
}

