package ru.ravel.quizforms.model

import jakarta.persistence.*
import ru.ravel.quizforms.converters.TypeConverter
import ru.ravel.quizforms.enums.QuestionSelectedType
import ru.ravel.quizforms.enums.QuestionType

@Entity
data class RadioQuestion(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	override var id: Long,

	override var text: String,

	@Enumerated(EnumType.ORDINAL)
	@Convert(converter = TypeConverter::class)
	override var type: QuestionType = QuestionType.RADIO,

	@Transient
	var selectedAnswer: AnswerVariant? = null,

	@OneToMany
	@JoinColumn
	override var answers: MutableList<AnswerVariant>,

	override var questionSelectedType: QuestionSelectedType,

	) : Question() {
	constructor() : this(
		id = -1,
		text = "",
		selectedAnswer = null,
		answers = mutableListOf(),
		questionSelectedType = QuestionSelectedType.ANY,
	)

// 	override fun isAnswerCorrect(): Boolean {
// 		return correctAnswer == selectedAnswer
// 	}
}
