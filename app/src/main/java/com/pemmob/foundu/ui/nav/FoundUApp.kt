package com.pemmob.foundu.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pemmob.foundu.ui.auth.LoginScreen
import com.pemmob.foundu.ui.auth.AdminLoginScreen
import androidx.compose.ui.platform.LocalContext
import com.pemmob.foundu.ui.category.CategoryListScreen
import com.pemmob.foundu.ui.item.ItemDetailScreen
import com.pemmob.foundu.ui.item.ItemListScreen
import com.pemmob.foundu.ui.pickuppoint.PickupPointListScreen
import com.pemmob.foundu.ui.theme.*
import kotlinx.serialization.Serializable

@Serializable
object MainRoute

@Composable
fun FoundUApp(rootNavController: NavHostController = rememberNavController()) {
    NavHost(
        navController = rootNavController,
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreen(
                onLoginClick = {
                    rootNavController.navigate(MainRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    rootNavController.navigate(RegisterRoute)
                },
                onAdminLoginClick = {
                    rootNavController.navigate(AdminLoginRoute)
                }
            )
        }

        composable<AdminLoginRoute> {
            AdminLoginScreen(
                onLoginClick = {
                    rootNavController.navigate(MainRoute) {
                        popUpTo(AdminLoginRoute) { inclusive = true }
                    }
                },
                onBackClick = {
                    rootNavController.popBackStack()
                }
            )
        }

        composable<RegisterRoute> {
            val context = LocalContext.current
            com.pemmob.foundu.ui.auth.RegisterScreen(
                onRegisterClick = { name, email, password ->
                    val success = com.pemmob.foundu.data.local.UserSession.registerMahasiswa(
                        context,
                        name,
                        email,
                        password
                    )

                    if (success) {
                        rootNavController.navigate(MainRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        android.widget.Toast.makeText(
                            context,
                            "Email sudah terdaftar",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onLoginClick = {
                    rootNavController.popBackStack()
                }
            )
        }

        composable<MainRoute> {
            MainScreen(
                onLogout = {
                    rootNavController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val currentDestination by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    )
                    .background(
                        Brush.linearGradient(
                            listOf(
                                NavbarStart,
                                NavbarMid,
                                NavbarEnd
                            )
                        )
                    ),
                tonalElevation = 0.dp
            ) {

                NavigationBarItem(
                    selected = currentDestination
                        ?.destination
                        ?.hasRoute<ItemListRoute>() == true,
                    onClick = {
                        navController.navigateToTab(ItemListRoute)
                    },
                    icon = {
                        Icon(
                            Icons.Filled.List,
                            "Barang"
                        )
                    },
                    label = {
                        Text(
                            "Barang",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = FoundUPrimary.copy(alpha = 0.3f),
                        unselectedIconColor = Color.White.copy(alpha = 0.5f),
                        unselectedTextColor = Color.White.copy(alpha = 0.5f)
                    )
                )

                if (com.pemmob.foundu.data.local.UserSession.role == "admin") {

                    NavigationBarItem(
                        selected = currentDestination
                            ?.destination
                            ?.hasRoute<CategoryListRoute>() == true,
                        onClick = {
                            navController.navigateToTab(CategoryListRoute)
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Info,
                                "Kategori"
                            )
                        },
                        label = {
                            Text(
                                "Kategori",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            indicatorColor = FoundUPrimary.copy(alpha = 0.3f),
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            unselectedTextColor = Color.White.copy(alpha = 0.5f)
                        )
                    )

                    NavigationBarItem(
                        selected = currentDestination
                            ?.destination
                            ?.hasRoute<PickupPointListRoute>() == true,
                        onClick = {
                            navController.navigateToTab(PickupPointListRoute)
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Place,
                                "Titik Ambil"
                            )
                        },
                        label = {
                            Text(
                                "Titik Ambil",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            indicatorColor = FoundUPrimary.copy(alpha = 0.3f),
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            unselectedTextColor = Color.White.copy(alpha = 0.5f)
                        )
                    )
                }

                NavigationBarItem(
                    selected = currentDestination
                        ?.destination
                        ?.hasRoute<ProfileRoute>() == true,
                    onClick = {
                        navController.navigateToTab(ProfileRoute)
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Person,
                            "Profil"
                        )
                    },
                    label = {
                        Text(
                            "Profil",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = FoundUPrimary.copy(alpha = 0.3f),
                        unselectedIconColor = Color.White.copy(alpha = 0.5f),
                        unselectedTextColor = Color.White.copy(alpha = 0.5f)
                    )
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = ItemListRoute,
            modifier = Modifier.padding(padding)
        ) {

            composable<ItemListRoute> {
                ItemListScreen(
                    onItemClick = { item ->
                        navController.navigate(
                            ItemDetailRoute(itemId = item.id)
                        )
                    }
                )
            }

            composable<CategoryListRoute> {
                CategoryListScreen()
            }

            composable<PickupPointListRoute> {
                PickupPointListScreen()
            }

            composable<ProfileRoute> {
                com.pemmob.foundu.ui.profile.ProfileScreen(
                    onLogoutClick = {
                        com.pemmob.foundu.data.local.UserSession.logout()
                        onLogout()
                    }
                )
            }

            composable<ItemDetailRoute> { backStackEntry ->
                val route: ItemDetailRoute =
                    backStackEntry.toRoute()

                ItemDetailScreen(
                    itemId = route.itemId,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

private fun NavHostController.navigateToTab(route: Any) {
    navigate(route) {
        popUpTo(
            graph.findStartDestination().id
        ) {
            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }
}