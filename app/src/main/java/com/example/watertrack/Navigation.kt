package com.example.watertrack

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Serializable
sealed class Destinations {
    @Serializable
    object Home : Destinations()
    @Serializable
    object Stats : Destinations()
}

data class NavigationScreenItem(val route: Destinations, val text: String, val icon: ImageVector)


@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val screens = listOf(
        NavigationScreenItem(Destinations.Home, "Home", Icons.Default.Home),
        NavigationScreenItem(Destinations.Stats, "Stats", Icons.Default.BarChart)
    )

    val pagerState = rememberPagerState(pageCount = { screens.size })
    val coroutineScope = rememberCoroutineScope()

    val currentScreen = screens[pagerState.currentPage]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentScreen.text) },
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.droplet),
                        contentDescription = "App Icon",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                screens.forEachIndexed { index, screenItem ->
                    NavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        icon = { Icon(screenItem.icon, contentDescription = screenItem.text) },
                        label = { Text(screenItem.text) }
                    )
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues)
        ) { pageIndex ->
            when (screens[pageIndex].route) {
                is Destinations.Home -> HomeScreen()
                is Destinations.Stats -> StatsScreen()
            }
        }
    }
}