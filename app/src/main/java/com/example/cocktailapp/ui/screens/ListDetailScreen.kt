package com.example.cocktailapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.pillStar
import androidx.graphics.shapes.toPath
import androidx.window.core.layout.WindowWidthSizeClass
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cocktailapp.R
import com.example.cocktailapp.api.CocktailViewModel
import com.example.cocktailapp.model.Cocktail
import com.example.cocktailapp.ui.theme.backgroundColorsBrush
import com.example.cocktailapp.ui.theme.RotatingScallopedProfilePic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.max


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CocktailListDetail(
    navigator: ThreePaneScaffoldNavigator<Cocktail>,
    scope: CoroutineScope,
    modifier: Modifier = Modifier,
    cocktails: List<Cocktail>,
    cocktailViewModel: CocktailViewModel
) {

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            CocktailList(
                cocktails = cocktails,
                onItemClick = { item ->
                    if (item.instructions == null) {
                        cocktailViewModel.fetchCocktailDetail(item.id)
                    }


                    scope.launch {
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail,
                            item
                        )
                    }
                },
            )
        },
        detailPane = {
            navigator.currentDestination?.contentKey?.let {

                CocktailDetail(it, navigator, scope)
            }
        },
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TopBar(title: String, navigator: ThreePaneScaffoldNavigator<Cocktail>, scope: CoroutineScope) {
    TopAppBar(
        windowInsets = WindowInsets.statusBars,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        title = {
            Text(title)
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
                    contentDescription = "Go back"
                )
            }
        },

        )
}


@Composable
fun CocktailList(cocktails: List<Cocktail>, onItemClick: (Cocktail) -> Unit) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {

        items(cocktails) { cocktail ->
            OutlinedCard(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onItemClick(cocktail)
                    }
                    .border(
                        BorderStroke(4.dp, backgroundColorsBrush),
                        MaterialTheme.shapes.medium
                    )
            ) {
                CocktailListCardContent(cocktail)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetail(
    cocktail: Cocktail,
    navigator: ThreePaneScaffoldNavigator<Cocktail>,
    scope: CoroutineScope
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    Scaffold(
        topBar = {
            if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.EXPANDED) {
                TopBar(cocktail.name, navigator, scope)
            }
        }

    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {

            if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.EXPANDED) {
//                CocktailImage(cocktail)
                RotatingScallopedProfilePic(cocktail)
            } else {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize().padding(end = 20.dp)
                ) {
                    Text(
                        cocktail.name,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(end = 20.dp)
                    )

                    RotatingScallopedProfilePic(cocktail, modifier = Modifier.size(300.dp))

                }
            }

            Spacer(Modifier.size(16.dp))

            DetailsWindow(label = "Instructions") {
                Text(cocktail.instructions.toString())
            }


        }
    }

}

@Composable
fun DetailsWindow(label: String, content: @Composable () -> Unit) {
    OutlinedCard {
        Text(label)

        Spacer(Modifier.size(16.dp))

        content
    }
}

@Composable
fun CocktailListCardContent(cocktail: Cocktail, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        CocktailImage(
            cocktail,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .clip(RoundedCornerShape(topEnd = 6.dp, topStart = 6.dp)))

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
    )
}

