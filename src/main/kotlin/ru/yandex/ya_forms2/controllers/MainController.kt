package ru.yandex.ya_forms2.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
@CrossOrigin
class MainController() {


    @CrossOrigin
    @GetMapping("/")
    fun getSurveysPage(): String {
        return "surveysPage"
    }

}