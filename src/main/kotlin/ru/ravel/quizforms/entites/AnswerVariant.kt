package ru.ravel.quizforms.entites

import jakarta.persistence.*

@Entity
@Table(name = "answer_variant")
data class AnswerVariant(

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = -1,

	@Column(name="text", length = 1024)
	var text: String = "",

	@Column(name="correct")
	var correct: Boolean = false,

	@Column(name="score")
	var score: Int? = null,
)