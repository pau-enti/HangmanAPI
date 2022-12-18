# REST Hangman

Welcome to the Hangman API! This API allows you to play Hangman games through HTTP requests. It can be played in Catalan, English and Spanish words.

It was based on the code https://github.com/sastani/Hangman by Sinaastani.

This is an adapted version written in Kotlin for the Degree in Video Game Development at ENTI-UB made by Pau GG. 
Developer contact at ```pau.garcia@enti.cat``` 


# Installation:

## Clone this repository
``` 
git clone https://github.com/pau-enti/HangmanAPI
```
## Run

```
java -jar target/Hangman-0.0.1-SNAPSHOT.jar --server.port=5002
```

This command launches the API on the specified port (if not set, the default port is 8080). 


# Hangman API


## Endpoints

### `GET /new?lang=<language code>&maxTries=<max tries>`

Creates a new Hangman game with the specified language and maximum number of tries. The response will include the unique game token created, as well as the word to guess in the form of underscores (_) to indicate the hidden letters.

The Hangman API supports multiple languages for the word to be guessed in the game. The available language codes are:

- `cat` for Catalan
- `en` for English
- `es` for Spanish

If no language is specified, the game will default to English.

`maxTries`: This parameter indicates the maximum number of incorrect guesses allowed before the player loses the game. If no value is specified, the default is `null` which means there is no limit of requests.

**Example**
```
GET /new?lang=es&maxTries=10

{
"token": "fad0b6a1ef-7cc1-4e71-8d43-40b9e55f7fds",
"language": "es",
"maxTries": 10,
"word": "_________",
"incorrectGuesses": 0
}
```


### `GET /games`

Returns a list of all active games. The parameters in each game are:

Here is a description of the other parameters in the Game class:

- `token`: A unique identifier for the game. This is used to identify a specific game when making API requests.
- `language`: The language of the word to be guessed in the game. This is one of the Language enum values (`cat`, `en`, or `es`).
- `maxTries`: The maximum number of incorrect guesses allowed before the player loses the game. If no value is specified, the default is `null` which means there is no limit of requests.
- `solution`: The word to be guessed in the game.
- `hangman`: A string representation of the game progress. Underscores (_) represent unguessed letters, and correctly guessed letters are shown in their proper positions.
- `incorrectGuesses`: The number of incorrect guesses the player has made so far in the game.
-  `status`: Indicates the current status of the game. There are three possible values for this field:
   - `ACTIVE`: The game is in progress and the player has not won or lost yet.
   - `WON`: The player has successfully guessed the word and won the game.
   - `LOST`: The player has made the maximum number of incorrect guesses and lost the game.

**Example**

```
GET /games

{
    "token": "dsasd6a1ef-7cc1-4e71-8d43-40b9e5324ds5f",
    "language": "en",
    "maxTries": null,
    "solution": "apple",
    "hangman": "___le",
    "status": "ACTIVE",
    "incorrectGuesses": 2
}

{
    "token": "7cc1-4e71-8d43-40b9e5324ds5f",
    "language": "cat",
    "maxTries": 10,
    "solution": "poma",
    "hangman": "poma",
    "status": "WON",
    "incorrectGuesses": 3
}

{
    "token": "dsasd6a1ef-8d43-40b9e5324ds5f",
    "language": "es",
    "maxTries": 8,
    "solution": "manzana",
    "hangman": "__nz___",
    "status": "LOST",
    "incorrectGuesses": 8
}


```

### `GET /game?token=<game token>`

Returns the details of the game with the specified token.

**Example**
```
GET /game?token=dsasd6a1ef-7cc1-4e71-8d43-40b9e5324ds5f

    {
        "token": "dsasd6a1ef-7cc1-4e71-8d43-40b9e5324ds5f",
        "language": "cat",
        "maxTries": null,
        "solution": "espectacular",
        "hangman": "____",
        "status": "ACTIVE",
        "incorrectGuesses": 3
    }
```


### `POST /guess`
Makes a guess at a letter in the word. Accepts a GuessInput object in the request body with the game's token and the letter being guessed. Returns a GuessOutput object with the updated game state.

**Example**
```
POST /guess

{
"token": "fad0b6a1ef-7cc1-4e71-8d43-40b9e55f7fds",
"letter": "a"
}
```
```
{
"token": "fad0b6a1ef-7cc1-4e71-8d43-40b9e55f7fds",
"hangman": "_a__a__",
"incorrectGuesses": 3,
"correct": true
}
```

### `GET /hint?token=<game token>`
Returns a hint for the game with the specified token.

**Example**

```
GET /hint?token=fad0b6a1ef-7cc1-4e71-8d43-40b9e55f7fds

{
"token": "fad0b6a1ef-7cc1-4e71-8d43-40b9e55f7fds",
"hint": "k"
}
```

## Compatibility Endpoints
The compatibility endpoints are provided for compatibility with older versions of the Hangman API. These endpoints accept request parameters in the query string rather than in the request body.

### `GET /hangman?token=<game token>`

This endpoint is equivalent to `GET /game?token=<game token>`

### `POST /hangman?lang=<language code>&maxTries=<max tries>`

This endpoint is equivalent to `GET /new?lang=<language code>&maxTries=<max tries>`

### `PUT /hangman?token=<game token>&letter=<letter>`

This endpoint is equivalent to `POST /guess` with a JSON body 