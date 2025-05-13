package com.example.cocktailapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cocktailapp.api.CocktailViewModel
import com.example.cocktailapp.api.RetrofitClient
import com.example.cocktailapp.model.Cocktail
import com.example.cocktailapp.ui.theme.backgroundColorsBrush
import com.example.cocktailapp.ui.theme.rainbowColorsBrush
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CocktailListDetail(navigator: ThreePaneScaffoldNavigator<Cocktail>,
                       scope: CoroutineScope,
                       modifier: Modifier = Modifier,
                       cocktails: List<Cocktail>,
                       cocktailViewModel: CocktailViewModel) {

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
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
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.contentKey?.let {


                    CocktailDetail(it, navigator, scope)
                }
            }
        },
        modifier = modifier
            .background(backgroundColorsBrush)
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TopBar(navigator: ThreePaneScaffoldNavigator<Cocktail>, scope: CoroutineScope) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
            titleContentColor = MaterialTheme.colorScheme.onBackground,
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
                    contentDescription = "Go back"
                )
            }
        },

        )
}


@Composable
fun CocktailList(cocktails: List<Cocktail>, onItemClick: (Cocktail) -> Unit) {

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {

        items(cocktails) { cocktail ->
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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CocktailDetail(cocktail: Cocktail, navigator: ThreePaneScaffoldNavigator<Cocktail>, scope: CoroutineScope) {
    Scaffold (
        topBar = {
            TopBar(navigator, scope)
        }
    ) { innerPadding -> Column (
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            CocktailImage(cocktail)
            Spacer(Modifier.size(16.dp))

            Text(
                text = cocktail.name,
                fontSize = 24.sp,
            )


        cocktail.instructions?.let { Text(text = it) }

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
        CocktailImage(cocktail, modifier = Modifier.fillMaxSize())

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
