package ru.ravel.quizforms.controllers

import jakarta.servlet.http.HttpSession
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ravel.quizforms.entites.*
import ru.ravel.quizforms.enums.QuestionType
import ru.ravel.quizforms.services.QuestionsService
import java.net.HttpURLConnection


@RestController
@RequestMapping("/api/v1")
@CrossOrigin
class ApiController(
	val questionsService: QuestionsService
) {

	@GetMapping(
		"",
		"/"
	)
	fun getGroupOfQuestion(): ResponseEntity<Any> {
		return ResponseEntity.ok().body(questionsService.getGroupOfQuestion())
	}

	@PostMapping(
		"/answersFromRespondent",
		"/answersFromRespondent/"
	)
	fun getAnswersFromRespondent(
		@RequestBody res: Array<Map<String, Any>>,
		httpSession: HttpSession
	): ResponseEntity<Any> {
		val map = res.map { question: Map<String, Any> ->
			val questions: ArrayList<Question> = ArrayList()
			(question["questions"] as List<*>).forEach {
				when ((it as Map<*, *>)["type"]) {
					QuestionType.TEXT.name -> questions.add(TextQuestion(it))
					QuestionType.CHECKBOX.name -> questions.add(CheckboxQuestion(it))
					QuestionType.RADIO.name -> questions.add(RadioQuestion(it))
				}
			}
			questions.forEach { questionsService.fillCorrectAnswer(it) }
			GroupOfQuestion(
				id = question["id"] as Long,
				title = question["title"] as String,
				passingScore = 0,
				questions = questions
			)
		}
		return ResponseEntity.ok().body(map)
	}


	@PostMapping(
		"/group/",
		"/group"
	)
	fun postGroup(): ResponseEntity<Any> {
		val id = questionsService.createGroup()
		return if (id != null) {
			ResponseEntity.ok().body(mapOf(Pair("id", id)))
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}


	@DeleteMapping(
		"/group/{id}/",
		"/group/{id}"
	)
	fun deleteGroup(
		@PathVariable id: Long
	): ResponseEntity<Any> {
		return if (questionsService.deleteGroup(id)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}


	@PatchMapping(
		"/group/{groupId}/title/",
		"/group/{groupId}/title"
	)
	fun patchGroupTitle(
		@PathVariable groupId: Long,
		@RequestBody body: Map<String, String>,
	): ResponseEntity<Any> {
		return if (questionsService.patchTitle(groupId, body)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}


	@PostMapping(
		"group/{groupId}/question/",
		"group/{groupId}/question"
	)
	fun postQuestion(
		@PathVariable groupId: Long,
		@RequestBody body: Map<String, String>,
	): ResponseEntity<Any> {
		val id = questionsService.createQuestion(groupId, body["type"] ?: "")
		return if (id > 0) {
			ResponseEntity.ok().body(mapOf(Pair("id", id)))
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}


	@PatchMapping(
		"group/{groupId}/question/{questionId}/",
		"group/{groupId}/question/{questionId}"
	)
	fun patchQuestion(
		@PathVariable groupId: Long,
		@PathVariable questionId: Long,
		@RequestBody body: Map<String, String>
	): ResponseEntity<Any> {
		return if (questionsService.questionChanged(groupId, questionId, body)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}


	@DeleteMapping(
		"group/{groupId}/question/{questionId}/",
		"group/{groupId}/question/{questionId}"
	)
	fun deleteQuestion(
		@PathVariable groupId: Long,
		@PathVariable questionId: Long,
	): ResponseEntity<Any> {
		return if (questionsService.deleteQuestion(groupId, questionId)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}


	@PostMapping(
		"/group/{groupId}/question/{questionId}/answer/",
		"/group/{groupId}/question/{questionId}/answer"
	)
	fun postAnswer(
		@PathVariable groupId: String,
		@PathVariable questionId: Long,
	): ResponseEntity<Any> {
		val id = questionsService.postAnswer(questionId)
		return if (id != null) {
			ResponseEntity.ok().body(mapOf(Pair("id", id)))
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}


	@PatchMapping(
		"/group/{groupId}/question/{questionId}/answer/{answerId}/",
		"/group/{groupId}/question/{questionId}/answer/{answerId}"
	)
	fun patchAnswer(
		@PathVariable answerId: Long,
		@PathVariable groupId: String,
		@PathVariable questionId: String,
		@RequestBody body: Map<String, Map<String, Any>>,
	): ResponseEntity<Any> {
		return if (questionsService.patchAnswer(answerId, body)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

	@DeleteMapping(
		"/group/{groupId}/question/{questionId}/answer/{answerId}/",
		"/group/{groupId}/question/{questionId}/answer/{answerId}"
	)
	fun deleteAnswer(
		@PathVariable groupId: Long,
		@PathVariable questionId: Long,
		@PathVariable answerId: Long,
	): ResponseEntity<Any> {
		return if (questionsService.deleteAnswer(answerId)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}


	@PatchMapping(
		"/group/{groupId}/score",
		"/group/{groupId}/score/"
	)
	fun patchGroupScore(
		@PathVariable groupId: Long,
		@RequestBody body: Map<String, Any>,
	): ResponseEntity<Any> {
		return if (questionsService.groupScoreChanged(groupId, body)) {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_NO_CONTENT)).build()
		} else {
			ResponseEntity.of(ProblemDetail.forStatus(HttpURLConnection.HTTP_BAD_REQUEST)).build()
		}
	}

}