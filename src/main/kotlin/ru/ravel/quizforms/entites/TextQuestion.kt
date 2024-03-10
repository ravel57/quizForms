package ru.ravel.quizforms.entites

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import ru.ravel.quizforms.converters.TypeConverter
import ru.ravel.quizforms.enums.QuestionType
import java.util.*
import kotlin.jvm.Transient

@Entity
data class TextQuestion(

	@Id
	@get:Column(name = "id")
	override var id: Long,

	@get:Column(name = "text")
	override var text: String,

	@get:Enumerated(EnumType.ORDINAL)
	@get:Convert(converter = TypeConverter::class)
	override var type: QuestionType = QuestionType.TEXT,

	@JsonIgnore
	@Transient
	var correctAnswers: List<AnswerVariant>?,

	@get:Column(name = "answers")
	override var answers: MutableList<AnswerVariant>,
) : Question() {

	constructor() : this(
		id = -1,
		text = "",
		type = QuestionType.TEXT,
		correctAnswers = null,
		answers = Collections.emptyList()
	)

}
