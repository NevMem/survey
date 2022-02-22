package com.nevmem.survey.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nevmem.survey.service.achievement.api.Achievement

@Composable
fun AchievementView(achievement: Achievement) {
    when (achievement) {
        is Achievement.CounterAchievement -> {
            Text(achievement.title + " " + achievement.result.toString())
        }
        is Achievement.VeryFirstAchievement -> {
            if (achievement.id == "very-first-survey") {
                VeryFirstAchievement()
            } else {
                Text("Unsupported achievement ${achievement.title}")
            }
        }
    }
}

@Composable
fun VeryFirstAchievement() {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary.copy(alpha = 0.75f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "1",
            style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.background),
        )
        Text(
            text = "st",
            style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.background),
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 6.dp, end = 6.dp),
        )
    }
}
