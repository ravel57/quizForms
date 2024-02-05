package ru.yandex.ya_forms2.models

import com.fasterxml.jackson.annotation.JsonIgnore

data class TextQuestion(
	override val id: Int,
	override val text: String,
	override val type: QuestionType = QuestionType.TEXT,
	override val groupId: Int,
	var answer: String,
	@JsonIgnore
	var correctAnswer: AnswerVariant?,
	override val groupTitle: String,
) : Question {

	constructor(it: Map<*, *>) : this(
		id = it["id"] as Int,
		text = it["text"] as String,
		type = QuestionType.TEXT,
		answer = if (it["answer"] != null) it["answer"] as String else "",
		correctAnswer = null,
		groupId = it["groupId"] as Int,
		groupTitle = it["groupTitle"] as String,
	)


	override fun isAnswerCorrect(): Boolean {
		return correctAnswer?.text == answer
	}

}
