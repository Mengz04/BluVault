package com.example.bluvault

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bluvault.components.BottomNavBar

data class BottomNavItem(
    val route: String,
    val icon: Int,
    val label: String
)

@Composable
fun MainScaffold(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val bottomNavItems = listOf(
        BottomNavItem("Dashboard", R.drawable.home, "Home"),
        BottomNavItem("Card", R.drawable.card, "Card"),
        BottomNavItem("QRIS", R.drawable.qr_icon, "QRIS"),
        BottomNavItem("Transaction", R.drawable.frame, "Transaction"),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = bottomNavItems.map { it.route }
    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    items = bottomNavItems,
                    currentRoute = currentRoute,
                    onItemSelected = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) {
        content()
    }
}
