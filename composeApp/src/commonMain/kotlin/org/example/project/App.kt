package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mohamedrejeb.calf.ui.web.WebView
import com.mohamedrejeb.calf.ui.web.rememberWebViewState
//import com.multiplatform.webview.web.WebView
//import com.multiplatform.webview.web.rememberWebViewState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        val state = rememberWebViewState(
            url = "https://github.com/MohamedRejeb"
        )

        LaunchedEffect(state.isLoading) {
            // Get the current loading state
        }
        WebView(
            state = state,
            modifier = Modifier
                .fillMaxSize()
        )
//        val webViewState =
//            rememberWebViewState("https://www.baidu.com")
//        Column(Modifier.fillMaxSize()) {
//            val text = webViewState.let {
//                "${it.pageTitle ?: ""} ${it.loadingState} ${it.lastLoadedUrl ?: ""}"
//            }
//            Text(text)
//            WebView(
//                state = webViewState,
//                modifier = Modifier.fillMaxSize()
//            )
//        }
    }
}