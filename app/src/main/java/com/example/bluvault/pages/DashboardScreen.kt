package com.example.bluvault.pages

import FamilijenGrotesk
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bluvault.R
import com.example.bluvault.components.AvatarLabel
import com.example.bluvault.components.EWalletItem
import kotlin.String

data class EWalletData(
    val icon: Painter,
    val label: String,
    val backgroundColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavHostController){
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val wallets = listOf(
        EWalletData(painterResource(id = R.drawable.gopey), "HoPay", Color(0xFF27AE60)),
        EWalletData(painterResource(id = R.drawable.urlaja), "URLaja", Color(0xffff2c2c)),
        EWalletData(painterResource(id = R.drawable.sopipey), "SopiPey", Color(0xFFEB7C17)),
        EWalletData(painterResource(id = R.drawable.dono), "Dono", Color(0xFF3EB7DB))
    )

    val transfers = listOf(
        "Sarah", "Dhika", "Filza", "Rafi", "Meng"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.bluvault_dark_icon),
                    contentDescription = "bluVault logo",
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.default_user),
                    contentDescription = "default user",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Balance
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            ){
                Text(
                    text = "Total Balance",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontFamily = FamilijenGrotesk,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Rp 12.000.000,00",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 38.sp,
                    lineHeight = 36.sp,
                    fontFamily = FamilijenGrotesk
                )
            }


            // E-money
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical =16.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "E-money",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )

//                        Icon(
//                            imageVector = Icons.Default.OpenInNew,
//                            contentDescription = "Open",
//                            tint = Color.Gray,
//                            modifier = Modifier.size(20.dp)
//                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 300.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            items(wallets) { wallet ->
                                EWalletItem(
                                    icon = wallet.icon,
                                    label = wallet.label,
                                    backgroundColor = wallet.backgroundColor
                                )
                            }
                        }
                    }
                }
            }

            // Quick transfer
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical =16.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quick transfer",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )

//                        Icon(
//                            imageVector = Icons.Default.OpenInNew,
//                            contentDescription = "Open",
//                            tint = Color.Gray,
//                            modifier = Modifier.size(20.dp)
//                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Column {
                        LazyHorizontalGrid(
                            rows = GridCells.Fixed(1),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 76.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            items(transfers) { transfer ->
                                AvatarLabel(
                                    label = transfer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
