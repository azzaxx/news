package com.example.news.mainpage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.news.R
import com.example.news.di.local.News

class NewsPreviewProvider: PreviewParameterProvider<News> {
    override val values: Sequence<News>
        get() = sequenceOf(
            News(
                articalId = "",
                title = "This is title",
                author = listOf("Artur Mike"),
                content = "The Card composable acts as a Material Design container for your UI." +
                        "Cards typically present a single coherent piece of content. " +
                        "The following are some examples of where you might use a card",
                description = "The Card composable acts as a Material Design container for your UI",
                imageUrl = "https://developer.android.com/static/develop/ui/compose/images/graphics-CSC-Portrait.png",
                pubDate = "21.03.2024",
                sourceIcon = "https://developer.android.com/static/develop/ui/compose/images/graphics-CSC-Portrait.png",
                source = "News Data IO",
                nextPage = null
            )
        )
}

@Preview(showBackground = true)
@Composable
fun NewsCardPreview(@PreviewParameter(NewsPreviewProvider::class) news: News) {
    NewsCard(newsItem = news,
        addToFavorite = {

        },
        openNews = {

        },
        shareNews = {

        })
}

enum class MainTabs(val icon: ImageVector) {
    HOME(Icons.Filled.Home),
    FAVORITE(Icons.Filled.Favorite)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navigateToDetail: (News) -> Unit) {
    val viewModel: MainViewModel = hiltViewModel()
    val pullToRefreshState = rememberPullToRefreshState()
    val isLoading = viewModel.mainStateFlow.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(MainTabs.HOME) }

    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        Column {
            NewsTabs {
                selectedTab = it
            }
            when (selectedTab) {
                MainTabs.HOME -> NewsTotalList(navigateToDetail)
                MainTabs.FAVORITE -> Text(
                    text = "This is favorite tab"
                )
            }
        }
        PullToRefresh(
            modifier = Modifier.align(Alignment.TopCenter),
            pullToRefreshState = pullToRefreshState,
            isRefreshing = isLoading.value.isLoading,
            onRefresh = { viewModel.fetchNews() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTabs(selectedTab: (MainTabs) -> Unit) {
    var selectedTabState by remember { mutableIntStateOf(0) }
    val tabTitles = MainTabs.entries.map { it.name }

    PrimaryTabRow(selectedTabIndex = selectedTabState) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabState == index,
                onClick = {
                    selectedTabState = index
                    selectedTab(MainTabs.entries[index])
                },
                text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                icon = {
                    Icon(
                        imageVector = MainTabs.entries[index].icon,
                        contentDescription = "Tab Icon"
                    )
                },
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefresh(
    modifier: Modifier = Modifier,
    pullToRefreshState: PullToRefreshState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            onRefresh()
        }
    }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            pullToRefreshState.startRefresh()
        } else {
            pullToRefreshState.endRefresh()
        }
    }
    PullToRefreshContainer(
        state = pullToRefreshState,
        modifier = modifier
    )
}

@Composable
fun NewsTotalList(
    navigateToDetail: (News) -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
    listState: LazyListState = rememberLazyListState()
) {
    val newsItemsList = viewModel.newsDataPagerFlow.collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        if (newsItemsList.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = newsItemsList.itemCount,
                    key = newsItemsList.itemKey { it.articalId },
                ) { index ->
                    val news = newsItemsList[index]
                    if (news != null) {
                        NewsCard(
                            newsItem = news,
                            addToFavorite = { favorite ->
                                viewModel.addToFavorite(favorite)
                            },
                            openNews = navigateToDetail,
                            shareNews = { share ->

                            })
                    }
                }
                item {
                    if (newsItemsList.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsCard(
    newsItem: News,
    addToFavorite: (News) -> Unit,
    openNews: (News) -> Unit,
    shareNews: (News) -> Unit
) {
    val news = remember { mutableStateOf(newsItem) }

    Box(
        modifier = Modifier
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(1f)
                .clickable { openNews(news.value) },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        ) {
            Column {
                if (!news.value.imageUrl.isNullOrEmpty()) {
                    GlideImage(
                        model = news.value.imageUrl,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp),
                        contentDescription = "News image",
                        loading = placeholder(R.drawable.ic_launcher_background)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    text = news.value.title
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                ) {
                    if (!news.value.sourceIcon.isNullOrEmpty()) {
                        GlideImage(
                            model = news.value.sourceIcon,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    BorderStroke(1.dp, Color.Gray),
                                    RoundedCornerShape(12.dp)
                                ),
                            loading = placeholder(R.drawable.ic_launcher_background),
                            contentDescription = "Author image"
                        )
                    }
                    if (!news.value.author.isNullOrEmpty()) {
                        Text(
                            fontSize = 10.sp,
                            modifier = Modifier
                                .padding(start = if (news.value.sourceIcon.isNullOrEmpty()) 0.dp else 8.dp),
                            text = news.value.author.toString()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                news.value.description?.let {
                    Text(
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 4,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 16.dp),
                        text = it
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Row(modifier = Modifier.weight(1f)) {
                        IconButton(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(24.dp),
                            onClick = {
                                news.value = news.value.copy(isFavorite = !news.value.isFavorite)
                                addToFavorite(news.value)
                            }) {
                            Icon(
                                imageVector = if (news.value.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Icon favorite"
                            )
                        }
                        IconButton(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(24.dp),
                            onClick = { shareNews(news.value) }) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "Icon share"
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .height(IntrinsicSize.Max)
                    ) {
                        Text(
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp),
                            text = "4k Views"
                        )
                        Text(
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                            text = "69 Comments"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    fontSize = 10.sp,
                    modifier = Modifier.padding(start = 16.dp),
                    text = news.value.source
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}