import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bluvault.R

@Composable
fun WelcomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030B1C)) // Dark blue background
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp), // Adjust to push content below status bar
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.bluvalut_icon),
                contentDescription = "bluVault logo",
                modifier = Modifier
                    .width(120.dp)
                    .height(40.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "A Modern Way of\nFinancing",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 36.sp,
                fontFamily = FamilijenGrotesk
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create your bluVault account today and\nrevolutionize your banking experience.",
                color = Color(0xFFB0B9C7),
                fontSize = 14.sp,
                fontFamily = FamilijenGrotesk
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { navController.navigate("Register") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Create Free Account",
                    color = Color(0xFF030B1C),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FamilijenGrotesk,
                    fontSize = 16.sp,
                )
            }
        }
    }
}
