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
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cocktailapp.R
import com.example.cocktailapp.api.CocktailViewModel
import com.example.cocktailapp.model.Cocktail
import com.example.cocktailapp.ui.theme.CocktailAppTheme


enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    DRINKS(R.string.drinks, Icons.Outlined.LocalBar, R.string.drinks),
    TIMER(R.string.timer, Icons.Outlined.Timer, R.string.timer),
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppScreen(viewModel: CocktailViewModel = viewModel(), cocktails: List<Cocktail> = viewModel.cocktailList.value) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Cocktail>()
    val scope = rememberCoroutineScope()
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.DRINKS) }
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
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        // Destination content.
        when (currentDestination) {
            AppDestinations.DRINKS -> CocktailListDetail(
                navigator = scaffoldNavigator,
                scope = scope,
                cocktails = cocktails,
                cocktailViewModel = viewModel
            )
            AppDestinations.TIMER -> TimerScreenContent(timerViewModel)
        }
    }
}



@Composable
fun CocktailImage(cocktail: Cocktail, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(cocktail.imageUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.cocktail_placeholder),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(RoundedCornerShape(topEnd = 6.dp, topStart = 6.dp))
    )
}




@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GreetingPreview() {
    val cocktails = listOf(
    Cocktail("1", "Kioki Coffee", "https://www.thecocktaildb.com/images/media/drink/uppqty1441247374.jpg"),
    Cocktail("1", "Pink Moon", "https://www.thecocktaildb.com/images/media/drink/lnjoc81619696191.jpg"),
    Cocktail("1", "Orange Scented Hot Chocolate", "https://www.thecocktaildb.com/images/media/drink/hdzwrh1487603131.jpg"),
    Cocktail("1", "Chocolate Monkey", "https://www.thecocktaildb.com/images/media/drink/tyvpxt1468875212.jpg"),
    Cocktail("1", "Almond Chocolate Coffee", "https://www.thecocktaildb.com/images/media/drink/jls02c1493069441.jpg"),
    Cocktail("1", "Chocolate Milk", "https://www.thecocktaildb.com/images/media/drink/j6q35t1504889399.jpg"),
    Cocktail("1", "Banana Strawberry Shake", "https://www.thecocktaildb.com/images/media/drink/vqquwx1472720634.jpg"),
    Cocktail("1", "Just a Moonmint", "https://www.thecocktaildb.com/images/media/drink/znald61487604035.jpg"),
    Cocktail("1", "Black Forest Shake", "https://www.thecocktaildb.com/images/media/drink/xxtxsu1472720505.jpg")
)

    CocktailAppTheme {
        AppScreen(cocktails = cocktails)
    }
}