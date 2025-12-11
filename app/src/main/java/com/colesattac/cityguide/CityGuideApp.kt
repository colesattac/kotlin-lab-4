package com.colesattac.cityguide

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.colesattac.cityguide.ui.CategoriesScreen
import com.colesattac.cityguide.ui.PlaceDetailScreen
import com.colesattac.cityguide.ui.PlacesScreen
import com.colesattac.cityguide.ui.PlacesViewModel

enum class CityScreen(val title: Int) {
    Categories(title = R.string.app_name),
    Places(title = R.string.places_list),
    Details(title = R.string.place_details),
    About(title = R.string.about_app)
}

// 2. Создаем Composable для экрана "О приложении"
@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Практическая работа #4",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Магистрант группы МИС-22н\nАгитаев И.А.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityGuideAppBar(
    currentScreen: CityScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityGuideApp(
    viewModel: PlacesViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val currentScreen = when {
        currentRoute == CityScreen.About.name -> CityScreen.About
        currentRoute?.startsWith(CityScreen.Details.name) == true -> CityScreen.Details
        currentRoute?.startsWith(CityScreen.Places.name) == true -> CityScreen.Places
        else -> CityScreen.Categories
    }

    Scaffold(
        topBar = {
            CityGuideAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = CityScreen.Categories.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = CityScreen.Categories.name) {
                CategoriesScreen(
                    categories = uiState.categories,
                    onCategoryClick = { category ->
                        // 3. Обновляем логику навигации
                        if (category == "О приложении") {
                            navController.navigate(CityScreen.About.name)
                        } else {
                            viewModel.updateCurrentCategory(category)
                            navController.navigate("${CityScreen.Places.name}/$category")
                        }
                    }
                )
            }

            composable(route = "${CityScreen.Places.name}/{category}") {
                PlacesScreen(
                    places = uiState.currentCategoryPlaces,
                    onPlaceClick = { placeId ->
                        viewModel.updateCurrentPlace(placeId)
                        navController.navigate("${CityScreen.Details.name}/$placeId")
                    }
                )
            }

            composable(route = "${CityScreen.Details.name}/{placeId}") {
                PlaceDetailScreen(place = uiState.currentSelectedPlace)
            }

            // Добавляем новый маршрут для экрана "О приложении"
            composable(route = CityScreen.About.name) {
                AboutScreen()
            }
        }
    }
}
