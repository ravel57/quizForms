package ru.ravel.quizforms.entites

import jakarta.persistence.Id

data class Answer(

	@Id
	var id: Long = -1,

	var text: String = "",
)