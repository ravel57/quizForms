package ru.yandex.ya_forms2.models

interface Question {
    val id: Int
    val text: String
    val type: QuestionType
    val groupId: Int
    val groupTitle: String

    fun isAnswerCorrect(): Boolean
}