import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.bluvault.R
import com.example.bluvault.pages.DashboardScreen
import com.example.bluvault.pages.LoginScreen
import com.example.bluvault.pages.OTPCodeScreen
import com.example.bluvault.pages.RegisterScreen

val FamilijenGrotesk = FontFamily(
    Font(R.font.familjen_grotesk, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.familjen_grotesk, FontWeight.Normal, FontStyle.Italic)

)

@Composable
fun BluVaultApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Dashboard") {
        composable("Welcome") {
            WelcomeScreen(navController)
        }
        composable("OTPNumber") {
            OTPNumberScreen(navController)
        }
        composable("OTPCode") {
            OTPCodeScreen(navController)
        }
        composable("Register") {
            RegisterScreen(navController)
        }
        composable("Login"){
            LoginScreen(navController)
        }
        composable("Dashboard"){
            DashboardScreen(navController)
        }
    }
}
