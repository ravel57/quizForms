package ru.ravel.quizforms.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class AnswerVariant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(length = 16384)
    var text: String,

    var correct: Boolean,

    var score: Int?,

) {
    constructor() : this(
        id = -1,
        text = "",
        correct = false,
        score = 0,
    )
}
