package com.colesattac.cityguide.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colesattac.cityguide.R
import com.colesattac.cityguide.data.Place

// Экран 1: Список категорий
@Composable
fun CategoriesScreen(
    categories: List<String>,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(categories) { category ->
            CategoryCard(
                categoryName = category,
                onCardClick = { onCategoryClick(category) }
            )
        }
    }
}

// Карточка для категории
@Composable
fun CategoryCard(categoryName: String, onCardClick: () -> Unit) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onCardClick),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        androidx.compose.material3.Text(
            text = categoryName,
            modifier = Modifier.padding(16.dp),
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge
        )
    }
}

// Экран 2: Список мест в категории
@Composable
fun PlacesScreen(
    places: List<Place>,
    onPlaceClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(places) { place ->
            PlaceCard(
                place = place,
                onCardClick = { onPlaceClick(place.id) }
            )
        }
    }
}

// Карточка для места в списке
@Composable
fun PlaceCard(place: Place, onCardClick: () -> Unit) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onCardClick),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        androidx.compose.foundation.layout.Column(modifier = Modifier.padding(16.dp)) {
            androidx.compose.material3.Text(
                text = place.name,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material3.Text(
                text = place.address,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                color = androidx.compose.material3.MaterialTheme.colorScheme.secondary
            )
        }
    }
}


// Экран 3: Детальная информация о месте
@Composable
fun PlaceDetailScreen(
    place: Place?,
    modifier: Modifier = Modifier
) {
    if (place == null) {
        // Показываем сообщение, если данные не найдены
        androidx.compose.foundation.layout.Box(modifier = modifier.fillMaxSize()) {
            androidx.compose.material3.Text(stringResource(R.string.place_not_found), style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
        }
        return
    }

    androidx.compose.foundation.layout.Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        androidx.compose.material3.Text(
            text = place.name,
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
        androidx.compose.material3.Text(
            text = "Категория: ${place.category}",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
            color = androidx.compose.material3.MaterialTheme.colorScheme.primary
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
        androidx.compose.material3.Text(
            text = "Адрес: ${place.address}",
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Divider()
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = place.description,
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp
        )
    }
}
