package com.metrolist.music.ui.screens.setup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import com.metrolist.music.R
import com.metrolist.music.constants.HasSeenSetupWizardKey
import com.metrolist.music.utils.dataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetupWizardScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 6 })

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                if (pagerState.currentPage > 0) {
                    TextButton(onClick = { 
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }) {
                        Text(stringResource(R.string.setup_wizard_back))
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                val isLastPage = pagerState.currentPage == 5
                Button(onClick = { 
                    coroutineScope.launch {
                        if (isLastPage) {
                            context.dataStore.edit { it[HasSeenSetupWizardKey] = true }
                        } else {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }) {
                    Text(if (isLastPage) stringResource(R.string.setup_wizard_finish) else stringResource(R.string.setup_wizard_next))
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> LookAndFeelPage()
                1 -> ConnectLibraryPage()
                2 -> PlayerDesignPage()
                3 -> BottomNavPage()
                4 -> AudioPlaybackPage()
                5 -> ContentDataPage()
            }
        }
    }
}
