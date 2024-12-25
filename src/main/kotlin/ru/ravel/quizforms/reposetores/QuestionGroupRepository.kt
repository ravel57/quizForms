package ru.ravel.quizforms.reposetores

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.ravel.quizforms.model.GroupOfQuestion

@Repository
interface QuestionGroupRepository : JpaRepository<GroupOfQuestion, Long>
