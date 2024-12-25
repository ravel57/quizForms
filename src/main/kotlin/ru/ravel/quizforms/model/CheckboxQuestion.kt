package ru.ravel.quizforms.model

import jakarta.persistence.*
import ru.ravel.quizforms.converters.TypeConverter
import ru.ravel.quizforms.enums.QuestionSelectedType
import ru.ravel.quizforms.enums.QuestionType

@Entity
data class CheckboxQuestion(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	override var id: Long,

	override var text: String,

	@Convert(converter = TypeConverter::class)
	override var type: QuestionType = QuestionType.CHECKBOX,

	@OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], orphanRemoval = true)
	@JoinColumn
	override var answers: MutableList<AnswerVariant>,

	@Transient
	var selectedAnswers: List<AnswerVariant>?,

	override var questionSelectedType: QuestionSelectedType,
) : Question() {
	constructor() : this(
		id = -1,
		text = "",
		selectedAnswers = null,
		answers = mutableListOf(),
		questionSelectedType = QuestionSelectedType.ANY,
	)

// 	override fun isAnswerCorrect(): Boolean {
// 		return selectedAnswers!!.any { it.id in correctAnswers!!.map { answerVariant -> answerVariant.id } }
// 	}
}
