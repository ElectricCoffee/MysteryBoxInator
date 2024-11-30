import catalogue.Catalogue
import config.defaultConfig
import java.nio.file.Path
import java.nio.file.Paths

object ResourceHelper {
    fun getTestDataPath(): Path {
        val uri = javaClass.getResource("/test-data.csv")?.toURI() ?: throw Exception("couldn't find the file")
        return Paths.get(uri)
    }

    fun getTestCatalogue(): Catalogue {
        val path = getTestDataPath()
        return Catalogue.fromFile(defaultConfig, path, startIndex = 1)
    }
}