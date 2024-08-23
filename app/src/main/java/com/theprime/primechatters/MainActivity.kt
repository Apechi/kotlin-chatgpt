package com.theprime.primechatters

import android.annotation.SuppressLint
import android.media.tv.AdRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import com.adsmedia.adsmodul.AdsHelper
import com.adsmedia.adsmodul.utils.AdsConfig
import com.theprime.primechatters.ui.common.AppBar
import com.theprime.primechatters.ui.common.AppScaffold
import com.theprime.primechatters.ui.conversations.Conversation
import com.theprime.primechatters.ui.theme.ChatGPTLiteTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        AdsHelper.initializeAdsPrime(this, BuildConfig.APPLICATION_ID, AdsConfig.Game_App_ID)
        AdsHelper.loadInterstitialPrime(this, AdsConfig.Interstitial_ID)

        setContentView(
            ComposeView(this).apply {
                consumeWindowInsets = false
                setContent {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val drawerOpen by mainViewModel.drawerShouldBeOpened.collectAsState()

                    if (drawerOpen) {
                        // Open drawer and reset state in VM.
                        LaunchedEffect(Unit) {
                            // wrap in try-finally to handle interruption whiles opening drawer
                            try {
                                drawerState.open()
                            } finally {
                                mainViewModel.resetOpenDrawerAction()
                            }
                        }
                    }

                    // Intercepts back navigation when the drawer is open
                    val scope = rememberCoroutineScope()
                    val focusManager = LocalFocusManager.current

                    BackHandler {
                        if (drawerState.isOpen) {
                            scope.launch {
                                drawerState.close()
                            }
                        } else {
                            focusManager.clearFocus()
                        }
                    }
                    val darkTheme = remember(key1 = "darkTheme") {
                        mutableStateOf(true)
                    }
                    ChatGPTLiteTheme(darkTheme.value) {
                        Surface(
                            color = MaterialTheme.colorScheme.background,
                        ) {
                            AppScaffold(
                                drawerState = drawerState,
                                onChatClicked = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                onNewChatClicked = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                    AdsHelper.showInterstitialPrime(this@MainActivity, AdsConfig.Interstitial_ID, AdsConfig.Interval)
                                },
                                onIconClicked = {
                                    darkTheme.value = !darkTheme.value
                                }
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 46.dp)

                                    ) {
                                        AppBar(onClickMenu = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        })
                                        Divider()
                                        Conversation()
                                    }

                                    // Add the RelativeLayout banner ad here
                                    AndroidView(
                                        factory = { context ->
                                            LayoutInflater.from(context)
                                                .inflate(R.layout.ads_layout, null)
                                        },
                                        update = { view ->
                                            AdsHelper.showBannerPrime(
                                                this@MainActivity,
                                                view.findViewById<RelativeLayout>(R.id.layads),
                                                AdsConfig.Game_App_ID
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 50.dp) // Align the ad to the bottom center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChatGPTLiteTheme {

    }
}