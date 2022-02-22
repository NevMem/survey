package com.nevmem.survey.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nevmem.survey.R
import com.nevmem.survey.service.achievement.api.Achievement
import com.nevmem.survey.util.getText
import org.koin.androidx.compose.viewModel

@Composable
fun HomeScreen(
    navController: NavController,
) {
    val vm by viewModel<HomeScreenViewModel>()
    val items = vm.uiState.value

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items.forEach { homeItem ->
            when (homeItem) {
                is AchievementsState -> item { AchievementsView(homeItem) }
                is SurveyState -> item { SurveyView(navController, homeItem) }
                HomeScreenHeader -> item { HeaderItem() }
            }
        }
    }
}

@Composable
private fun HeaderItem() {
    Text(
        getText(id = R.string.app_name),
        modifier = Modifier
            .padding(top = 32.dp, bottom = 16.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h3,
    )
}

@Composable
private fun AchievementsView(item: AchievementsState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Text(
                getText(id = R.string.achievements_title),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                style = MaterialTheme.typography.body1,
            )
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
                    .horizontalScroll(rememberScrollState()),
            ) {
                item.achievements.forEach {
                    AchievementView(it)
                }
            }
        }
    }
}

@Composable
private fun SurveyView(
    navController: NavController,
    item: SurveyState,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            when (item) {
                is SurveyState.AlreadyAnsweredSurvey -> {
                    Text(item.survey.name)
                }
                SurveyState.NoSurvey -> {
                    Button(onClick = { navController.navigate("join") }) {
                        Text(getText(id = R.string.join_survey))
                    }
                }
                is SurveyState.ReadySurvey -> {
                    Text(
                        getText(id = R.string.home_screen_joined_survey),
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        item.survey.name,
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.body2,
                    )
                    Button(
                        modifier = Modifier.padding(top = 8.dp),
                        onClick = { navController.navigate("survey") },
                    ) {
                        Text(getText(id = R.string.take_survey))
                    }
                }
            }
        }
    }
}
