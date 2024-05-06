package com.example.news.detailpage

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.news.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(navigateBack: () -> Unit) {
    val detailViewModel: DetailPageViewModel = hiltViewModel()
    val detailPageUIState = detailViewModel.detailPageUIState.collectAsStateWithLifecycle()

    detailPageUIState.value.errorMessage?.let {
        Toast.makeText(
            LocalContext.current,
            it,
            Toast.LENGTH_LONG
        )
    }

    detailPageUIState.value.news?.let { news ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = navigateBack,
                    content = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Arrow back",
                        )
                    }
                )
            }
            GlideImage(
                model = news.imageUrl,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 320.dp),
                contentDescription = "News image",
                loading = placeholder(R.drawable.ic_launcher_background)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = news.title
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                if (!news.sourceIcon.isNullOrEmpty()) {
                    GlideImage(
                        model = news.sourceIcon,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                BorderStroke(1.dp, Color.Gray),
                                RoundedCornerShape(12.dp)
                            ),
                        loading = placeholder(R.drawable.ic_launcher_background),
                        contentDescription = "Author image"
                    )
                }
                if (!news.author.isNullOrEmpty()) {
                    Text(
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = if (news.sourceIcon.isNullOrEmpty()) 0.dp else 8.dp),
                        text = news.author.toString()
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                overflow = TextOverflow.Ellipsis,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = news.content
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 16.dp),
                text = news.source
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}