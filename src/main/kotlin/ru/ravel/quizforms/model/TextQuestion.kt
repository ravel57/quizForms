package ru.ravel.quizforms.model

import jakarta.persistence.*
import ru.ravel.quizforms.converters.TypeConverter
import ru.ravel.quizforms.enums.QuestionSelectedType
import ru.ravel.quizforms.enums.QuestionType
import java.util.*

@Entity
data class TextQuestion(
	@Id
	@get:GeneratedValue(strategy = GenerationType.IDENTITY)
	override var id: Long,

	override var text: String,

	@Convert(converter = TypeConverter::class)
	override var type: QuestionType = QuestionType.TEXT,

	@OneToMany
	@JoinColumn
	override var answers: MutableList<AnswerVariant>,

	override var questionSelectedType: QuestionSelectedType,

	var answer: String,

) : Question() {
	constructor() : this(
		id = -1,
		text = "",
		answers = Collections.emptyList(),
		questionSelectedType = QuestionSelectedType.ANY,
		answer = "",
	)
}
