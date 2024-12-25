package ru.ravel.quizforms.converters

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ru.ravel.quizforms.enums.QuestionType

@Converter(autoApply = true)
class TypeConverter : AttributeConverter<QuestionType, Int> {

	override fun convertToDatabaseColumn(attribute: QuestionType?): Int? {
		return attribute?.ordinal
	}

	@OptIn(ExperimentalStdlibApi::class)
	override fun convertToEntityAttribute(dbData: Int?): QuestionType? {
		return dbData?.let { QuestionType.entries[it] }
	}
}
