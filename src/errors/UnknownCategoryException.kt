package errors

class UnknownCategoryException(string: String) : Exception("The string ${string} did not match any valid categories")