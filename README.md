terrace
=======

How to build
------------

    ant

How to run
----------

    bin/terrace

Game Variants
-------------

Number of Players: The user has the option of playing a two player  game or a four player game. In the two player game, each player has 2 rows of pieces set up on opposite sides of the board. In the four player game, each player has 1 row of pieces set up on each side of the board. The Triangular board does not support four player since the  board is smaller and four player leaves too little options for people to move. The user has the option to play with 1 human player and 1 AI player,  2 human players, 3 human players and 1 AI player, or 4 human players.

Size: Each board has a small and large size. This is because on the standard “large” size, the game can last for a while, therefore we provided a small size if the user wanted to play a faster game.

The objective of the game is to get your “King Piece” (the box shaped piece) to the diagonally opposite corner space or capture the other player’s “King Piece.” Pieces are grouped into different sizes, all sizes have the same capturing power. This game also allows players to capture their own pieces to put themselves into a better position.

Movement Rules
--------------

### Square board

 - Standard Rules: Pieces can move to any vacant space that is on the same terrace as long as they do not jump over an opponents piece, directly or diagonally up a terrace, and directly down a terrace. They can capture diagonally downward as long as the piece it is capturing is the same size or smaller.

 - Downhill Rules: Same as the standard rules except pieces can also move any number of spots downwards as long as they do not jump over a piece.

 - Aggressive Rules: Same as the standard rules except pieces can also capture upwards diagonally as long as the piece it is capturing is at least one size smaller.

### Triangle board

 - Standard Rules: Pieces can move up terraces through corners, to any adjacent spaces on the same level, and down terraces through edges if the space if vacant. They can capture downwards through edges if the piece is the same size or smaller. 


Design
------

The game is represented by the GameState class which keeps track of the current player, the players still alive, the winner (if there is one), and the state of the board. The board is represented by a Board object, which can either be a DefaultBoard or a TriangleBoard. Each board holds a two dimensional array of GamePieces, which are the pieces that the player can move on the board. There is also a Player class and a PlayerColor class. There is a one-to-one mapping between Players and PlayerColors. The PlayerColors are used to render to the pieces on the board. There is also an enum called Variant which is used to set the variant for each GameState.

The game is run by a GameServer, which holds a GameState and controls the flow of the game by continuously asking the active player for it’s move. The GameServer runs in it’s own thread but communicates with the players (except AI) and GUI using message passing and callbacks.

The GUI hierarchy closely mirrors the backend. There is a GamePanel that keeps track of all the information about the current game. It contains a GameServer instance, as well as a GUIBoard instance. The GUIBoard instance is analogous to the Board class. Similar to the Board class, the GUIBoard class keeps a list of GamePieces, a class that is ananalogouso the backend’s GamePieces. There is also a GUITriangleBoard to handle rendering the triangle board. The tiles of the game are rendered using BoardTile class, from which TriangleTile and SquareTile extend.

AI
--

The AI relies on the minimax algorithm. It scores the board by taking the difference between the AI's pieces and the average of the other opponents' pieces. The players' pieces are scored based on the size of the piece: bigger pieces are scored with a higher value since in general, they tend to be more helpful for beating the other players.

When there are two players, the minimax algorithm looks forward 3 steps. When there are four players, the algorithm looks forward 4 steps. The additional step is necessary since you need to take into account all players. It is possible because there are fewer pieces total.

In order to look ahead, the AI must be sure to clone the game state such that it does not affect the original game.

Message Passing
---------------

In order to communicate between threads (GUI, game server, client threads), we implemented a message passing system which is based on the system in the programming language Rust. A port is the receiving end of a message queue, and each port and spawn any number of channels, which are the sending end. In Terrace we use this system mainly to send moves from the GUI to the game server through the player objects.

Networking
----------

Our project supports a simple client/server network architecture where up to four players can play on up to four different computers, but one will be designated as the server and will coordinate the game. (It is possible to have multiple players at a single computer, so for example you could have a four player game where one player is on the server, two are on one client, and the last is on another client.)

The server is supported simply by having the GameBuilder give networked player objects to the LocalGameServer. These players forward game state information to the clients. The clients use an entirely different instance of the GameServer interface, which does no turn control or move validation, but simply forwards state information to the GUI and move information from the local players to the server.
