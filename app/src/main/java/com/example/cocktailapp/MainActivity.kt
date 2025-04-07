package com.example.cocktailapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cocktailapp.ui.theme.CocktailAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocktailAppTheme {
                AppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppScreen() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Cocktail>()
    val scope = rememberCoroutineScope()

    Scaffold (
        topBar = {
            TopBar(
                navigator = navigator,
                scope = scope,
            )
        }
    ) { innerPadding ->
        CocktailListDetail(
            navigator = navigator,
            scope = scope,
            modifier = Modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TopBar(navigator: ThreePaneScaffoldNavigator<Cocktail>, scope: CoroutineScope) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = {
            Text("Cocktails")
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    if (navigator.canNavigateBack()) {
                        navigator.navigateBack()
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CocktailListDetail(navigator: ThreePaneScaffoldNavigator<Cocktail>, scope: CoroutineScope, modifier: Modifier) {
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                CocktailList(
                    onItemClick = { item ->
                        scope.launch {
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                item
                            )
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.contentKey?.let {
                    CocktailDetail(it)
                }
            }
        },
        modifier = modifier
            .background(backgroundColorsBrush)
    )
}


@Composable
fun CocktailList(onItemClick: (Cocktail) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        cocktails.forEach {cocktail ->
            item {
                OutlinedCard (
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onItemClick(cocktail)
                        }
                        .border(
                            BorderStroke(4.dp, rainbowColorsBrush),
                            MaterialTheme.shapes.medium
                        )
                ) {
                    CocktailListCardContent(cocktail)
                }
            }
        }
    }
}

@Composable
fun CocktailDetail(cocktail: Cocktail) {
    val text = cocktail.name
    Card {
        Column (
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Text(
                text = "Details page for $text",
                fontSize = 24.sp
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = "TODO: Add great details here"
            )
        }
    }
}

@Composable
fun CocktailListCardContent(cocktail: Cocktail, modifier: Modifier = Modifier) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cocktail.imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.cocktail_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .padding(4.dp)
                .clip(RoundedCornerShape(topEnd = 6.dp, topStart = 6.dp))
        )
        Text(
            text = cocktail.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)

        )
    }
}

@Parcelize
class Cocktail(
    val name: String,
    val imageUrl: String
) : Parcelable

val cocktails = listOf(
    Cocktail("Kioki Coffee", "https://www.thecocktaildb.com/images/media/drink/uppqty1441247374.jpg"),
    Cocktail("Pink Moon", "https://www.thecocktaildb.com/images/media/drink/lnjoc81619696191.jpg"),
    Cocktail("Orange Scented Hot Chocolate", "https://www.thecocktaildb.com/images/media/drink/hdzwrh1487603131.jpg"),
    Cocktail("Chocolate Monkey", "https://www.thecocktaildb.com/images/media/drink/tyvpxt1468875212.jpg"),
    Cocktail("Almond Chocolate Coffee", "https://www.thecocktaildb.com/images/media/drink/jls02c1493069441.jpg"),
    Cocktail("Chocolate Milk", "https://www.thecocktaildb.com/images/media/drink/j6q35t1504889399.jpg"),
    Cocktail("Banana Strawberry Shake", "https://www.thecocktaildb.com/images/media/drink/vqquwx1472720634.jpg"),
    Cocktail("Just a Moonmint", "https://www.thecocktaildb.com/images/media/drink/znald61487604035.jpg"),
    Cocktail("Black Forest Shake", "https://www.thecocktaildb.com/images/media/drink/xxtxsu1472720505.jpg")
)

val rainbowColorsBrush = Brush.sweepGradient(
    listOf(
        Color(0xFF9575CD),
        Color(0xFFBA68C8),
        Color(0xFFE57373),
        Color(0xFFFFB74D),
        Color(0xFFFFF176),
        Color(0xFFAED581),
        Color(0xFF4DD0E1),
        Color(0xFF9575CD)
    )
)

val backgroundColorsBrush = Brush.verticalGradient(
    listOf(
        Color(0xFF7BD5F5),
        Color(0xFF787FF6)
    )
)

@Preview(
    showBackground = true,
    showSystemUi = true)
@Composable
fun GreetingPreview() {
    CocktailAppTheme {
        AppScreen()
    }
}