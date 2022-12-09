## RESTful Hangman

REST API for playing Hangman creating using Spring Boot. It can be played in Catalan, English and Spanish words.

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
java -jar target/Hangman-0.0.1-SNAPSHOT.jar
```

## Making requests

To get a new game:

**GET localhost:8080/new**

To make a guess:

**POST localhost:8080/guess{"game":[gameId], "guess":[character]}**

To get a JSON object containing game data for every game in the current session make the following API call:

**POST localhost:8080/games**

If you pass a string of characters that is longer than one character,
it only keeps the first character and uses that to make a guess.

Additionally, if you guess a character that is correct multiple times, you are not penalized.
However, if you guess a wrong character multiple times, you will lose a turn each time.
