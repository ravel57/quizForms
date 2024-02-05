package ru.yandex.ya_forms2.models

import com.fasterxml.jackson.annotation.JsonIgnore

data class RadioQuestion(
	override val id: Int,
	override val text: String,
	override val type: QuestionType = QuestionType.RADIO,
	override val groupId: Int,
	override val groupTitle: String,
	val answers: ArrayList<AnswerVariant>? = null,
	var selectedAnswer: AnswerVariant? = null,
	@JsonIgnore
	var correctAnswer: AnswerVariant? = null,
) : Question {
	constructor(it: Map<*, *>) : this(
		id = it["id"] as Int,
		text = it["text"] as String,
		type = QuestionType.RADIO,
		answers = (it["answers"] ?: mutableListOf<AnswerVariant>()) as ArrayList<AnswerVariant>,
		selectedAnswer = if (it["selectedAnswer"] == null) null else AnswerVariant(
			id = (it["selectedAnswer"] as Map<*, *>)["id"] as Int,
			text = (it["selectedAnswer"] as Map<*, *>)["text"] as String,
			isCorrect = false,//it["is_correct"] as Boolean,
			score = 0,//it["score"] as Int,
		),
		groupId = it["groupId"] as Int,
		groupTitle = it["groupTitle"] as String,
	)

	override fun isAnswerCorrect(): Boolean {
		return correctAnswer == selectedAnswer
	}
}