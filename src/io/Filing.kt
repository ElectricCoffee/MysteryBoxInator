package io

import catalogue.Catalogue
import catalogue.CsvLoadMode
import config.Config
import config.Config.Companion.fromFile
import config.configFilePath
import config.configFolderPath
import config.defaultConfigString
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Filing {
    @JvmStatic
    @Throws(IOException::class)
    fun ensureConfigDir() {
        val configFolder: Path = Paths.get(configFolderPath)

        if (!Files.exists(configFolder)) {
            Files.createDirectory(configFolder)
        }
    }

    @JvmStatic
    @Throws(IOException::class)
    fun ensureOutputDir(config: Config) {
        val outputFolder: Path = Paths.get(config.io.outputDirectory)

        if (!Files.exists(outputFolder)) {
            Files.createDirectory(outputFolder)
        }
    }

    @JvmStatic
    @Throws(IOException::class)
    fun createConfig(): Config {
        val configFile: Path = Paths.get(configFilePath)

        ensureConfigDir()

        if (!Files.exists(configFile)) {
            Files.createFile(configFile)

            Files.write(configFile, defaultConfigString.toByteArray())
        }

        return fromFile(configFile)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun backupCatalogue(config: Config, catalogue: Catalogue) {
        val outputDir: Path = Paths.get(config.io.outputDirectory)

        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH.mm.ss"))

        val outputFile = Paths.get(outputDir.toString() + File.separator + "catalogue-" + date + ".backup.csv")

        ensureOutputDir(config)

        // writes a timestamped backup of the current catalogue
        Files.write(outputFile, catalogue.toCsv(true), StandardOpenOption.CREATE)
    }

    private fun catalogueWorkingCopyFile(config:Config) = Paths.get(config.io.outputDirectory + File.separator + "catalogue.working-copy.csv")

    @JvmStatic
    @Throws(IOException::class)
    fun deleteWorkingCopy(config: Config) {
        Files.delete(catalogueWorkingCopyFile(config))
    }

    @JvmStatic
    @Throws(IOException::class)
    fun readWorkingCopy(config: Config): Catalogue {
        val inputFile = catalogueWorkingCopyFile(config)

        val catalogue = Catalogue(config)

        if (Files.exists(inputFile)) {
            catalogue.appendFromFile(inputFile, CsvLoadMode.OVERWRITE)
        }

        return catalogue
    }

    @JvmStatic
    @Throws(IOException::class)
    fun writeWorkingCopy(config: Config, catalogue: Catalogue) {
        val outputFile = catalogueWorkingCopyFile(config)

        ensureOutputDir(config)

        // writes a timestamped backup of the current catalogue
        Files.write(
            outputFile,
            catalogue.toCsv(false),
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        )
    }
}
