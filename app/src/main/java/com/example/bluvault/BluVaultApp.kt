import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.bluvault.MainScaffold
import com.example.bluvault.R
import com.example.bluvault.pages.DashboardScreen
import com.example.bluvault.pages.LoginScreen
import com.example.bluvault.pages.OTPCodeScreen
import com.example.bluvault.pages.RegisterScreen
import com.example.bluvault.pages.TopUpScreen

val FamilijenGrotesk = FontFamily(
    Font(R.font.familjen_grotesk, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.familjen_grotesk, FontWeight.Normal, FontStyle.Italic)

)

@Composable
fun BluVaultApp() {
    val navController = rememberNavController()

    MainScaffold(navController = navController) {
        NavHost(navController = navController, startDestination = "TopUp") {
            composable("Welcome") { WelcomeScreen(navController) }
            composable("OTPNumber") { OTPNumberScreen(navController) }
            composable("OTPCode") { OTPCodeScreen(navController) }
            composable("Register") { RegisterScreen(navController) }
            composable("Login") { LoginScreen(navController) }

            // ✅ Pages with BottomNav
            composable("Dashboard") { DashboardScreen(navController) }
            composable("TopUp") { TopUpScreen(navController) }
//            composable("Card") { CardScreen(navController) }
//            composable("QRIS") { QRISScreen(navController) }
//            composable("Transaction") { TransactionScreen(navController) }
//            composable("Profile") { ProfileScreen(navController) }
        }
    }
}

