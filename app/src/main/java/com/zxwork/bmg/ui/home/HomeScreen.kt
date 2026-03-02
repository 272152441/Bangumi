@file:OptIn(ExperimentalMaterial3Api::class)

package com.zxwork.bmg.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.zxwork.bmg.data.model.CalendarModel
import com.zxwork.bmg.ui.theme.BmgTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
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
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "每日放送",
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
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
}

@Composable
fun CalendarList(
    calendars: List<CalendarModel>,
    onItemClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
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
        Row(
            modifier = Modifier
                .padding(start = 16.dp,end = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(4.dp, 18.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = calendar.weekday.cn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
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
    val cornerShape = RoundedCornerShape(8.dp)
    
    Card(
        modifier = Modifier
            .width(130.dp)
            .clip(cornerShape)
            .clickable { onItemClick(item.id) }
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), cornerShape),
        shape = cornerShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = item.images?.common?.replace("http://", "https://"),
                    contentDescription = item.nameCn.ifEmpty { item.name },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)),
                    contentScale = ContentScale.Crop
                )
                // 评分小标签
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = item.rating?.score?.toString() ?: "0.0",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = item.nameCn.ifEmpty { item.name },
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (item.rank != null) {
                    Text(
                        text = "Rank #${item.rank}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BmgTheme {
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
                    count = CalendarModel.Item.Rating.Count(0, 10, 2, 3, 4, 5, 6, 7, 8, 9)
                ),
                collection = CalendarModel.Item.Collection(doing = 100),
                rank = i + 1,
                summary = "",
                type = 2,
                url = ""
            )
        }
        val mockCalendars = listOf(
            CalendarModel(
                weekday = CalendarModel.Weekday(cn = "星期一", en = "Mon", id = 1, ja = "月"),
                items = mockItems
            )
        )

        HomeScreenContent(
            uiState = HomeState(calendars = mockCalendars),
            onIntent = {}
        )
    }
}
