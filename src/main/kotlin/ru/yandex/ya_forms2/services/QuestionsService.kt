package ru.yandex.ya_forms2.services

import org.springframework.stereotype.Service
import ru.yandex.ya_forms2.models.*
import ru.yandex.ya_forms2.reposetores.QuestionsRepository

@Service
class QuestionsService(
	val questionsRepository: QuestionsRepository
) {

	fun getGroupOfQuestion(): Set<GroupOfQuestion> {
		return questionsRepository.getQuestionGroups()
	}

	fun fillCorrectAnswer(question: Question) {
		val correctAnswers = questionsRepository.getCorrectAnswers(question)
		when (question.type) {
			QuestionType.TEXT -> {
				(question as TextQuestion).correctAnswer = correctAnswers.stream().findFirst().orElse(AnswerVariant(0, "", false, 0))
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
		return questionsRepository.createQuestion(groupId, type)
	}

	fun questionChanged(groupId: Long, questionId: Long, body: Map<String, String>): Boolean {
		val text = body["text"] ?: ""
		return questionsRepository.questionChanged(groupId, questionId, text)
	}

	fun deleteQuestion(groupId: Long, questionId: Long): Boolean {
		return questionsRepository.deleteQuestion(groupId, questionId)
	}

	fun postAnswer(questionId: Long): Long? {
		return questionsRepository.postAnswer(questionId)
	}

	fun patchAnswer(answerId: Long, body: Map<String, Map<String, Any>>): Boolean {
		val map = body["answer"] ?: mapOf()
		val text: String = map["text"].toString()
		val isCorrect: Boolean = map["isCorrect"].toString().toBoolean()
		val score: Int = map["score"] as Int
		return questionsRepository.patchAnswer(answerId, text, isCorrect, score)
	}

	fun deleteAnswer(questionId: Long, answerId: Long): Boolean {
		return questionsRepository.deleteAnswer(questionId, answerId)
	}

}