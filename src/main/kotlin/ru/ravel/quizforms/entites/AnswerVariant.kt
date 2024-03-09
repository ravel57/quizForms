package ru.ravel.quizforms.entites

import jakarta.persistence.*

@Entity
@Table(name = "answer_variant")
data class AnswerVariant(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = -1,

	var text: String = "",

	var isCorrect: Boolean = false,

	var score: Int? = null,
)