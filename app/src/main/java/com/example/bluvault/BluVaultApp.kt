import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bluvault.MainScaffold
import com.example.bluvault.R
import com.example.bluvault.operations.CardData
import com.example.bluvault.pages.CreateCardPage
import com.example.bluvault.pages.DashboardScreen
import com.example.bluvault.pages.LoginScreen
import com.example.bluvault.pages.OTPCodeScreen
import com.example.bluvault.pages.RegisterScreen
import com.example.bluvault.pages.TopUpPage
import com.example.bluvault.pages.TopUpScreen
import com.example.bluvault.pages.TransferPage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.bluvault.pages.DeleteCardPage
import com.example.bluvault.pages.DepositPage
import com.example.bluvault.pages.QRISScreen
import com.example.bluvault.pages.TransactionPage

val FamilijenGrotesk = FontFamily(
    Font(R.font.familjen_grotesk, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.familjen_grotesk, FontWeight.Normal, FontStyle.Italic)

)

@Composable
fun BluVaultApp() {
    val navController = rememberNavController()

    MainScaffold(navController = navController) {
        NavHost(navController = navController, startDestination = "Welcome") {
            composable("Welcome") { WelcomeScreen(navController) }
            composable("OTPNumber") { OTPNumberScreen(navController) }
            composable("OTPCode") { OTPCodeScreen(navController) }
            composable("Register") { RegisterScreen(navController) }
            composable("Login") { LoginScreen(navController) }

            // ✅ Pages with BottomNav
            composable("Dashboard") { DashboardScreen(navController) }
            composable("Card") { CardCollectionScreen(navController) }
            composable("CreateCard") { CreateCardPage(navController) }
            composable("deposit") { DepositPage(navController) }
            composable(
                route = "transfer?receiverUid={receiverUid}",
                arguments = listOf(
                    navArgument("receiverUid") {
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val activeCard = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<CardData>("activeCard")

                val receiverUid = backStackEntry.arguments?.getString("receiverUid")

                TransferPage(
                    navController = navController,
                    activeCard = activeCard,
                    receiverUid = receiverUid
                )
            }
            composable(
                route = "topup",
            ) { backStackEntry ->
                val activeCard = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<CardData>("activeCard")

                TopUpPage(
                    navController = navController,
                    activeCard = activeCard,
                )
            }
            composable("deleteCard") { backStackEntry ->
                val activeCard = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<CardData>("activeCard")

                if (activeCard != null) {
                    DeleteCardPage(navController = navController, activeCard = activeCard)
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
            composable("QRIS") { QRISScreen(navController) }
            composable("Transaction") { TransactionPage(navController) }
//            composable("Profile") { ProfileScreen(navController) }
        }
    }
}

