package com.nevmem.survey.report

import android.util.Log
import com.yandex.metrica.YandexMetrica

fun report(event: String) {
    Log.d("report", event)
    YandexMetrica.reportEvent(event)
}

fun report(event: String, message: String?) {
    Log.d("report", "Event: $event message: $message")
    YandexMetrica.reportEvent(event, message)
}

fun report(event: String, params: Map<String, String>) {
    Log.d("report", "Event: $event params: $params")
    YandexMetrica.reportEvent(event, params)
}
