package com.hangman.api.web

import com.hangman.api.exception.GameDoesNotExistException
import com.hangman.api.exception.GameOverException
import com.hangman.api.exception.InvalidCharacterException
import com.hangman.api.models.*
import com.hangman.api.models.io.GuessInput
import com.hangman.api.models.io.GuessOutput
import com.hangman.api.models.io.HintOutput
import com.hangman.api.models.io.NewGameOutput
import com.hangman.api.web.WordsLists.Language.*
import jakarta.servlet.ServletContext
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
internal class GamesController {

    /**
     * Get list of active games in session
     */
    @GetMapping("/games")
    fun getGameList(): List<Game> = ActiveGames


    /**
     * Get a game from session
     */
    @GetMapping("/game")
    fun getGame(@RequestParam token: String): Game {
        val games = getGameList()

        for (game in games) {
            if (game.token == token)
                return game
        }
        throw GameDoesNotExistException(token)
    }

    @GetMapping("/hangman")
    fun getGame_compatibility(@RequestParam token: String): Game = getGame(token)

    /**
     * Create new game
     */
    @GetMapping("/new")
    fun startGame(@RequestParam lang: String?, @RequestParam maxTries: Int?): NewGameOutput {
        val languageCode = WordsLists.Language.values().find { lang == it.code } ?: EN
        val words = when (languageCode) {
            EN -> WordsLists.english
            ES -> WordsLists.spanish
            CAT -> WordsLists.catala
        }

        val newGame = Game(words, languageCode, maxTries)
        ActiveGames.add(newGame)

        return NewGameOutput(newGame)
    }


    @PostMapping("/hangman")
    fun startGame_compatible(
        @RequestParam lang: String?,
        @RequestParam maxTries: Int?,
        session: HttpSession
    ): NewGameOutput = startGame(lang, maxTries)


    /**
     * Make guess
     */
    @PostMapping("/guess")
    fun makeGuess(@RequestBody guess: GuessInput): ResponseEntity<*> {
        val gameId = guess.token ?: throw GameDoesNotExistException(null)
        val letterInput = guess.letter ?: throw InvalidCharacterException(null)

        val game = getGame(gameId)
        if (game.status == Game.Status.WON || game.status == Game.Status.LOST)
            throw GameOverException(game.status)

        val isCorrect = game.guessLetter(letterInput)
        return ResponseEntity(GuessOutput(game.token, game.hangman, game.incorrectGuesses, isCorrect), HttpStatus.OK)
    }

    @PutMapping("/hangman")
    fun makeGuess_compatiblity(
        @RequestParam token: String,
        @RequestParam letter: String
    ): ResponseEntity<*> =
        makeGuess(GuessInput(token, letter))

    /**
     * Get a hint
     */
    @GetMapping("/hint")
    fun giveMeAHint(@RequestParam token: String): HintOutput {
        val game = getGame(token)
        return HintOutput(game.token, game.giveMeAHint())
    }
}