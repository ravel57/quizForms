package ru.ravel.quizforms.entites

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import ru.ravel.quizforms.converters.TypeConverter
import ru.ravel.quizforms.enums.QuestionType
import java.util.*
import kotlin.jvm.Transient

@Entity
data class RadioQuestion(

	@Id
	@get:Column(name = "id")
	override var id: Long,

	@get:Column(name = "text")
	override var text: String,

	@get:Enumerated(EnumType.ORDINAL)
	@get:Convert(converter = TypeConverter::class)
	override var type: QuestionType = QuestionType.RADIO,

	@Transient
	var selectedAnswer: AnswerVariant? = null,

	@JsonIgnore
	@Transient
	var correctAnswer: AnswerVariant? = null,

	@get:Column(name = "answers")
	override var answers: MutableList<AnswerVariant>,
) : Question() {

	constructor(it: Map<*, *>) : this(
		id = it["id"] as Long,
		text = it["text"] as String,
		type = QuestionType.RADIO,
		selectedAnswer = if (it["selectedAnswer"] == null) null else AnswerVariant(
			id = (it["selectedAnswer"] as Map<*, *>)["id"] as Long,
			text = (it["selectedAnswer"] as Map<*, *>)["text"] as String,
			isCorrect = false,//it["is_correct"] as Boolean,
			score = 0,//it["score"] as Int,
		),
		answers = Collections.emptyList()
	)

	constructor() : this(
		id = -1,
		text = "",
		type = QuestionType.RADIO,
		selectedAnswer = null,
		correctAnswer = null,
		answers = Collections.emptyList()
	)

//	override fun isAnswerCorrect(): Boolean {
//		return correctAnswer == selectedAnswer
//	}
}