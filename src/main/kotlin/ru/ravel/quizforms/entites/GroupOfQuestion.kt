package ru.ravel.quizforms.entites

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*

@Entity
@Table(name = "question_group")
data class GroupOfQuestion(
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = -1,

	@Column(name = "title")
	var title: String = "",

	@Column(name = "passing_score")
	var passingScore: Int? = null,

	@OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "question_group")
	var questions: MutableList<Question> = Collections.emptyList(),
)