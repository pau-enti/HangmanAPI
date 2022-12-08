package com.hangman.api.web

import java.io.*

object WordsLists {

    private const val EN_WORDS_FILE = "words_en.txt"
    private const val CAT_WORDS_FILE = "words_cat.txt"

    enum class Language(val code: String) {
        EN("en"), CAT("cat")
    }

    val english: ArrayList<String>
    val catala: ArrayList<String>

    init {
        val path = System.getProperty("user.dir") + File.separator

        english = loadWordsFromFile(path + EN_WORDS_FILE)
        catala = loadWordsFromFile(path + CAT_WORDS_FILE)
    }

    private fun loadWordsFromFile(file: String): ArrayList<String> {
        val wordList = ArrayList<String>()
        try {
            val br = BufferedReader(FileReader(file))
            var word = br.readLine()
            while (word != null) {
                wordList.add(word)
                word = br.readLine()
            }
            br.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (i: IOException) {
            i.printStackTrace()
        }
        return wordList
    }
}