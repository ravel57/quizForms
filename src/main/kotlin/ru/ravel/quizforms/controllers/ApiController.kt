package ru.ravel.quizforms.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ravel.quizforms.enums.QuestionType
import ru.ravel.quizforms.model.*
import ru.ravel.quizforms.services.QuestionService
import java.net.HttpURLConnection


@RestController
@RequestMapping("/api/v1")
@CrossOrigin
class ApiControllerImpl(
	private val questionService: QuestionService,
) {
	private val objectMapper = ObjectMapper()

	@GetMapping("")
	fun getGroupOfQuestion(): ResponseEntity<Any> {
		return ResponseEntity.ok().body(questionService.getGroupOfQuestion())
	}

	@PostMapping("/answersFromRespondent")
	fun getAnswersFromRespondent(
		@RequestBody res: Array<Map<String, Any>>,
	): ResponseEntity<Any> {
		val map = res.map { question ->
			val questions: ArrayList<Question> = ArrayList()
			(question["questions"] as List<*>).forEach {
				when ((it as Map<*, *>)["type"]) {
					QuestionType.TEXT.name -> {
						questions.add(
							objectMapper.readValue(
								objectMapper.writeValueAsString(it),
								TextQuestion::class.java
							)
						)
					}

					QuestionType.CHECKBOX.name -> {
						questions.add(
							objectMapper.readValue(
								objectMapper.writeValueAsString(it),
								CheckboxQuestion::class.java
							)
						)
					}

					QuestionType.RADIO.name ->
						questions.add(
							objectMapper.readValue(
								objectMapper.writeValueAsString(it),
								RadioQuestion::class.java
							)
						)
				}
			}
			GroupOfQuestion(
				id = question["id"].toString().toLong(),
				title = question["title"] as String,
				passingScore = question["passingScore"] as Int?,
				questions = questions
			)
		}
		return ResponseEntity.ok().body(map)
	}

	@PostMapping("/group")
	fun postGroup(): ResponseEntity<Any> {
		val id = questionService.createGroup()
		return if (id != null) {
			ResponseEntity.ok().body(mapOf(Pair("id", id)))
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

	@DeleteMapping("/group/{id}")
	fun deleteGroup(
		@PathVariable id: Long,
	): ResponseEntity<Any> {
		return if (questionService.deleteGroup(id)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

	@PatchMapping("/group/{groupId}/title")
	fun patchGroupTitle(
		@PathVariable groupId: Long,
		@RequestBody body: Map<String, String>,
	): ResponseEntity<Any> {
		return if (questionService.patchTitle(groupId, body)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

	@PostMapping("group/{groupId}/question")
	fun postQuestion(
		@PathVariable groupId: Long,
		@RequestBody body: Map<String, String>,
	): ResponseEntity<Any> {
		val id = questionService.createQuestion(groupId, body["type"] ?: "")
		return if (id > 0) {
			ResponseEntity.ok().body(mapOf(Pair("id", id)))
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

	@PatchMapping("group/{groupId}/question/{questionId}")
	fun patchQuestion(
		@PathVariable groupId: Long,
		@PathVariable questionId: Long,
		@RequestBody body: Map<String, String>,
	): ResponseEntity<Any> {
		return if (questionService.questionChanged(groupId, questionId, body)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

	@DeleteMapping("group/{groupId}/question/{questionId}")
	fun deleteQuestion(
		@PathVariable groupId: Long,
		@PathVariable questionId: Long,
	): ResponseEntity<Any> {
		return if (questionService.deleteQuestion(groupId, questionId)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

	@PostMapping("/group/{groupId}/question/{questionId}/answer")
	fun postAnswer(
		@PathVariable groupId: String,
		@PathVariable questionId: Long,
	): ResponseEntity<Any> {
		val id = questionService.postAnswer(questionId)
		return if (id != null) {
			ResponseEntity.ok().body(mapOf(Pair("id", id)))
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

	@PatchMapping("/group/{groupId}/question/{questionId}/answer/{answerId}")
	fun patchAnswer(
		@PathVariable answerId: Long,
		@PathVariable groupId: Long,
		@PathVariable questionId: Long,
		@RequestBody body: Map<String, Map<String, Any>>,
	): ResponseEntity<Any> {
		return if (questionService.patchAnswer(questionId, answerId, body)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

	@DeleteMapping("/group/{groupId}/question/{questionId}/answer/{answerId}")
	fun deleteAnswer(
		@PathVariable groupId: Long,
		@PathVariable questionId: Long,
		@PathVariable answerId: Long,
	): ResponseEntity<Any> {
		return if (questionService.deleteAnswer(answerId)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

	@PatchMapping("/group/{groupId}/score")
	fun patchGroupScore(
		@PathVariable groupId: Long,
		@RequestBody body: Map<String, Int>,
	): ResponseEntity<Any> {
		return if (questionService.groupScoreChanged(groupId, body)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

	@PatchMapping("/group/{groupId}/question/{questionId}/correct-answer-type-changed/any")
	fun correctAnswerTypeChangedAny(
		@PathVariable groupId: Long,
		@PathVariable questionId: Long,
	): ResponseEntity<Any> {
		return if (questionService.correctAnswerTypeChangedAny(questionId)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

	@PatchMapping("/group/{groupId}/question/{questionId}/correct-answer-type-changed/all")
	fun correctAnswerTypeChangedAll(
		@PathVariable groupId: Long,
		@PathVariable questionId: Long,
	): ResponseEntity<Any> {
		return if (questionService.correctAnswerTypeChangedAll(questionId)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}
}
