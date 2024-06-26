package ru.ravel.quizforms.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import ru.ravel.quizforms.enums.QuestionType
import ru.ravel.quizforms.entites.*
import ru.ravel.quizforms.entites.CheckboxQuestion
import ru.ravel.quizforms.entites.Question
import ru.ravel.quizforms.entites.RadioQuestion
import ru.ravel.quizforms.entites.TextQuestion
import ru.ravel.quizforms.reposetores.QuestionsRepository

@Service
class QuestionsService(
	val questionsRepository: QuestionsRepository
) {

	fun getGroupOfQuestion(): List<GroupOfQuestion> {
		return questionsRepository.getQuestionGroups()
	}

	fun fillCorrectAnswer(question: Question) {
		val correctAnswers = questionsRepository.getCorrectAnswers(question)
		when (question.type) {
			QuestionType.TEXT -> {
				(question as TextQuestion).correctAnswers = correctAnswers
			}

			QuestionType.RADIO -> {
				(question as RadioQuestion).correctAnswer = correctAnswers[0]
			}

			QuestionType.CHECKBOX -> {
				(question as CheckboxQuestion).correctAnswers = correctAnswers
			}
		}
	}

	fun createGroup(): Long? {
		return questionsRepository.createGroup()
	}

	fun patchTitle(groupId: Long, body: Map<String, String>): Boolean {
		val newTitle = body["newTitle"] ?: ""
		return questionsRepository.patchTitle(groupId, newTitle)
	}

	fun deleteGroup(id: Long): Boolean {
		return questionsRepository.deleteGroup(id)
	}

	fun createQuestion(groupId: Long, type: String): Long {
		return questionsRepository.createQuestion(groupId, QuestionType.valueOf(type.uppercase()))
	}

	fun questionChanged(groupId: Long, questionId: Long, body: Map<String, String>): Boolean {
		val text = body["text"] ?: ""
		return questionsRepository.questionChanged(questionId, text)
	}

	fun deleteQuestion(groupId: Long, questionId: Long): Boolean {
		return questionsRepository.deleteQuestion(groupId, questionId)
	}

	fun postAnswer(questionId: Long): Long? {
		return questionsRepository.postAnswer(questionId)
	}

	fun patchAnswer(answerId: Long, body: Map<String, Map<String, Any>>): Boolean {
		val objectMapper = ObjectMapper()
		val answerVariant = objectMapper.readValue(objectMapper.writeValueAsString(body["answer"]), AnswerVariant::class.java)
		return questionsRepository.patchAnswer(answerId, answerVariant)
	}

	fun deleteAnswer(answerId: Long): Boolean {
		return questionsRepository.deleteAnswer(answerId)
	}

	fun groupScoreChanged(groupId: Long, body: Map<String, Any>): Boolean {
		return questionsRepository.groupScoreChanged(groupId, body["score"] as Int)
	}

}