package com.hangman.api.web

import java.io.*
import java.text.Normalizer

object WordsLists {

    private const val EN_WORDS_FILE = "words_en.txt"
    private const val CAT_WORDS_FILE = "words_cat.txt"
    private const val ES_WORDS_FILE = "words_es_complete.txt"

    enum class Language(val code: String) {
        EN("en"), CAT("cat"), ES("es")
    }

    val english: ArrayList<String>
    val catala: ArrayList<String>
    val spanish: ArrayList<String>

    init {
        val path = System.getProperty("user.dir") + File.separator

        english = loadWordsFromFile(path + EN_WORDS_FILE)
        catala = loadWordsFromFile(path + CAT_WORDS_FILE)
        spanish = loadWordsFromFile(path + ES_WORDS_FILE)
    }

    private fun loadWordsFromFile(file: String): ArrayList<String> {
        val collection = ArrayList<String>()
        try {
            BufferedReader(FileReader(file)).use {
                var word = it.readLine()
                while (word != null) {
                    collection.add(word)
                    word = it.readLine()
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return collection
    }

    fun CharSequence.unaccent(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return "\\p{InCombiningDiacriticalMarks}+".toRegex().replace(temp, "")
    }
}