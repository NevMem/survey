package com.nevmem.survey.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun getText(@StringRes id: Int) = LocalContext.current.resources.getText(id).toString()
