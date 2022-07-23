package com.mood.moodnews.ui.article

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mood.moodnews.R
import com.mood.moodnews.model.Post
import com.mood.moodnews.utils.isScrolled

/**
 * Stateless Article Screen that displays a single post.
 *
 * @param post (state) item to display.
 * @param navigationIconContent (UI) content to show for the navigation icon.
 * @param bottomBarContent (UI) content to show for the bottom bar.
 */
@Composable
private fun ArticleScreenContent(
    post: Post,
    navigationIconContent: @Composable (() -> Unit)? = null,
    bottomBarContent: @Composable () -> Unit = { },
    lazyListState: LazyListState = rememberLazyListState()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_article_background),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(36.dp)
                        )

                        Text(
                            text = stringResource(
                                id = R.string.published_in,
                                arrayOf((post.publication?.name) ?: "")
                            ),
                            style = MaterialTheme.typography.subtitle2,
                            color = LocalContentColor.current,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1.5f)
                        )

                    }
                },
                navigationIcon = navigationIconContent,
                elevation = if (lazyListState.isScrolled) 0.dp else 4.dp,
                backgroundColor = MaterialTheme.colors.surface
            )
        },
        bottomBar = bottomBarContent
    ) { innerPadding ->
        PostContent(
            post = post, modifier = Modifier.
                // innerPadding takes into account the top and bottom bar
            padding(innerPadding), state = lazyListState
        )

    }
}

