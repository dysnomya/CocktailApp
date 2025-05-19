package com.example.cocktailapp.ui.screens

import androidx.annotation.StringRes
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cocktailapp.R
import com.example.cocktailapp.api.AlcoholicViewModel
import com.example.cocktailapp.api.NonAlcoholicViewModel
import com.example.cocktailapp.model.Cocktail
import com.example.cocktailapp.ui.theme.CocktailAppTheme


enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int,
    val route: String
) {
    DRINKS(R.string.drinks, Icons.Outlined.LocalBar, R.string.drinks, "drinks_route"),
    NON_ALCOHOLIC(R.string.non_alcoholic, Icons.Outlined.LocalCafe, R.string.non_alcoholic, "non_alcoholic_route"),
    TIMER(R.string.timer, Icons.Outlined.Timer, R.string.timer, "timer_route"),
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppScreen() {
    val alcoholicViewModel: AlcoholicViewModel = viewModel()
    val alcoholic: List<Cocktail> = alcoholicViewModel.cocktailList.value

    val nonAlcoholicViewModel: NonAlcoholicViewModel = viewModel()
    val nonAlcoholic: List<Cocktail> = nonAlcoholicViewModel.cocktailList.value

    val drinksNavigator = rememberListDetailPaneScaffoldNavigator<Cocktail>()
    val nonAlcoholicNavigator = rememberListDetailPaneScaffoldNavigator<Cocktail>()
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
        NavHost(
            navController = navController,
            startDestination = AppDestinations.DRINKS.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None},
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },


        ) {
            composable(AppDestinations.DRINKS.route) { CocktailListDetail(
                navigator = drinksNavigator,
                scope = scope,
                cocktails = alcoholic,
                cocktailViewModel = alcoholicViewModel
            )}

            composable(AppDestinations.NON_ALCOHOLIC.route) { CocktailListDetail(
                navigator = nonAlcoholicNavigator,
                scope = scope,
                cocktails = nonAlcoholic,
                cocktailViewModel = nonAlcoholicViewModel
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