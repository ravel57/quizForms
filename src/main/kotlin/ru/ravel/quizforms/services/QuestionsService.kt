package ru.ravel.quizforms.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import ru.ravel.quizforms.enums.QuestionType
import ru.ravel.quizforms.enums.QuestionSelectedType
import ru.ravel.quizforms.model.AnswerVariant
import ru.ravel.quizforms.model.CheckboxQuestion
import ru.ravel.quizforms.model.GroupOfQuestion
import ru.ravel.quizforms.model.Question
import ru.ravel.quizforms.model.RadioQuestion
import ru.ravel.quizforms.model.TextQuestion
import ru.ravel.quizforms.reposetores.AnswerVariantRepository
import ru.ravel.quizforms.reposetores.QuestionGroupRepository
import ru.ravel.quizforms.reposetores.QuestionRepository

@Service
class QuestionService(
	private val questionGroupRepository: QuestionGroupRepository,
	private val questionRepository: QuestionRepository,
	private val answerVariantRepository: AnswerVariantRepository,
) {
	private val objectMapper = ObjectMapper()

	fun getGroupOfQuestion(): MutableIterable<GroupOfQuestion> {
		return questionGroupRepository.findAll()
	}

	fun createGroup(): Long? {
		return questionGroupRepository.save(GroupOfQuestion()).id
	}

	fun patchTitle(groupId: Long, body: Map<String, String>): Boolean {
		val newTitle = body["newTitle"] ?: ""
		val groupOfQuestion = questionGroupRepository.findById(groupId).orElseThrow()
		groupOfQuestion.title = newTitle
		questionGroupRepository.save(groupOfQuestion)
		return true
	}

	fun deleteGroup(id: Long): Boolean {
		val groupOfQuestion = questionGroupRepository.findById(id).orElseThrow()
		groupOfQuestion.questions.forEach { question ->
			question.answers.forEach { answerVariantRepository.delete(it) }
			questionRepository.delete(question)
		}
		questionGroupRepository.delete(groupOfQuestion)
		return true
	}

	fun createQuestion(groupId: Long, type: String): Long {
		val question: Question = when (QuestionType.valueOf(type.uppercase())) {
			QuestionType.TEXT -> TextQuestion()
			QuestionType.RADIO -> RadioQuestion()
			QuestionType.CHECKBOX -> CheckboxQuestion()
		}
		questionRepository.save(question)
		val groupOfQuestion = questionGroupRepository.findById(groupId).orElseThrow()
		groupOfQuestion.questions.add(question)
		questionGroupRepository.save(groupOfQuestion)
		return groupOfQuestion.id
	}

	fun questionChanged(groupId: Long, questionId: Long, body: Map<String, String>): Boolean {
		val text = body["text"] ?: ""
		val question = questionGroupRepository.findById(groupId).orElseThrow().questions.find { it.id == questionId }!!
		question.text = text
		questionRepository.save(question)
		return true
	}

	fun deleteQuestion(groupId: Long, questionId: Long): Boolean {
		val question = questionRepository.findById(questionId).orElseThrow()
		question.answers.forEach { answerVariantRepository.delete(it) }
		questionRepository.delete(question)
		return true
	}

	fun postAnswer(questionId: Long): Long? {
		val answerVariant = AnswerVariant()
		answerVariantRepository.save(answerVariant)
		val question = questionRepository.findById(questionId).orElseThrow()
		question.answers.add(answerVariant)
		questionRepository.save(question)
		return answerVariant.id
	}

	fun patchAnswer(questionId: Long, answerId: Long, body: Map<String, Map<String, Any>>): Boolean {
		val answerVariant =
			objectMapper.readValue(objectMapper.writeValueAsString(body["answer"]), AnswerVariant::class.java)
		answerVariantRepository.save(answerVariant)
		questionRepository.findById(questionId).orElseThrow().answers.add(answerVariant)
		return true
	}

	fun deleteAnswer(answerId: Long): Boolean {
		answerVariantRepository.deleteById(answerId)
		return true
	}

	fun groupScoreChanged(groupId: Long, body: Map<String, Int>): Boolean {
		val groupOfQuestion = questionGroupRepository.findById(groupId).orElseThrow()
		groupOfQuestion.passingScore = body["score"]
		questionGroupRepository.save(groupOfQuestion)
		return true
	}

	fun correctAnswerTypeChangedAny(questionId: Long): Boolean {
		val question = questionRepository.findById(questionId).orElseThrow()
		question.questionSelectedType = QuestionSelectedType.ANY
		questionRepository.save(question)
		return true
	}

	fun correctAnswerTypeChangedAll(questionId: Long): Boolean {
		val question = questionRepository.findById(questionId).orElseThrow()
		question.questionSelectedType = QuestionSelectedType.ALL
		questionRepository.save(question)
		return true
	}
}