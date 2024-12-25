package ru.ravel.quizforms.model

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.ravel.quizforms.converters.TypeConverter
import ru.ravel.quizforms.enums.QuestionSelectedType
import ru.ravel.quizforms.enums.QuestionType

@Entity
abstract class Question {
	@get:Id
	@get:GeneratedValue(strategy = GenerationType.IDENTITY)
	abstract var id: Long

	abstract var text: String

	@get:Convert(converter = TypeConverter::class)
	abstract var type: QuestionType

	@get:OneToMany(
		cascade = [CascadeType.ALL],
		orphanRemoval = true,
		fetch = FetchType.EAGER,
		targetEntity = AnswerVariant::class
	)
	@get:JoinColumn
	@get:OnDelete(action = OnDeleteAction.CASCADE)
	abstract var answers: MutableList<AnswerVariant>

	abstract var questionSelectedType: QuestionSelectedType

// 	@get:ManyToOne(cascade = [CascadeType.ALL])
// 	open var questionGroup: GroupOfQuestion?,

// 	abstract fun isAnswerCorrect(): Boolean
}
