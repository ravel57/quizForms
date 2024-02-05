package ru.yandex.ya_forms2.models

data class AnswerVariant (
    val id: Int,
    val text: String,
    var isCorrect: Boolean,
    val score: Int,
)