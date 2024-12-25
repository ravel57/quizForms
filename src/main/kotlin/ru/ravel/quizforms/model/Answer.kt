package ru.ravel.quizforms.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Answer(
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long,

	var text: String,
) {
    constructor() : this(
        id = -1,
        text = ""
    )
}
