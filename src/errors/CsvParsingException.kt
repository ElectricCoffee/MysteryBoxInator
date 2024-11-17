package errors

class CsvParsingException(chunks: List<String>) : Exception("Parsing failed because the csv contained incorrect data. Got $chunks") {
}