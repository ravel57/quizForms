package ru.ravel.quizforms.reposetores

import org.springframework.data.jpa.repository.JpaRepository
import ru.ravel.quizforms.model.Question

interface QuestionRepository : JpaRepository<Question, Long>
