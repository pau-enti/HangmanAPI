# RESTful API Hangman

This is a REST API for playing Hangman creating using Spring Boot. It can be played in Catalan, English and Spanish words.

It was based on the code https://github.com/sastani/Hangman by Sinaastani.

This is an adapted version written in Kotlin for the Degree in Video Game Development at ENTI-UB made by Pau GG. 
Developer contact at ```pau.garcia@enti.cat``` or ```pau.g.gozalvez@gmail.com```

# Installation:

## Clone this repository
``` 
git clone https://github.com/pau-enti/HangmanAPI
```
## Run

To run the server on localhost, use this command. Note that you can modify the default port (8080) 
using the command line --server.port=5002

```
java -jar releases/HangmanAPI-1.0.jar --server.port=5002
```

# Endpoints

## New game

``` 
GET localhost:5002/new
``` 
or 
``` 
POST localhost:5002/hangman
```

You can append this optional parameters:

- **lang**: modifies the language of the returned word. The available languages are 
  - cat
  - en
  - es
- **maxTries**: [Integer] set a maximum number of guesses. If this number is reached, the game status changes to lost. If this value is not set, there is no maximum guesses.

The result is:
```
{ "token" : String,           // Alphanumeric token for identify this game
  "hangman" : String,         // Hangman i.e for word water is "_____"
  "language" : [en/cat/es],   // Default en
  "maxTries" : Int or null
}
```

Examples:

GET localhost:5002/new?lang=cat&maxTries=7


## Make a guess:

``` 
POST localhost:8080/guess{"game":[gameId], "guess":[character]}**
``` 

To get a JSON object containing game data for every game in the current session make the following API call:

**POST localhost:8080/games**

If you pass a string of characters that is longer than one character,
it only keeps the first character and uses that to make a guess.

Additionally, if you guess a character that is correct multiple times, you are not penalized.
However, if you guess a wrong character multiple times, you will lose a turn each time.
