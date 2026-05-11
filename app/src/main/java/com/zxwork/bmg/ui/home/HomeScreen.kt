@file:OptIn(ExperimentalMaterial3Api::class)

package com.zxwork.bmg.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.zxwork.bmg.domain.model.Calendar
import com.zxwork.bmg.domain.model.Subject
import com.zxwork.bmg.domain.model.SubjectType
import com.zxwork.bmg.ui.theme.BackgroundLight
import com.zxwork.bmg.ui.theme.BmgPink
import com.zxwork.bmg.ui.theme.BmgTheme
import com.zxwork.bmg.ui.theme.BmgYellow
import java.util.Locale

@Composable
fun HomeScreen(
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreenContent(
        uiState = uiState,
        onIntent = { intent ->
            if (intent is HomeIntent.OnItemClick) {
                onNavigateToDetail(intent.itemId)
            } else {
                viewModel.onIntent(intent)
            }
        },
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
        containerColor = BackgroundLight
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = BmgPink,
                        strokeWidth = 3.dp
                    )
                }

                uiState.error != null -> {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        item {
                            ModernHeader(title = "每日放送", subtitle = "Today's Schedule")
                        }

                        items(uiState.calendars) { calendar ->
                            CalendarSection(
                                calendar = calendar,
                                onItemClick = { onIntent(HomeIntent.OnItemClick(it)) })
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(uiState.subjectSections) { section ->
                            if (section.items.isNotEmpty()) {
                                SubjectSection(
                                    section = section,
                                    onItemClick = { onIntent(HomeIntent.OnItemClick(it)) },
                                    onMoreClick = { /* TODO: navigate to subject list */ }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModernHeader(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color(0xFF2D2D2D)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = BmgPink,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .width(40.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(BmgPink)
        )
    }
}

@Composable
fun SubjectSection(
    section: SubjectSection,
    onItemClick: (Int) -> Unit,
    onMoreClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(18.dp)
                    .clip(CircleShape)
                    .background(BmgPink)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = subjectTypeLabel(section.type),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D2D2D)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "MORE",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.clickable { onMoreClick() }
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(section.items) { item ->
                SubjectItemCard(item = item, onItemClick = onItemClick)
            }
        }
    }
}

private fun subjectTypeLabel(type: SubjectType): String = when (type) {
    SubjectType.BOOK -> "书架上新"
    SubjectType.ANIME -> "追番列表"
    SubjectType.MUSIC -> "悦耳旋律"
    SubjectType.GAME -> "游戏世界"
    SubjectType.REAL -> "三次元剧"
}

@Composable
fun SubjectItemCard(
    item: Subject,
    onItemClick: (Int) -> Unit
) {
    val cardWidth = 120.dp
    val cornerShape = RoundedCornerShape(12.dp)

    Column(
        modifier = Modifier
            .width(cardWidth)
            .clickable { onItemClick(item.id) }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f), // 接近 2:3 比例
            shape = cornerShape,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            AsyncImage(
                model = item.images?.common?.replace("http://", "https://"),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.nameCn.ifEmpty { item.name },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp
            ),
            color = Color(0xFF2D2D2D),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 2.dp)
        )

        if (item.platform.isNotEmpty()) {
            Text(
                text = item.platform,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }
    }
}

@Composable
fun CalendarSection(
    calendar: Calendar,
    onItemClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = calendar.weekday.cn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = BmgPink
            )
            Spacer(modifier = Modifier.width(12.dp))
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = BmgPink.copy(alpha = 0.2f)
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(calendar.items) { item ->
                CalendarItemCard(item = item, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun CalendarItemCard(
    item: Calendar.Item,
    onItemClick: (Int) -> Unit
) {
    val cardWidth = 140.dp
    val cornerShape = RoundedCornerShape(12.dp)

    Column(
        modifier = Modifier
            .width(cardWidth)
            .clickable { onItemClick(item.id) }
    ) {
        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f),
                shape = cornerShape,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                AsyncImage(
                    model = item.images?.common?.replace("http://", "https://"),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // 评分
            val score = item.score ?: 0.0
            if (score > 0) {
                Surface(
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.TopEnd),
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = BmgYellow,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = String.format(Locale.getDefault(), "%.1f", score),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.nameCn.ifEmpty { item.name },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp
            ),
            color = Color(0xFF2D2D2D),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        if (item.rank != null) {
            Text(
                text = "Rank #${item.rank}",
                style = MaterialTheme.typography.labelSmall,
                color = BmgPink,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, name = "Home Screen - Loading")
@Composable
fun HomeScreenLoadingPreview() {
    BmgTheme {
        HomeScreenContent(
            uiState = HomeState(isLoading = true),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Home Screen - Success")
@Composable
fun HomeScreenPreview() {
    val mockSubjects = listOf(
        Subject(
            id = 1,
            type = 2,
            name = "SPY×FAMILY",
            nameCn = "间谍过家家",
            summary = "Summary",
            date = "2022-04-09",
            platform = "TV",
            relation = "片尾曲",
            images = Subject.Images("", "", "", "", "")
        ),
        Subject(
            id = 2,
            type = 2,
            name = "Chainsaw Man",
            nameCn = "电锯人",
            summary = "Summary",
            date = "2022-10-11",
            platform = "TV",
            relation = "片尾曲",
            images = Subject.Images("", "", "", "", "")
        )
    )

    val mockCalendars = listOf(
        Calendar(
            weekday = Calendar.Weekday(1, "星期一", "Mon", "月曜日"),
            items = listOf(
                Calendar.Item(
                    id = 1,
                    name = "Anime 1",
                    nameCn = "动画 1",
                    airDate = "2026-01-01",
                    airWeekday = 1,
                    images = null,
                    score = 8.5,
                    rank = 10,
                    summary = ""
                )
            )
        )
    )

    val mockSections = listOf(
        SubjectSection(type = SubjectType.ANIME, items = mockSubjects),
        SubjectSection(type = SubjectType.GAME, items = mockSubjects)
    )

    BmgTheme {
        HomeScreenContent(
            uiState = HomeState(
                isLoading = false,
                calendars = mockCalendars,
                subjectSections = mockSections
            ),
            onIntent = {}
        )
    }
}
