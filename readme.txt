# Board Game GUI and TextUI

Simple overview of use/purpose.

## Description

Used to run a game (or multiple games) of TicTacToe. The GUI games can be saved to a file after any turns, and games can be loaded from a file.
TextUI can only be used to play a game of TicTacToe, and only in the terminal. The player will play against another player, not the program.
For TicTacToe: The first player (who always goes first) uses “X” characters to populate the board while the second
uses the “O” character. Games will terminate when either player forms a straight line of 3 characters in any direction. For Numerical TicTacToe: users use odd or even numbers as
tokens and must create a sum of 15 across the board to win. The same numbers are not reused, and the player with odd numbers is always first.

## Getting Started

### Dependencies

No necessary dependencies (prerequisites, libraries, OS version, etc.) known

## Player file format

The player file saving and loading will only work with a specific loadout. The loadout is 4 integers in a text file seperated by newlines. A CSV will now work
because there are no commas. For example, if a player has id 1, has played 10 games, and won 6 (lost 4) the file would be written as can be seen below. note that
the first character is NOT a newline but "1". There is no newline after "4".

1
10
6
4

### Executing program

* cd to correct directory
* build with “gradle clean build”
* run with “gradle run” (also has info about how to run each program)
* execute TextUI with “java -cp build/classes/java/main boardgame.TextUI” (works on container)
* execute the GUI with "java -jar build/libs/A3.jar" (cannot be done in the container, you wil need Java installed. I used version 19)
* the GUI program can also be run by simply clicking on it (outside of the container)
* enter values as prompted by the program
```
use code blocks for commands
```
Expected output (for the TextUI program):

Welcome to a game of Tic Tac Toe!
Player 1 (X) goes first, followed by player 2 (O)
The game is won when either player can make a line of 3 characters or all board spaces are used

Player 1, please indicate where you would like to place your token.
Enter the row you want to pick (must be int, 1-3):

## Limitations

* Some thrown exceptions still cause the program to crash instead of restarting the GUI
* incorrectly formatted files will cause the program to crash

## Author Information

* Name: Martin Sergo
* Mail: msergo@uoguelph.ca
* Student number:1132977

## Development History
* 0.2
    * full implementation
* 0.1
    * Initial Release

## Acknowledgments

Inspiration, code snippets, etc.

* https://www.youtube.com/watch?v=Kmgo00avvEw&t=4537s
* https://gitlab.socs.uoguelph.ca/examples/exampleguiproject.git.
