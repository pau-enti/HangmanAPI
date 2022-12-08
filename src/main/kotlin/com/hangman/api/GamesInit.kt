package com.hangman.api

import java.io.*

internal class GamesInit {
    val wordList: ArrayList<String>

    init {
        val fpath = System.getProperty("user.dir") + File.separator + "bogwords.txt"
        wordList = createWordList(fpath)
    }

    private fun createWordList(file: String): ArrayList<String> {
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