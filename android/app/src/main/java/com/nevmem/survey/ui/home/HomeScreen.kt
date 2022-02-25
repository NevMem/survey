package com.nevmem.survey.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nevmem.survey.R
import com.nevmem.survey.service.achievement.api.Achievement
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
                is AchievementsState -> item { AchievementsView(homeItem.achievements) }
                is SurveyState -> item { SurveyView(navController, homeItem) { vm.leaveSurvey() } }
                HomeScreenHeader -> item { HeaderItem(navController) }
                FooterScreenHeader -> item { FooterView() }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = remember {
            object : Arrangement.Vertical {
                override fun Density.arrange(
                    totalSize: Int,
                    sizes: IntArray,
                    outPositions: IntArray
                ) {
                    var currentOffset = 0
                    sizes.forEachIndexed { index, size ->
                        if (index == sizes.lastIndex) {
                            outPositions[index] = totalSize - size
                        } else {
                            outPositions[index] = currentOffset
                            currentOffset += size
                        }
                    }
                }
            }
        },
    ) {
        item { HeaderItem(rememberNavController()) }
        item { AchievementsView(emptyList()) }
        item { SurveyView(rememberNavController(), SurveyState.NoSurvey) {  } }
        item { FooterView() }
    }
}

@Composable
private fun FooterView() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {  }) {
            Icon(imageVector = Icons.Filled.Settings, contentDescription = "settings button")
        }
        Button(
            onClick = {},
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(stringResource(id = R.string.take_survey_long))
        }
    }
}

@Composable
private fun HeaderItem(navController: NavController) {
    Text(
        stringResource(id = R.string.app_name),
        modifier = Modifier
            .padding(top = 32.dp, bottom = 16.dp)
            .fillMaxWidth()
            .clickable { navController.navigate("settings") },
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h3,
    )
}

@Composable
private fun AchievementsView(achievements: List<Achievement>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Text(
                stringResource(id = R.string.achievements_title),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                style = MaterialTheme.typography.subtitle1,
            )
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
                    .horizontalScroll(rememberScrollState()),
            ) {
                achievements.forEach {
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
    leaveSurvey: () -> Unit,
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
                SurveyState.NoSurvey -> { NoSurveyView(navController = navController) }
                is SurveyState.ReadySurvey -> {
                    ReadySurveyView(
                        navController = navController,
                        item = item,
                        leaveSurvey = leaveSurvey,
                    )
                }
            }
        }
    }
}

@Composable
private fun NoSurveyView(
    navController: NavController,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { navController.navigate("join") }) {
            Text(stringResource(id = R.string.join_survey))
        }
    }
}

@Composable
private fun ReadySurveyView(
    navController: NavController,
    item: SurveyState.ReadySurvey,
    leaveSurvey: () -> Unit,
) {
    var dialogOpened by remember { mutableStateOf(false) }

    Text(
        stringResource(id = R.string.home_screen_joined_survey),
        style = MaterialTheme.typography.subtitle1,
    )
    Text(
        item.survey.name,
        modifier = Modifier.padding(top = 8.dp),
        style = MaterialTheme.typography.body2,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(
            onClick = { navController.navigate("survey") },
        ) {
            Text(stringResource(id = R.string.take_survey))
        }
        OutlinedButton(
            onClick = { dialogOpened = true }
        ) {
            Text(stringResource(id = R.string.leave_survey))
        }
    }

    if (dialogOpened) {
        AlertDialog(
            title = { Text(stringResource(id = R.string.confirm_leave_survey)) },
            onDismissRequest = { dialogOpened = false },
            confirmButton = { Button(onClick = { leaveSurvey(); dialogOpened = false }) { Text(stringResource(id = R.string.ok)) } },
            dismissButton = { OutlinedButton(onClick = { dialogOpened = false }) { Text(stringResource(id = R.string.cancel)) } },
        )
    }
}
