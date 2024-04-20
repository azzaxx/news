package com.example.news

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.news.detailpage.DetailScreen
import com.example.news.mainpage.MainScreen


const val NEWS_ID_ARGUMENT = "newsId"

sealed class Screen(val route: String) {
    data object NewsListScreen : Screen("NewsList")
    data object DetailNewsScreen : Screen("DetailNews")
}

@Composable
fun StartNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.NewsListScreen.route) {
        composable(route = Screen.NewsListScreen.route) {
            MainScreen { news ->
                navController.navigate("${Screen.DetailNewsScreen.route}/${news.articalId}")
            }
        }
        composable(route = "${Screen.DetailNewsScreen.route}/{$NEWS_ID_ARGUMENT}") {
            DetailScreen(navigateBack = {
                navController.popBackStack()
            })
        }
    }
}