package ru.yandex.ya_forms2.models

import com.fasterxml.jackson.annotation.JsonIgnore

data class CheckboxQuestion(
	override val id: Int,
	override val text: String,
	override val type: QuestionType = QuestionType.CHECKBOX,
	override val groupId: Int,
	val answers: ArrayList<AnswerVariant>? = null,
	var selectedAnswers: List<AnswerVariant>? = null,
	@JsonIgnore
	var correctAnswers: List<AnswerVariant>? = null,
	override val groupTitle: String,
) : Question {

	constructor(it: Map<*, *>) : this(
		id = it["id"] as Int,
		text = it["text"] as String,
		type = QuestionType.CHECKBOX,
		selectedAnswers = (if (it["selectedAnswers"] == null) listOf<Any>() else it["selectedAnswers"] as List<*>).map {
			AnswerVariant(
				id = (it as Map<*, *>)["id"] as Int,
				text = it["text"] as String,
				isCorrect = false,// it["is_correct"] as Boolean,
				score = 0, //it["score"] as Int,
			)
		},
		answers = (it["answers"] ?: mutableListOf<AnswerVariant>()) as ArrayList<AnswerVariant>,
		groupId = it["groupId"] as Int,
		groupTitle = it["groupTitle"] as String,
	)

	override fun isAnswerCorrect(): Boolean {
		return selectedAnswers!!.any { it -> it.id in correctAnswers!!.map { it.id } }
	}

}
