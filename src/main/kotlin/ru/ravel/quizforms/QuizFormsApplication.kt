package ru.ravel.quizforms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
class QuizFormsApplication

//TODO переделать на netty
//TODO разбить тесты по ролям
//TODO ассинхронность, реактивность

fun main(args: Array<String>) {
	runApplication<QuizFormsApplication>(*args)
}
