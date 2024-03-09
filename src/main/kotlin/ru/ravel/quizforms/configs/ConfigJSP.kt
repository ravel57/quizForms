package ru.ravel.quizforms.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.springframework.web.servlet.view.JstlView

@Configuration
class ConfigJSP {

    @Bean
    fun getViewResolver(): ViewResolver {
        val resolver = InternalResourceViewResolver()
        resolver.setPrefix("/WEB-INF/views/")
        resolver.setSuffix(".jsp")
        resolver.setViewClass(JstlView::class.java)
        return resolver
    }



}