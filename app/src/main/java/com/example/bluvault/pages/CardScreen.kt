import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluvault.components.ActionButtons
import com.example.bluvault.components.CardCarousel
import com.example.bluvault.components.Header
import com.example.bluvault.components.TransactionList
import com.example.bluvault.operations.CardData
import com.example.bluvault.operations.TransactionData
import com.example.bluvault.operations.fetchTransactions
import com.example.bluvault.operations.fetchUserCards

@Composable
fun CardCollectionScreen(navController: NavHostController) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseFirestore.getInstance()

    var cards by remember { mutableStateOf<List<CardData>>(emptyList()) }
    var selectedCardIndex by remember { mutableStateOf(0) }
    var transactions by remember { mutableStateOf<List<TransactionData>>(emptyList()) }

    val context = LocalContext.current

    // Fetch cards when the screen loads
    LaunchedEffect(uid) {
        uid?.let {
            fetchUserCards(uid = it, db = db) { cardList ->
                cards = cardList
            }
        }
    }

    // Fetch transactions once cards are loaded or when selected index changes
    LaunchedEffect(cards, selectedCardIndex) {
        if (cards.isNotEmpty()) {
            uid?.let {
                fetchTransactions(uid, cards[selectedCardIndex].id, db) {
                    transactions = it
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Header(navController)
        Spacer(modifier = Modifier.height(16.dp))

        if (cards.isNotEmpty()) {
            // 🔁 Normal flow
            CardCarousel(
                cards = cards,
                selectedIndex = selectedCardIndex,
                onCardChange = { index -> selectedCardIndex = index }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ActionButtons(
                onTransferClick = {
                    val selectedCard = cards[selectedCardIndex]
                    navController.currentBackStackEntry?.savedStateHandle?.set("activeCard", selectedCard)
                    navController.navigate("transfer?receiverUid=null")
                },
                onTopUpClick = {
                    val selectedCard = cards[selectedCardIndex]
                    navController.currentBackStackEntry?.savedStateHandle?.set("activeCard", selectedCard)
                    navController.navigate("topup")
                },
                onDeleteClick = {
                    val selectedCard = cards[selectedCardIndex]
                    navController.currentBackStackEntry?.savedStateHandle?.set("activeCard", selectedCard)
                    navController.navigate("deleteCard")
                },
                onDepositClick = {
                    navController.navigate("deposit")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransactionList(transactions)
        } else {
            // ❌ Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 64.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "No cards available",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button (
                        onClick = {
                            navController.navigate("CreateCard")
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Create a Card")
                    }
                }
            }
        }
    }
}

