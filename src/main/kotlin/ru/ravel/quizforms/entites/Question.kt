package ru.ravel.quizforms.entites

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.ravel.quizforms.converters.TypeConverter
import ru.ravel.quizforms.enums.QuestionType

@Entity
@Table(name = "question")
abstract class Question {

	@get:Id
	@get:Column(name = "id")
	@get:GeneratedValue(strategy = GenerationType.IDENTITY)
	abstract var id: Long

	@get:Column(name = "text")
	abstract var text: String

	@get:Enumerated(EnumType.ORDINAL)
	@get:Convert(converter = TypeConverter::class)
	abstract var type: QuestionType

	@get:OneToMany(cascade = [CascadeType.ALL])
	@get:OnDelete(action = OnDeleteAction.CASCADE)
	@get:JoinColumn(name = "question")
	abstract var answers: MutableList<AnswerVariant>

//	@get:ManyToOne(cascade = [CascadeType.ALL])
//	open var questionGroup: GroupOfQuestion?,

//	abstract fun isAnswerCorrect(): Boolean

}