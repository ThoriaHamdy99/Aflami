package com.amsterdam.ui.screens.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.navigation.Route

@Composable
fun ListsScreen(
    modifier: Modifier = Modifier
){
    ListsScreenContent(modifier = modifier)
}

@Composable
private fun ListsScreenContent(
    modifier: Modifier = Modifier
){
                val navController = LocalNavController.current
    Column(
        modifier = modifier.fillMaxSize().background(AppTheme.color.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.clickable {
                navController.navigate(Route.ListDetails(8547660, "Fav."))
            },
            text = "List Screen",
            color = AppTheme.color.title,
            style = AppTheme.textStyle.title.large
        )
    }
}

@Preview
@Composable
private fun ListsScreenPreview(){
    ListsScreenContent()
}