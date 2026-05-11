package com.zxwork.bmg.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.zxwork.bmg.data.model.SubjectType
import com.zxwork.bmg.domain.model.Character
import com.zxwork.bmg.domain.model.Episode
import com.zxwork.bmg.domain.model.Subject
import com.zxwork.bmg.domain.model.SubjectDetail
import com.zxwork.bmg.ui.theme.BackgroundLight
import com.zxwork.bmg.ui.theme.BmgPink
import com.zxwork.bmg.ui.theme.BmgYellow
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailScreen(
    onBackClick: () -> Unit, viewModel: SubjectDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val subject = uiState.subject
    val scrollState = rememberLazyListState()

    // Calculate top bar alpha based on scroll
    // Start fading in after 50dp, fully opaque at 150dp
    val density = LocalDensity.current
    val fadeStartPx = with(density) { 50.dp.toPx() }
    val fadeEndPx = with(density) { 150.dp.toPx() }

    val toolbarAlpha by remember {
        derivedStateOf {
            val firstVisibleItemIndex = scrollState.firstVisibleItemIndex
            if (firstVisibleItemIndex > 0) 1f
            else {
                val scrollOffset = scrollState.firstVisibleItemScrollOffset.toFloat()
                ((scrollOffset - fadeStartPx) / (fadeEndPx - fadeStartPx)).coerceIn(0f, 1f)
            }
        }
    }

    Scaffold(
        containerColor = BackgroundLight,
        contentWindowInsets = WindowInsets(0) // Prevent Scaffold from adding automatic insets
    ) { paddingValues ->
        // 核心修改：明确移除对 paddingValues 的使用，让 Box 彻底置顶
        Box(modifier = Modifier.fillMaxSize()) {
            // Content
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center), color = BmgPink
                    )
                }

                uiState.error != null -> {
                    // ... error UI ...
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "加载失败 (˃̣̣̥᷄⌓˂̣̣̥᷅)", fontSize = 18.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                        Button(
                            onClick = { viewModel.onIntent(SubjectDetailIntent.Refresh) },
                            colors = ButtonDefaults.buttonColors(containerColor = BmgPink),
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("重试")
                        }
                    }
                }

                subject != null -> {
                    SubjectDetailContent(
                        subject,
                        scrollState,
                        uiState.episodes,
                        uiState.characters,
                        uiState.linkSubjects,
                    )
                }
            }

            // Custom Floating Top Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(alpha = toolbarAlpha)
                    .zIndex(1f),
                color = BackgroundLight,
                shadowElevation = if (toolbarAlpha > 0.9f) 4.dp else 0.dp
            ) {
                // 改用 Column + Spacer(statusBar) + Row 的组合，彻底解决 TopAppBar 的神秘高度问题
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                        Text(text = subject?.nameCn?.ifEmpty { subject?.name } ?: "条目详情",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            // Always show back button even when top bar is transparent
            if (toolbarAlpha < 1f) {
                Column(modifier = Modifier.zIndex(2f)) {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                    Box(
                        modifier = Modifier
                            .height(52.dp)
                            .padding(start = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(36.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Black.copy(alpha = 0.3f),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectDetailContent(
    subject: SubjectDetail,
    scrollState: LazyListState,
    episodes: List<Episode> = emptyList(),
    characters: List<Character> = emptyList(),
    linkSubjects: List<Subject> = emptyList()
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = scrollState,
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // ... (rest of the items)
        // Hero Header
        item {
            // ... (rest of hero header code)
            val parallaxOffset = remember {
                derivedStateOf {
                    if (scrollState.firstVisibleItemIndex == 0) {
                        scrollState.firstVisibleItemScrollOffset * 0.5f
                    } else 0f
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                // Background Image (Blurred with Parallax)
                AsyncImage(
                    model = subject.images?.large?.replace("http://", "https://"),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .graphicsLayer {
                            translationY = parallaxOffset.value
                            alpha = 0.4f
                        },
                    contentScale = ContentScale.Crop
                )

                // Gradient Scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, BackgroundLight),
                                startY = 0f,
                                endY = 600f
                            )
                        )
                )

                // Foreground Content
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Card(
                        modifier = Modifier
                            .width(120.dp)
                            .aspectRatio(0.7f)
                            .shadow(8.dp, RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        AsyncImage(
                            model = subject.images?.common?.replace("http://", "https://"),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = subject.nameCn.ifEmpty { subject.name },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF2D2D2D),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (subject.nameCn.isNotEmpty() && subject.name.isNotEmpty() && (subject.nameCn != subject.name)) {
                            Text(
                                text = subject.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = BmgPink, shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = subject.platform,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = subject.date,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        // Rating Section
        item {
            RatingSection(subject)
        }

        // Summary
        if (subject.summary.isNotEmpty()) {
            item {
                SectionTitle("内容简介")
                Text(
                    text = subject.summary,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp,
                    color = Color(0xFF444444)
                )
            }
        }

        // InfoBox
        if (subject.infobox.isNotEmpty()) {
            item {
                SectionTitle("详细信息")
            }
            items(subject.infobox) { item ->
                InfoBoxRow(item.key, item.value)
            }
        }

        // Tags
        if (subject.tags.isNotEmpty()) {
            item {
                SectionTitle("标签")
                FlowRow(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    subject.tags.take(15).forEach { tag ->
                        TagItem(tag)
                    }
                }
            }
        }

        // Episodes Section
        if (episodes.isNotEmpty()) {
            item {
                SectionTitle("章节列表")
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(episodes) { episode ->
                        EpisodeCard(episode)
                    }
                }
            }
        }

        // Characters Section
        if (characters.isNotEmpty()) {
            item {
                SectionTitle("角色介绍")
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(characters) { character ->
                        CharacterCard(character)
                    }
                }
            }
        }

        if (linkSubjects.isNotEmpty()) {
            item {
                SectionTitle("关联条目")
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(linkSubjects) { linkSubject ->
                        LinkSubjectCard(linkSubject)
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterCard(character: Character) {
    Column(
        modifier = Modifier.width(100.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
                .shadow(4.dp, RoundedCornerShape(12.dp)), shape = RoundedCornerShape(12.dp)
        ) {
            AsyncImage(
                model = character.images?.medium?.replace("http://", "https://"),
                contentDescription = character.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = character.name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D2D2D),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        Text(
            text = character.relation,
            style = MaterialTheme.typography.labelSmall,
            color = BmgPink,
            fontSize = 9.sp,
            maxLines = 1
        )

        if (character.actors.isNotEmpty()) {
            Text(
                text = "CV: ${character.actors.first().name}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontSize = 9.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun LinkSubjectCard(linkSubject: Subject) {

    Column(
        modifier = Modifier.width(100.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .shadow(8.dp, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp)
        ) {
            AsyncImage(
                model = linkSubject.images?.small,
                contentDescription = linkSubject.nameCn,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = linkSubject.relation,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D2D2D),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = linkSubject.nameCn,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun EpisodeCard(episode: Episode) {
    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "第 ${episode.sort.toInt()} 话",
                    style = MaterialTheme.typography.labelMedium,
                    color = BmgPink,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = episode.nameCn.ifEmpty { episode.name },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF2D2D2D),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (episode.airdate.isNotEmpty()) {
                Text(
                    text = episode.airdate,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    fontSize = 9.sp
                )
            }
        }
    }
}

@Composable
fun RatingSection(subject: SubjectDetail) {
    val rating = subject.rating ?: return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = String.format(Locale.getDefault(), "%.1f", rating.score),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = BmgYellow
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = BmgYellow,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(text = "${rating.total} votes", fontSize = 10.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            VerticalDivider(
                modifier = Modifier.height(40.dp), thickness = 1.dp, color = Color(0xFFEEEEEE)
            )

            Spacer(modifier = Modifier.width(24.dp))

            Column {
                if (rating.rank != null) {
                    Text(
                        text = "Rank #${rating.rank}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BmgPink
                    )
                }

                subject.collection?.let {
                    Text(
                        text = "${it.doing} 人在看 / ${it.collect} 人看过",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(16.dp)
                .background(BmgPink, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D2D2D)
        )
    }
}

@Composable
fun InfoBoxRow(key: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = key,
            modifier = Modifier.width(80.dp),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF444444)
        )
    }
}

@Composable
fun TagItem(tag: SubjectDetail.Tag) {
    Surface(
        color = Color(0xFFF5F5F5), shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = tag.name, fontSize = 12.sp, color = Color(0xFF666666))
            if (tag.count > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = tag.count.toString(), fontSize = 10.sp, color = Color.LightGray)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}
