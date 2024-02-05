package ru.yandex.ya_forms2.models

data class GroupOfQuestion(
	val id: Int,
	val title: String,
	val questions: Set<Question>,
)