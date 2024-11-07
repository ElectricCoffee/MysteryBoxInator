package errors

class UnknownPasteUpsException(value: String)
    : Exception("Did not recognise ${value}. Must be either 'yes', 'no', 'true', or 'false'.")