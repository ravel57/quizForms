package ru.ravel.quizforms.entites

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import ru.ravel.quizforms.converters.TypeConverter
import ru.ravel.quizforms.enums.QuestionType
import java.util.*
import kotlin.jvm.Transient

@Entity
data class CheckboxQuestion(

	@Id
	@get:Column(name = "id")
	override var id: Long,

	@get:Column(name = "text")
	override var text: String,

	@get:Enumerated(EnumType.ORDINAL)
	@get:Convert(converter = TypeConverter::class)
	override var type: QuestionType = QuestionType.CHECKBOX,

	@get:Column(name = "answers")
	override var answers: MutableList<AnswerVariant> = Collections.emptyList(),

	@Transient
	var selectedAnswers: List<AnswerVariant>? = null,

	@JsonIgnore
	@Transient
	var correctAnswers: List<AnswerVariant>? = null,
) : Question() {

	constructor() : this(
		id = -1,
		text = "",
		type = QuestionType.CHECKBOX,
		selectedAnswers = null,
		correctAnswers = null,
	)

//	override fun isAnswerCorrect(): Boolean {
//		return selectedAnswers!!.any { it.id in correctAnswers!!.map { answerVariant -> answerVariant.id } }
//	}

}
