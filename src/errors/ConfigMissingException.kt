package errors

import config.configFolderPath

class ConfigMissingException(val path: String) : Exception("'$path' missing in config") {
    val title get() = "'$path' missing error!"

    val longMessage get() = """
        Property $message.
        Please add the missing config property to $configFolderPath.
        If you are not sure what this means, please contact the developer.
    """.trimIndent()
}