package com.nevmem.survey.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nevmem.survey.R
import com.nevmem.survey.util.getText
import org.koin.androidx.compose.viewModel

@ExperimentalComposeUiApi
@Composable
fun HomeScreen() {
    val viewModel: HomeScreenViewModel by viewModel()

    val survey = viewModel.survey.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.padding(16.dp)) {
                Text(survey.name, style = MaterialTheme.typography.h5)
            }

            Text("${viewModel.progress.value + 1} / ${survey.questions.size}")

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp, top = 8.dp),
                progress = (viewModel.progress.value.toFloat() + 1) / survey.questions.size,
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            HomeScreenItemImpl(item = viewModel.currentItem.value)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            OutlinedButton(onClick = { viewModel.previous() }) {
                Text(getText(id = R.string.previous), modifier = Modifier.padding(4.dp))
            }
            Button(onClick = { viewModel.next() }) {
                Text(getText(id = R.string.next), modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun HomeScreenItemImpl(item: HomeScreenItem) {
    when (item) {
        is RatingQuestion -> {
            QuestionCard {
                Column {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        item.min.toString(),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        item.max.toString(),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
        is TextQuestion -> { TextQuestionImpl(item = item) }
        is StarsQuestion -> {
            QuestionCard {
                Column {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        item.stars.toString(),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(8.dp)
                    )
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

    QuestionCard {
        Column(modifier = Modifier) {
            Text(
                item.title,
                style = MaterialTheme.typography.subtitle1,
                overflow = TextOverflow.Visible,
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = itemsPadding)
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
                "${text.length} / ${item.maxLength}",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.End,
            )
        }
    }
}

@Composable
fun QuestionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colors.primary)
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}
