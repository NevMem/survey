package com.nevmem.survey

import kotlin.random.Random

object RandomStringGenerator {
    fun randomString(length: Int) = buildString {
        repeat(length) {
            append(choice(('A'..'Z').toList()))
        }
    }

    fun <T>choice(from: List<T>): T = from[Random.Default.nextInt(from.size)]
}
