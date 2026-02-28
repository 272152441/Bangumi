package com.zxwork.bmg.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.zxwork.bmg.data.model.CalendarModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreenContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            uiState.error != null -> {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                CalendarList(
                    calendars = uiState.calendars,
                    onItemClick = { onIntent(HomeIntent.OnItemClick(it)) }
                )
            }
        }
    }
}

@Composable
fun CalendarList(
    calendars: List<CalendarModel>,
    onItemClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(calendars) { calendar ->
            CalendarSection(calendar = calendar, onItemClick = onItemClick)
        }
    }
}

@Composable
fun CalendarSection(
    calendar: CalendarModel,
    onItemClick: (Int) -> Unit
) {
    Column {
        Text(
            text = calendar.weekday.cn,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(calendar.items) { item ->
                CalendarItemCard(item = item, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun CalendarItemCard(
    item: CalendarModel.Item,
    onItemClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable { onItemClick(item.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = item.images.common,
                contentDescription = item.nameCn.ifEmpty { item.name },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = item.nameCn.ifEmpty { item.name },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "评分: ${item.rating.score}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val mockItems = List(3) { i ->
        CalendarModel.Item(
            id = i,
            name = "Anime $i",
            nameCn = "动漫 $i",
            airDate = "2024-01-01",
            airWeekday = 1,
            images = CalendarModel.Item.Images(
                common = "", grid = "", large = "", medium = "", small = ""
            ),
            rating = CalendarModel.Item.Rating(
                score = 8.5, total = 100,
                count = CalendarModel.Item.Rating.Count(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            ),
            collection = CalendarModel.Item.Collection(doing = 100),
            rank = 1,
            summary = "",
            type = 2,
            url = ""
        )
    }
    val mockCalendars = listOf(
        CalendarModel(
            weekday = CalendarModel.Weekday(cn = "星期一", en = "Mon", id = 1, ja = "月"),
            items = mockItems
        ),
        CalendarModel(
            weekday = CalendarModel.Weekday(cn = "星期二", en = "Tue", id = 2, ja = "火"),
            items = mockItems
        )
    )

    HomeScreenContent(
        uiState = HomeState(calendars = mockCalendars),
        onIntent = {}
    )
}
