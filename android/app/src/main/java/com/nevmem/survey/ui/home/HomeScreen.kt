package com.nevmem.survey.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Slider
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
import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.util.getText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel

@ExperimentalComposeUiApi
@Composable
fun HomeScreen(
    scaffoldState: ScaffoldState,
) {
    val viewModel: HomeScreenViewModel by viewModel()

    val survey = viewModel.survey.value

    var currentAnswer: QuestionAnswer? by rememberSaveable { mutableStateOf(null) }
    val setCurrentAnswer = { answer: QuestionAnswer ->
        currentAnswer = answer
    }

    val needAnswerMessage = getText(id = R.string.wait_for_answer)
    val moveNext: () -> Unit = {
        if (currentAnswer == null) {
            GlobalScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(needAnswerMessage)
            }
        } else {
            viewModel.dispatch(HomeScreenAction.Next(currentAnswer!!))
            currentAnswer = null
        }
    }

    val send: () -> Unit = {
        if (currentAnswer == null) {
            GlobalScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(needAnswerMessage)
            }
        } else {
            viewModel.dispatch(HomeScreenAction.Send(currentAnswer!!))
            currentAnswer = null
        }
    }

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

            HomeScreenProgressBar(viewModel.uiState.value)
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            HomeScreenItemImpl(
                item = viewModel.uiState.value.currentItem,
                setCurrentAnswer = setCurrentAnswer,
            )
        }

        ActionsRow(
            actions = viewModel.uiState.value.actions,
            moveNext = moveNext,
            movePrev = { viewModel.dispatch(HomeScreenAction.Previous) },
            send = send,
            retry = { viewModel.dispatch(HomeScreenAction.Retry) }
        )
    }
}

@Composable
private fun ActionsRow(
    actions: List<HomeScreenActionType>,
    moveNext: () -> Unit,
    movePrev: () -> Unit,
    send: () -> Unit,
    retry: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 32.dp),
        horizontalArrangement = if (actions.size >= 2) Arrangement.SpaceBetween else Arrangement.SpaceAround,
    ) {
        actions.forEach {
            when (it) {
                HomeScreenActionType.Next -> NextAction(moveNext = moveNext)
                HomeScreenActionType.Send -> SendAction(send = send)
                HomeScreenActionType.Previous -> PrevAction(movePrev = movePrev)
                HomeScreenActionType.Retry -> RetryAction(retry = retry)
            }
        }
    }
}

@Composable
private fun NextAction(moveNext: () -> Unit) {
    Button(onClick = { moveNext() }) {
        Text(getText(id = R.string.next), modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp))
    }
}

@Composable
private fun PrevAction(movePrev: () -> Unit) {
    OutlinedButton(onClick = { movePrev() }) {
        Text(getText(id = R.string.previous), modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp))
    }
}

@Composable
private fun SendAction(send: () -> Unit) {
    Button(onClick = { send() }) {
        Text(getText(id = R.string.send), modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp))
    }
}

@Composable
private fun RetryAction(retry: () -> Unit) {
    Button(onClick = { retry() }) {
        Text(getText(id = R.string.retry), modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp))
    }
}

@Composable
private fun HomeScreenProgressBar(uiState: HomeScreenUiState) {
    if (uiState.progress is ProgressState.ActualProgress) {
        Text("${uiState.progress.progress} / ${uiState.progress.outOf}")

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp, top = 8.dp),
            progress = uiState.progress.percentage,
        )
    }
}

@ExperimentalComposeUiApi
@Composable
private fun HomeScreenItemImpl(
    item: HomeScreenItem,
    setCurrentAnswer: (QuestionAnswer) -> Unit,
) {
    when (item) {
        is RatingQuestion -> { RatingQuestionImpl(item = item, setCurrentAnswer = setCurrentAnswer) }
        is TextQuestion -> { TextQuestionImpl(item = item, setCurrentAnswer = setCurrentAnswer) }
        is StarsQuestion -> { StarsQuestionImpl(item = item, setCurrentAnswer = setCurrentAnswer) }
        else -> { SendingView(item = item) }
    }
}

@Composable
private fun SendingView(item: HomeScreenItem) {
    QuestionCard {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            when (item) {
                SendingAnswers.Sending -> {
                    CircularProgressIndicator()
                }
                is SendingAnswers.Error -> {
                    Column {
                        Text(
                            getText(id = R.string.sending_answers_failed),
                            style = MaterialTheme.typography.h4,
                        )
                        Text(
                            getText(id = R.string.sending_answers_failed_description),
                            modifier = Modifier.padding(top = 12.dp),
                            style = MaterialTheme.typography.subtitle2,
                        )
                        Text(
                            item.message,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                }
                SendingAnswers.Success -> {
                    Text("Success")
                }
                else -> throw IllegalStateException()
            }
        }
    }
}

@Composable
private fun RatingQuestionImpl(
    item: RatingQuestion,
    setCurrentAnswer: (QuestionAnswer) -> Unit,
) {
    var sliderValue by rememberSaveable { mutableStateOf(item.min.toFloat()) }
    QuestionCard {
        Column {
            Text(
                item.title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(8.dp),
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    item.min.toString(),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(8.dp),
                )
                Slider(
                    value = sliderValue,
                    onValueChange = {
                        sliderValue = it
                        setCurrentAnswer(QuestionAnswer.RatingQuestionAnswer(it.toInt()))
                    },
                    steps = item.max - item.min - 1,
                    valueRange = item.min.toFloat()..item.max.toFloat(),
                    modifier = Modifier.weight(1f),
                )
                Text(
                    item.max.toString(),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}

@Composable
private fun StarsQuestionImpl(
    item: StarsQuestion,
    setCurrentAnswer: (QuestionAnswer) -> Unit,
) {
    var sliderValue by rememberSaveable { mutableStateOf((item.stars / 2 + 1).toFloat()) }
    QuestionCard {
        Column {
            Text(
                item.title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(8.dp)
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "1",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(8.dp),
                )
                Slider(
                    value = sliderValue,
                    onValueChange = {
                        sliderValue = it
                        setCurrentAnswer(QuestionAnswer.StarsQuestionAnswer(it.toInt()))
                    },
                    steps = item.stars - 2,
                    valueRange = 1f..item.stars.toFloat(),
                    modifier = Modifier.weight(1f),
                )
                Text(
                    item.stars.toString(),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun TextQuestionImpl(
    item: TextQuestion,
    setCurrentAnswer: (QuestionAnswer) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val itemsPadding = 16.dp

    QuestionCard {
        Column(modifier = Modifier) {
            Text(
                item.title,
                style = MaterialTheme.typography.h5,
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
                        setCurrentAnswer(QuestionAnswer.TextQuestionAnswer(text))
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
private fun QuestionCard(content: @Composable () -> Unit) {
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
