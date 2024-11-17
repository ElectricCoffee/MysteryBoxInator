package io

import catalogue.Catalogue
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

class Filing {
    companion object {
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

            val date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            val outputFile = Paths.get(outputDir.toString() + File.separator + "catalogue-" + date + ".backup.csv")

            ensureOutputDir(config)

            // writes a timestamped backup of the current catalogue
            Files.write(outputFile, catalogue.toCsv(true), StandardOpenOption.CREATE)
        }

        @JvmStatic
        @Throws(IOException::class)
        fun writeWorkingCopy(config: Config, catalogue: Catalogue) {
            val outputDir: Path = Paths.get(config.io.outputDirectory)

            val outputFile = Paths.get(outputDir.toString() + File.separator + "catalogue.working-copy.csv")

            ensureOutputDir(config)

            // writes a timestamped backup of the current catalogue
            Files.write(
                outputFile,
                catalogue.toCsv(true),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        }
    }
}