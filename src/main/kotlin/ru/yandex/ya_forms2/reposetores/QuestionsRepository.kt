package ru.yandex.ya_forms2.reposetores

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import ru.yandex.ya_forms2.models.*
import java.sql.Connection
import java.sql.Statement


@Repository
class QuestionsRepository(
	@Autowired
	private val jdbcTemplate: JdbcTemplate,
) {
	//TODO поменять на хибернейт

	fun getQuestionGroups(): Set<GroupOfQuestion> {
		val groupOfQuestion: MutableSet<GroupOfQuestion> = mutableSetOf()
		val groups: List<Map<String, Any>> = jdbcTemplate.queryForList("SELECT * FROM question_group ORDER BY id")
		for (group in groups) {
			val questions = jdbcTemplate.queryForList(
				"""
				|SELECT 
				|    q.id as id,
				|    q.group_id as groupId,
				|    q.text as text,
				|    qt.id, 
				|    qt.name as type
				|FROM question as q
				|JOIN question_type as qt ON qt.id = q.type
				|WHERE group_id = ${group["id"]}
				|ORDER BY q.id
				""".trimMargin()
			).map {
				it["groupTitle"] = group["title"]
				when (it["type"]) {
					QuestionType.TEXT.name -> TextQuestion(it)
					QuestionType.CHECKBOX.name -> CheckboxQuestion(it)
					QuestionType.RADIO.name -> RadioQuestion(it)
					else -> throw RuntimeException()
				}
			}.toSet()
			for (question in questions) {
				jdbcTemplate.queryForList(
					"""
					|SELECT id, text, question_id, is_correct, score
					|FROM answer
					|WHERE question_id = ${question.id}
					|ORDER BY id
					""".trimMargin()
				).map {
					when (question.type) {
						QuestionType.TEXT -> (question as TextQuestion).answer = it["text"].toString()

						QuestionType.CHECKBOX -> (question as CheckboxQuestion).answers?.add(
							AnswerVariant(
								id = it["id"] as Int,
								text = it["text"].toString(),
								isCorrect = it["is_correct"] as Boolean,
								score = (it["score"] ?: 0) as Int,
							)
						)

						QuestionType.RADIO -> (question as RadioQuestion).answers?.add(
							AnswerVariant(
								id = it["id"] as Int,
								text = it["text"].toString(),
								isCorrect = it["is_correct"] as Boolean,
								score = (it["score"] ?: 0) as Int,
							)
						)
					}
				}
			}
			groupOfQuestion.add(
				GroupOfQuestion(
					id = group["id"] as Int,
					title = group["title"] as String,
					questions = questions
				)
			)
		}

		return groupOfQuestion
	}


	fun getCorrectAnswers(question: Question): List<AnswerVariant> {
		return jdbcTemplate.queryForList(
			"""
			|SELECT text, id, is_correct, score
            |FROM answer
            |WHERE question_id = ${question.id} AND is_correct
			""".trimMargin()
		).map {
			AnswerVariant(
				id = it["id"] as Int,
				text = it["text"] as String,
				isCorrect = it["is_correct"] as Boolean,
				score = (it["score"] ?: 0) as Int,
			)
		}
	}


	fun createGroup(): Long? {
		val keyHolder: KeyHolder = GeneratedKeyHolder()
		jdbcTemplate.update({ connection: Connection ->
			connection.prepareStatement(
				"INSERT INTO question_group (title) values ('')",
				Statement.RETURN_GENERATED_KEYS
			)
		}, keyHolder)
		return keyHolder.keys?.get("id").toString().toLong()
	}


	fun patchTitle(groupId: Long, newTitle: String): Boolean {
		return jdbcTemplate.update("UPDATE question_group SET title = '$newTitle' WHERE id = $groupId") == 1
	}


	fun deleteGroup(id: Long): Boolean {
		return jdbcTemplate.update("DELETE FROM question_group WHERE id = $id") == 1
	}


	fun createQuestion(groupId: Long, type: String): Long {
		val keyHolder: KeyHolder = GeneratedKeyHolder()
		jdbcTemplate.update({ connection: Connection ->
			connection.prepareStatement(
				"""
					INSERT INTO question (group_id, text, type) 
					VALUES ($groupId, '', (SELECT id from question_type WHERE name = '$type'))""",
				Statement.RETURN_GENERATED_KEYS
			)
		}, keyHolder)
		return keyHolder.keys?.get("id").toString().toLong()
	}

	fun questionChanged(groupId: Long, questionId: Long, text: String): Boolean {
		return jdbcTemplate.update("UPDATE question SET text = '$text' WHERE id = $questionId AND group_id = $groupId") == 1
	}

	fun deleteQuestion(groupId: Long, questionId: Long): Boolean {
		return jdbcTemplate.update("DELETE FROM question WHERE id = $questionId AND group_id = $groupId") == 1
	}

	fun postAnswer(questionId: Long): Long? {
		val keyHolder: KeyHolder = GeneratedKeyHolder()
		jdbcTemplate.update({ connection: Connection ->
			connection.prepareStatement(
				"INSERT INTO answer (question_id, text, is_correct) VALUES ($questionId, '', false)",
				Statement.RETURN_GENERATED_KEYS
			)
		}, keyHolder)
		return keyHolder.keys?.get("id").toString().toLong()
	}

	fun patchAnswer(answerId: Long, text: String, isCorrect: Boolean, score: Int): Boolean {
		return jdbcTemplate.update(
			"""
			|UPDATE answer 
			|SET text = '$text', is_correct	= $isCorrect, score = $score
			|WHERE id = $answerId
			""".trimMargin()
		) == 1
	}

	fun deleteAnswer(questionId: Long, answerId: Long): Boolean {
		return jdbcTemplate.update("DELETE FROM answer WHERE question_id = $questionId AND id = $answerId") == 1
	}

}