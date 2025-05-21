package com.example.watertrack

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val items = listOf(
        NavigationItem(Destinations.Home, "Home", Icons.Default.Home),
        NavigationItem(Destinations.Stats, "Stats", Icons.Default.BarChart)
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, items = items)
        },
        topBar = {
            TopAppBar(
                title = { Text("WaterTrack") },
                modifier = Modifier,
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.droplet),
                        contentDescription = "",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        modifier = modifier
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destinations.Home,
            modifier = Modifier.padding(padding)
        ) {
            composable<Destinations.Home> {
                HomeScreen()
            }
            composable<Destinations.Stats> {
                StatsScreen()
            }
        }
    }
}

@Composable
fun BottomNavBar(
    navController: NavHostController,
    items: List<NavigationItem>
) {
    val backStackEntry = navController
        .currentBackStackEntryAsState()

    val currentRoute = when (backStackEntry.value?.destination?.route) {
        Destinations.Home::class.qualifiedName -> Destinations.Home
        Destinations.Stats::class.qualifiedName -> Destinations.Stats
        else -> Destinations.Home
    }

    NavigationBar {
        items.forEach { item ->
            val route = item.route
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(item.route)
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.text) },
                label = { Text(item.text) }
            )
        }
    }
}


data class NavigationItem(val route: Destinations, val text: String, val icon: ImageVector)

@Serializable
sealed class Destinations {
    @Serializable
    object Home : Destinations()

    @Serializable
    object Stats : Destinations()

}
