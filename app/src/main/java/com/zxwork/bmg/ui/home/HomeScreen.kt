@file:OptIn(ExperimentalMaterial3Api::class)

package com.zxwork.bmg.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.zxwork.bmg.data.model.SubjectModel
import com.zxwork.bmg.data.model.SubjectType
import com.zxwork.bmg.ui.theme.BmgOrange
import com.zxwork.bmg.ui.theme.BmgPink
import com.zxwork.bmg.ui.theme.BmgPinkLight
import com.zxwork.bmg.ui.theme.BmgTheme
import com.zxwork.bmg.ui.theme.BmgWarmBg
import com.zxwork.bmg.ui.theme.BmgYellow

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
        containerColor = BmgWarmBg.copy(alpha = 0.5f) // 使用全局暖色背景
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = BmgPink
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
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp),
                        verticalArrangement = Arrangement.spacedBy(28.dp)
                    ) {
                        item {
                            CuteHeader(title = "每日放送 ✨")
                        }
                        
                        items(uiState.calendars) { calendar ->
                            CalendarSection(calendar = calendar, onItemClick = { onIntent(HomeIntent.OnItemClick(it)) })
                        }
                        
                        item {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                                thickness = 2.dp,
                                color = BmgPink.copy(alpha = 0.1f)
                            )
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
fun CuteHeader(title: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Surface(
            color = BmgPink,
            shape = RoundedCornerShape(topStart = 24.dp, bottomEnd = 24.dp, topEnd = 8.dp, bottomStart = 8.dp),
            modifier = Modifier.shadow(6.dp, RoundedCornerShape(topStart = 24.dp, bottomEnd = 24.dp, topEnd = 8.dp, bottomStart = 8.dp))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
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
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 甜甜圈形状的装饰
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(BmgOrange)
                    .border(3.dp, BmgOrange.copy(alpha = 0.3f), CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = subjectTypeLabel(section.type),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5C4033) // 深巧克力暖色
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onMoreClick) {
                Text("更多 >", color = BmgPink, fontWeight = FontWeight.Bold)
            }
        }
        
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
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
    item: SubjectModel.Item,
    onItemClick: (Int) -> Unit
) {
    val cardWidth = 160.dp
    val cardHeight = 220.dp
    val cornerShape = RoundedCornerShape(16.dp)
    
    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .shadow(4.dp, cornerShape)
            .clickable { item.id?.let(onItemClick) },
        shape = cornerShape,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = item.images?.common?.replace("http://", "https://"),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // 顶部半透明遮罩 (可选，增加氛围感)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.2f))
                            )
                        )
                )

                // 平台标签
                val sub = item.platform ?: item.date
                if (!sub.isNullOrEmpty()) {
                    Surface(
                        modifier = Modifier.padding(8.dp).align(Alignment.TopEnd),
                        color = BmgPink.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = sub.take(4),
                            fontSize = 9.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.nameCn?.ifEmpty { item.name ?: "" } ?: item.name ?: "",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    color = Color(0xFF4A4A4A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = item.name ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun CalendarSection(
    calendar: CalendarModel,
    onItemClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = BmgPinkLight.copy(alpha = 0.3f),
                shape = CircleShape
            ) {
                Text(
                    text = calendar.weekday.cn,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    color = BmgPink,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "New Releases",
                fontSize = 12.sp,
                color = Color.LightGray,
                fontWeight = FontWeight.Medium
            )
        }
        
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
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
    item: CalendarModel.Item,
    onItemClick: (Int) -> Unit
) {
    val cardWidth = 160.dp
    val cardHeight = 220.dp
    val cornerShape = RoundedCornerShape(16.dp)
    
    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .shadow(4.dp, cornerShape)
            .clickable { onItemClick(item.id) },
        shape = cornerShape,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = item.images?.common?.replace("http://", "https://"),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // 评分标签
                val score = item.rating?.score ?: 0.0
                if (score > 0) {
                    Surface(
                        modifier = Modifier.padding(8.dp).align(Alignment.BottomEnd),
                        color = BmgYellow,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "$score", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
            
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.nameCn.ifEmpty { item.name },
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    color = Color(0xFF4A4A4A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (item.rank != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Rank #${item.rank}",
                        fontSize = 9.sp,
                        color = BmgPink,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(BmgPink.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
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
        HomeScreenContent(
            uiState = HomeState(),
            onIntent = {}
        )
    }
}
