package ru.ravel.quizforms.reposetores

import jakarta.persistence.EntityManagerFactory
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.ravel.quizforms.entites.*
import ru.ravel.quizforms.enums.QuestionType


@Repository
class QuestionsRepository(
	@Autowired
	private val jdbcTemplate: JdbcTemplate,

	@Autowired
	private val factory: EntityManagerFactory,
) {

	private val sessionFactory: SessionFactory = factory.unwrap(SessionFactory::class.java)

	fun getQuestionGroups(): List<GroupOfQuestion> {
		val criteria = sessionFactory.openSession().criteriaBuilder
		val query = criteria.createQuery(GroupOfQuestion::class.java)
		val from = query.from(GroupOfQuestion::class.java)
		query.select(from)
		query.orderBy(criteria.asc(from.get<Long>("id")))
		return sessionFactory.openSession().createQuery(query).resultList
	}


	fun getCorrectAnswers(question: Question): List<AnswerVariant> {
		return jdbcTemplate.queryForList(
			"""
			|SELECT text, id, is_correct, score
            |FROM answer_variant
            |WHERE question_id = ${question.id} AND is_correct
			""".trimMargin()
		).map {
			AnswerVariant(
				id = it["id"] as Long,
				text = it["text"] as String,
				correct = it["correct"] as Boolean,
				score = (it["score"] ?: 0) as Int,
			)
		}
	}


	fun createGroup(): Long? {
		val groupOfQuestion = GroupOfQuestion()
		val session = sessionFactory.openSession()
		session.beginTransaction()
		session.persist(groupOfQuestion)
		session.transaction.commit()
		session.close()
		return groupOfQuestion.id
	}


	fun patchTitle(groupId: Long, newTitle: String): Boolean {
		val session = sessionFactory.openSession()
		val groupOfQuestion = session.get(GroupOfQuestion::class.java, groupId)
		session.beginTransaction()
		groupOfQuestion.title = newTitle
		session.merge(groupOfQuestion)
		session.transaction.commit()
		session.close()
		return true
	}


	fun deleteGroup(id: Long): Boolean {
		val session = sessionFactory.openSession()
		session.beginTransaction()
		session.remove(session.get(GroupOfQuestion::class.java, id))
		session.transaction.commit()
		session.close()
		return true
	}


	fun createQuestion(groupId: Long, type: QuestionType): Long {
		val question: Question = when (type) {
			QuestionType.TEXT -> TextQuestion()
			QuestionType.CHECKBOX -> CheckboxQuestion()
			QuestionType.RADIO -> RadioQuestion()
		}
		val session = sessionFactory.openSession()
		val groupOfQuestion = session.get(GroupOfQuestion::class.java, groupId)
		val transaction = session.beginTransaction()
		session.save(question)
		groupOfQuestion.questions.add(question)
		transaction.commit()
		session.close()
		return question.id
	}

	fun questionChanged(questionId: Long, text: String): Boolean {
		val session = sessionFactory.openSession()
		val question = session.get(Question::class.java, questionId)
		session.beginTransaction()
		question?.text = text
		session.merge(question)
		session.transaction.commit()
		session.close()
		return true
	}

	fun deleteQuestion(groupId: Long, questionId: Long): Boolean {
		val session = sessionFactory.openSession()
		session.beginTransaction()
		session.remove(session.get(Question::class.java, questionId))
		session.transaction.commit()
		session.close()
		return true
	}

	fun postAnswer(questionId: Long): Long? {
		val answer = AnswerVariant()
		val session = sessionFactory.openSession()
		val question = session.get(Question::class.java, questionId)
		session.beginTransaction()
		session.save(answer)
		question.answers.add(answer)
		session.transaction.commit()
		session.close()
		return answer.id
	}

	fun patchAnswer(answerId: Long, newAnswerVariant: AnswerVariant): Boolean {
		val session = sessionFactory.openSession()
		val answerVariant = session.get(AnswerVariant::class.java, answerId)
		session.beginTransaction()
		answerVariant?.text = newAnswerVariant.text
		answerVariant?.correct = newAnswerVariant.correct
		answerVariant?.score = newAnswerVariant.score
		session.merge(answerVariant)
		session.transaction.commit()
		session.close()
		return true
	}

	fun deleteAnswer(answerId: Long): Boolean {
		val session = sessionFactory.openSession()
		session.beginTransaction()
		session.remove(session.get(AnswerVariant::class.java, answerId))
		session.transaction.commit()
		session.close()
		return true
	}

	fun groupScoreChanged(groupId: Long, score: Int): Boolean {
		val session = sessionFactory.openSession()
		session.beginTransaction()
		val groupOfQuestion = session.get(GroupOfQuestion::class.java, groupId)
		groupOfQuestion.passingScore = score
		session.merge(groupOfQuestion)
		session.transaction.commit()
		session.close()
		return true
	}

}