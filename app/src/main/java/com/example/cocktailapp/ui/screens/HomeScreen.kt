package com.example.cocktailapp.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cocktailapp.R
import com.example.cocktailapp.api.CocktailViewModel
import com.example.cocktailapp.model.Cocktail
import com.example.cocktailapp.ui.theme.CocktailAppTheme
import com.example.cocktailapp.ui.theme.backgroundColorsBrush


enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int,
    val route: String
) {
    DRINKS(R.string.drinks, Icons.Outlined.LocalBar, R.string.drinks, "drinks_route"),
    TIMER(R.string.timer, Icons.Outlined.Timer, R.string.timer, "timer_route"),
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppScreen() {
    val viewModel: CocktailViewModel = viewModel()
    val cocktails: List<Cocktail> = viewModel.cocktailList.value

    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Cocktail>()
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()

    val timerViewModel: TimerViewModel = viewModel()

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = stringResource(it.contentDescription)
                        )
                    },
                    label = { Text(stringResource(it.label)) },
                    selected = currentDestination?.destination?.route == it.route,
                    onClick = {
                        navController.navigate(it.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },

    ) {
        NavHost(navController = navController, startDestination = AppDestinations.DRINKS.route) {
            composable(AppDestinations.DRINKS.route) { CocktailListDetail(
                navigator = scaffoldNavigator,
                scope = scope,
                cocktails = cocktails,
                cocktailViewModel = viewModel
            )}

            composable(AppDestinations.TIMER.route) { TimerScreenContent(timerViewModel)}
        }
    }
}







@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GreetingPreview() {

    CocktailAppTheme {
        AppScreen()
    }
}