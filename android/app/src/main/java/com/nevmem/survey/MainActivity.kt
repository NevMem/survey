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
import com.nevmem.survey.report.report
import com.nevmem.survey.service.push.api.PushService
import com.nevmem.survey.ui.home.HomeScreen
import com.nevmem.survey.ui.join.JoinScreen
import com.nevmem.survey.ui.settings.SettingsScreen
import com.nevmem.survey.ui.splash.EthnoSplashScreen
import com.nevmem.survey.ui.survey.SurveyScreen
import org.koin.android.ext.android.inject

@ExperimentalComposeUiApi
class MainActivity : AppCompatActivity() {

    private val pushService by inject<PushService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        report("main-activity", "onCreate")

        pushService.start()

        setContent {
            MdcTheme {
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    scaffoldState = scaffoldState,
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") { EthnoSplashScreen(navController) }
                        composable("join") { JoinScreen(navController, scaffoldState) }
                        composable("survey") { SurveyScreen(scaffoldState) }
                        composable("home") { HomeScreen(navController) }
                        composable("settings") { SettingsScreen(navController) }
                    }
                }
            }
        }
    }
}
