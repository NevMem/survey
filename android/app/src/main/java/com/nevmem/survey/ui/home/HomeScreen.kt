package com.nevmem.survey.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nevmem.survey.R
import com.nevmem.survey.util.getText
import org.koin.androidx.compose.viewModel

@ExperimentalComposeUiApi
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
        item {
            Spacer(modifier = Modifier.height(256.dp))
        }
    }
}

@ExperimentalComposeUiApi
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
                    Text(item.surveyName, style = MaterialTheme.typography.h5, modifier = Modifier.padding(8.dp))
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
        is TextQuestion -> { TextQuestionImpl(item = item) }
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

@ExperimentalComposeUiApi
@Composable
fun TextQuestionImpl(item: TextQuestion) {
    var text by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val itemsPadding = 16.dp

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
    ) {
        Column {
            Text(
                item.title,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(start = itemsPadding, top = itemsPadding, end = itemsPadding),
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = itemsPadding, start = itemsPadding, end = itemsPadding)
                    .fillMaxWidth(),
                value = text,
                onValueChange = {
                    if (it.length <= item.maxLength) {
                        text = it
                    }
                },
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide(); focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            )
            Text(
                modifier = Modifier.fillMaxWidth().padding(start = itemsPadding, end = itemsPadding, bottom = itemsPadding),
                text = "${text.length} / ${item.maxLength}",
                textAlign = TextAlign.End,
            )
        }
    }
}
