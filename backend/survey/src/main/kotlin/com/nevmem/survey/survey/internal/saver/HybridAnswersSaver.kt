package com.nevmem.survey.survey.internal.saver

import com.nevmem.survey.config.ConfigProvider
import com.nevmem.survey.data.answer.SurveyAnswer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class HybridAnswersSaver(
    private val backgroundScope: CoroutineScope,
    private val configProvider: ConfigProvider,
): AnswerSaver {
    private val simpleSaver by lazy {
        SimpleAnswerSaver()
    }

    private val bunchSaver by lazy {
        BunchAnswerSaver(backgroundScope)
    }

    private var currentSaver: AnswerSaver = simpleSaver

    init {
        backgroundScope.launch {
            configProvider.config.collect {
                currentSaver = simpleSaver
                if (it.useBunchSaver) {
                    currentSaver = bunchSaver
                }
            }
        }
    }

    override fun save(answer: SurveyAnswer) {
        currentSaver.save(answer)
    }
}