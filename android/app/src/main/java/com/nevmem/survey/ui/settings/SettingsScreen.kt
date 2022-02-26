package com.nevmem.survey.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nevmem.survey.R
import org.koin.androidx.compose.viewModel

@Composable
fun SettingsScreen(
    navController: NavController,
) {
    val vm by viewModel<SettingsScreenViewModel>()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        vm.uiState.value.forEach {
            when (it) {
                HeaderSettingsScreenItem -> item { HeaderView(navController) }
                is AboutSettingsScreenItem -> item { AboutView(it) }
                is SwitchSettingsScreenItem -> item { SwitchView(it) }
            }
        }
    }
}

@Composable
private fun SwitchView(item: SwitchSettingsScreenItem) {
    val scope = rememberCoroutineScope()
    val value = item.setting.changes.collectAsState(initial = item.setting.value, scope.coroutineContext)

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(stringResource(id = item.title))
        Switch(
            checked = value.value,
            onCheckedChange = {
                item.setting.value = it
            }
        )
    }
}

@Composable
private fun AboutView(item: AboutSettingsScreenItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(id = R.string.app_name), style = MaterialTheme.typography.h4)
        Text("Version: ${item.version}", style = MaterialTheme.typography.body2)
    }
}

@Composable
private fun HeaderView(navController: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Filled.ArrowBack, "back")
        }
        Text(
            stringResource(id = R.string.settings_title),
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { HeaderView(navController = rememberNavController()) }
        item { AboutView(item = AboutSettingsScreenItem("0.0.0")) }
    }
}
