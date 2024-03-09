package ru.ravel.quizforms.converters

import jakarta.persistence.AttributeConverter
import ru.ravel.quizforms.enums.QuestionType

class TypeConverter : AttributeConverter<QuestionType, Int> {

	override fun convertToDatabaseColumn(attribute: QuestionType?): Int {
		return QuestionType.values().withIndex().find { it.value == attribute }?.index ?: -1
	}

	override fun convertToEntityAttribute(dbData: Int?): QuestionType? {
		return QuestionType.values().find { it.ordinal == dbData }
	}

}