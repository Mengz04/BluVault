package com.example.bluvault.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.pager.ExperimentalPagerApi
import androidx.compose.runtime.Composable
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.bluvault.operations.CardData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CardCarousel(
    cards: List<CardData>,
    selectedIndex: Int,
    onCardChange: (Int) -> Unit
) {
    if (cards.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No cards available",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
        return
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val cardHeightDp = 180.dp
    val overlapDp = 60.dp
    val listHeightDp = 300.dp

    val scrollToCenter: (Int) -> Unit = { index ->
        coroutineScope.launch {
            val cardHeightPx = with(density) { cardHeightDp.toPx() }
            val overlapPx = with(density) { overlapDp.toPx() }
            val listHeightPx = with(density) { listHeightDp.toPx() }

            val centerOffset = (listHeightPx / 2 - cardHeightPx / 2).toInt()

            listState.animateScrollToItem(
                index = index,
                scrollOffset = -centerOffset
            )
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(listHeightDp),
        verticalArrangement = Arrangement.spacedBy(-overlapDp),
        contentPadding = PaddingValues(vertical = 60.dp)
    ) {
        itemsIndexed(cards) { index, card ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(if (index == selectedIndex) 1f else 0f)
                    .clickable {
                        onCardChange(index)
                        scrollToCenter(index)
                    }
            ) {
                CardItem(card = card, isSelected = index == selectedIndex)
            }
        }
    }
}





