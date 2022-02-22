package com.nevmem.survey

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.nevmem.survey.ui.survey.SurveyScreen
import com.nevmem.survey.ui.join.JoinScreen
import com.nevmem.survey.ui.splash.EthnoSplashScreen

@ExperimentalComposeUiApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MdcTheme {
                val scaffoldState = rememberScaffoldState()
                Scaffold(scaffoldState = scaffoldState) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") { EthnoSplashScreen(navController) }
                        composable("join") { JoinScreen(navController, scaffoldState) }
                        composable("survey") { SurveyScreen(scaffoldState) }
                    }
                }
            }
        }
    }
}
