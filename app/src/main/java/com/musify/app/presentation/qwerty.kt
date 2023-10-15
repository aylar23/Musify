package com.musify.app.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.musify.app.PlayerController
import com.musify.app.ui.components.SongView
import com.musify.app.ui.utils.reorder.ReorderableItem
import com.musify.app.ui.utils.reorder.detectReorderAfterLongPress
import com.musify.app.ui.utils.reorder.rememberReorderableLazyListState
import com.musify.app.ui.utils.reorder.reorderable

