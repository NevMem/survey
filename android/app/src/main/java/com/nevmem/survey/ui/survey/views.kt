package com.nevmem.survey.ui.survey

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nevmem.survey.data.answer.QuestionAnswer

@Composable
internal fun RadioQuestionImpl(
    item: RadioQuestion,
    setCurrentAnswer: (QuestionAnswer) -> Unit,
) {
    var selectedId: String? by remember { mutableStateOf(null) }

    QuestionCard {
        Column {
            Text(
                item.title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(8.dp)
            )
            item.variants.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = it.id == selectedId,
                        onClick = {
                            selectedId = it.id
                            setCurrentAnswer(QuestionAnswer.RadioQuestionAnswer(it.id))
                        },
                    )
                    Text(text = it.variant)
                }
            }
        }
    }
}

@Preview
@Composable
private fun RadioQuestionImplPreview() {
    RadioQuestionImpl(
        RadioQuestion(
            "radio?",
            variants = listOf(
                RadioQuestion.QuestionVariant("0", "first"),
                RadioQuestion.QuestionVariant("1", "second"),
                RadioQuestion.QuestionVariant("2", "third"),
                RadioQuestion.QuestionVariant("3", "forth"),
            )
        ),
        setCurrentAnswer = {},
    )
}

@Composable
internal fun QuestionCard(content: @Composable () -> Unit) {
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
