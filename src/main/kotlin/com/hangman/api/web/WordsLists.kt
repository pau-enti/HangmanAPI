package com.hangman.api.web

import java.io.*
import java.text.Normalizer

object WordsLists {

    private const val EN_WORDS_FILE = "/words_en.txt"
    private const val CAT_WORDS_FILE = "/words_cat.txt"
    private const val ES_WORDS_FILE = "/words_es.txt"

    enum class Language(val code: String) {
        EN("en"), CAT("cat"), ES("es")
    }

    val english: List<String>
    val catala: List<String>
    val spanish: List<String>

    init {
        english = loadWordsFromFile(javaClass.getResourceAsStream(EN_WORDS_FILE))
        catala = loadWordsFromFile(javaClass.getResourceAsStream(CAT_WORDS_FILE))
        spanish = loadWordsFromFile(javaClass.getResourceAsStream(ES_WORDS_FILE))
    }

    private fun loadWordsFromFile(file: InputStream?): List<String> {
        try {
            if (file == null)
                throw FileNotFoundException()

            file.bufferedReader().use {
                return it.readLines()
            }
        } catch (e: FileNotFoundException) {
            System.err.println("File not found: $file")
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return emptyList() // on error
    }

    fun CharSequence.unaccent(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return "\\p{InCombiningDiacriticalMarks}+".toRegex().replace(temp, "")
    }
}