package com.nevmem.survey.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nevmem.survey.R

@Preview
@Composable
fun HomeScreen(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: HomeScreenViewModel = HomeScreenViewModel()
) {
    var surveyId: String by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(elevation = 4.dp, modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ErrorCard(scaffoldState, viewModel.state.value)
                Text(
                    getText(id = R.string.enter_survey_id),
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.body1,
                )

                Row(modifier = Modifier.padding(8.dp)) {
                    OutlinedTextField(
                        value = surveyId,
                        onValueChange = { surveyId = it },
                        label = { Text(getText(R.string.survey_id_label)) }
                    )
                }
                Row(modifier = Modifier.padding(8.dp)) {
                    Button(
                        onClick = {
                            viewModel.tryFetchSurvey(surveyId)
                        },
                        enabled = viewModel.state.value != HomeScreenViewModel.UiState.Loading,
                    ) {
                        Text(text = "Join survey")
                    }
                }
            }
        }
    }
}

@Composable
fun getText(@StringRes id: Int) = LocalContext.current.resources.getText(id).toString()

@Composable
private fun ErrorCard(scaffoldState: ScaffoldState, state: HomeScreenViewModel.UiState) {
    if (state is HomeScreenViewModel.UiState.Error) {
        LaunchedEffect(state.message) {
            scaffoldState.snackbarHostState.showSnackbar(state.message, duration = SnackbarDuration.Long)
        }
    }
}
