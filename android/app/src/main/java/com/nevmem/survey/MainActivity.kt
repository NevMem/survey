package com.nevmem.survey

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.material.composethemeadapter3.Mdc3Theme


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Mdc3Theme {
                SplashScreen()
            }
        }
    }

    @Composable
    private fun SplashScreen() {
        Text(
            text = "Ethnosurvey",
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentWidth()
                .wrapContentHeight()
        )
    }
}
