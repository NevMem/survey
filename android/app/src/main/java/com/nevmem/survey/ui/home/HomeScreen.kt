package com.nevmem.survey.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nevmem.survey.R
import com.nevmem.survey.util.getText
import org.koin.androidx.compose.viewModel

@Composable
fun HomeScreen() {
    val viewModel: HomeScreenViewModel by viewModel()

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        viewModel.items.value.map {
            item {
                HomeScreenItemImpl(it)
            }
        }
    }
}

@Composable
private fun HomeScreenItemImpl(item: HomeScreenItem) {
    when (item) {
        is Header -> {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = 3.dp
            ) {
                Column {
                    Text(item.surveyName, style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(8.dp))
                    Text(getText(id = R.string.home_survey_id) + " " + item.surveyId, style = MaterialTheme.typography.body2, modifier = Modifier.padding(8.dp))
                }
            }
        }
        is RatingQuestion -> {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column {
                    Text(item.title, style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(8.dp))
                    Text(item.min.toString(), style = MaterialTheme.typography.body2, modifier = Modifier.padding(8.dp))
                    Text(item.max.toString(), style = MaterialTheme.typography.body2, modifier = Modifier.padding(8.dp))
                }
            }
        }
        is TextQuestion -> {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column {
                    Text(item.title, style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(8.dp))
                    Text(item.maxLength.toString(), style = MaterialTheme.typography.body2, modifier = Modifier.padding(8.dp))
                }
            }
        }
        is StarsQuestion -> {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column {
                    Text(item.title, style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(8.dp))
                    Text(item.stars.toString(), style = MaterialTheme.typography.body2, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}
